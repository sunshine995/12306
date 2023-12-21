package com.jiawa.train.business.mapper;

import com.jiawa.train.business.domain.daily_train;
import com.jiawa.train.business.domain.daily_trainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface daily_trainMapper {
    long countByExample(daily_trainExample example);

    int deleteByExample(daily_trainExample example);

    int deleteByPrimaryKey(Long id);

    int insert(daily_train record);

    int insertSelective(daily_train record);

    List<daily_train> selectByExample(daily_trainExample example);

    daily_train selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") daily_train record, @Param("example") daily_trainExample example);

    int updateByExample(@Param("record") daily_train record, @Param("example") daily_trainExample example);

    int updateByPrimaryKeySelective(daily_train record);

    int updateByPrimaryKey(daily_train record);
}