package com.mawei.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Maw
 * @Date 2021/7/19 23:42
 * @Version 1.0
 */
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    //defaultfallback:服务降级的方法
    @GetMapping("/delay")
    @HystrixCommand(defaultFallback = "delayFallback",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "3000")//超时时间为3秒，超过则又降级方法处理
    })
    public String delay() throws InterruptedException {
        //休眠五秒触发超时条件
        Thread.sleep(5000L);
        return "访问成功";
    }

    public String delayFallback(){
         return "超时啦！ 我触发了降级方法";
    }

}
