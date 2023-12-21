package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.Train;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.daily_train;
import com.jiawa.train.business.domain.daily_trainExample;
import com.jiawa.train.business.mapper.daily_trainMapper;
import com.jiawa.train.business.req.daily_trainQueryReq;
import com.jiawa.train.business.req.daily_trainSaveReq;
import com.jiawa.train.business.resp.daily_trainQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class daily_trainService {

    private static final Logger LOG = LoggerFactory.getLogger(daily_trainService.class);

    @Resource
    private daily_trainMapper daily_trainMapper;

    @Resource
    private TrainService trainService;
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

    public void save(daily_trainSaveReq req) {
        DateTime now = DateTime.now();
        daily_train daily_train = BeanUtil.copyProperties(req, daily_train.class);
        if (ObjectUtil.isNull(daily_train.getId())) {
            daily_train.setId(SnowUtil.getSnowflakeNextId());
            daily_train.setCreateTime(now);
            daily_train.setUpdateTime(now);
            daily_trainMapper.insert(daily_train);
        } else {
            daily_train.setUpdateTime(now);
            daily_trainMapper.updateByPrimaryKey(daily_train);
        }
    }

    public PageResp<daily_trainQueryResp> queryList(daily_trainQueryReq req) {
        daily_trainExample daily_trainExample = new daily_trainExample();
        daily_trainExample.setOrderByClause("date desc,code asc");
        daily_trainExample.Criteria criteria = daily_trainExample.createCriteria();

        // 判断日期
        if (ObjectUtil.isNotNull(req.getDate())){
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())){
            criteria.andCodeEqualTo(req.getCode());
        }

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(), req.getSize());
        List<daily_train> daily_trainList = daily_trainMapper.selectByExample(daily_trainExample);

        PageInfo<daily_train> pageInfo = new PageInfo<>(daily_trainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<daily_trainQueryResp> list = BeanUtil.copyToList(daily_trainList, daily_trainQueryResp.class);

        PageResp<daily_trainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void delete(Long id) {
        daily_trainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 生成某日所有车次信息，包括车次、车站、车厢、座位
     * @param date
     */
    public void genDaily(Date date) {
        List<Train> trainList = trainService.selectAll();
        if (CollUtil.isEmpty(trainList)) {
            LOG.info("没有车次基础数据，任务结束");
            return;
        }

        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    // 面试时事务的传播机制，
    @Transactional
    public void genDailyTrain(Date date, Train train) {
        LOG.info("生成日期【{}】车次【{}】的信息开始", DateUtil.formatDate(date), train.getCode());
        // 删除该车次已有的数据
        daily_trainExample dailyTrainExample = new daily_trainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        daily_trainMapper.deleteByExample(dailyTrainExample);

        // 生成该车次的数据
        DateTime now = DateTime.now();
        daily_train dailyTrain = BeanUtil.copyProperties(train, daily_train.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        daily_trainMapper.insert(dailyTrain);

        // 生成该车次的车站数据
        dailyTrainStationService.genDaily(date, train.getCode());

        // 生成该车次的车厢数据
        dailyTrainCarriageService.genDaily(date, train.getCode());

        // 生成该车次的座位数据
        dailyTrainSeatService.genDaily(date, train.getCode());

        // 生成该车次的余票数据
        dailyTrainTicketService.getDaily(dailyTrain, date, train.getCode());

        LOG.info("生成日期【{}】车次【{}】的信息结束", DateUtil.formatDate(date), train.getCode());
    }
}
