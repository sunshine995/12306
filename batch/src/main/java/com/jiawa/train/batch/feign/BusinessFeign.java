package com.jiawa.train.batch.feign;

import com.jiawa.train.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

// 客户端 business就是服务端，通过客户端调用服务端接口
@FeignClient("business")
//@FeignClient(name = "business", url = "http://127.0.0.1:8084/business")
public interface BusinessFeign {

    @GetMapping("/member/hello")
    String hello();

    @GetMapping("/business/admin/daily-train/gen-daily/{date}")
    CommonResp<Object> genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
