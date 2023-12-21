package com.jiawa.train.business.controller.admin;

import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.resp.CommonResp;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.business.req.daily_trainQueryReq;
import com.jiawa.train.business.req.daily_trainSaveReq;
import com.jiawa.train.business.resp.daily_trainQueryResp;
import com.jiawa.train.business.service.daily_trainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class daily_trainAdminController {

    @Resource
    private daily_trainService daily_trainService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody daily_trainSaveReq req) {
        daily_trainService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<daily_trainQueryResp>> queryList(@Valid daily_trainQueryReq req) {
        PageResp<daily_trainQueryResp> list = daily_trainService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        daily_trainService.delete(id);
        return new CommonResp<>();
    }

    @GetMapping("/gen-daily/{date}")
    public CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        daily_trainService.genDaily(date);
        return new CommonResp<>();
    }

}
