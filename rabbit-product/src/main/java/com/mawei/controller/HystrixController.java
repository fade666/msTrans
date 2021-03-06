package com.mawei.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hystrix1")
public class HystrixController {

    @GetMapping("/sleep")
    public String sleep() throws InterruptedException {
        Thread.sleep(3001);
        return "我睡了几秒";
    }

}
