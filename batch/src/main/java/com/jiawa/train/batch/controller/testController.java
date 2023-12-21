package com.jiawa.train.batch.controller;

import com.jiawa.train.batch.feign.BusinessFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @Resource
    private BusinessFeign businessFeign;

    @GetMapping("/hello")
    public String hello(){
        String hello = businessFeign.hello();
        System.out.println(hello);
        return "hello world";
    }
}
