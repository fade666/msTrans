package com.mawei.controller;

import com.mawei.domain.MessageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/demo")
public class consumerController {

    @PostMapping("/dealMsg")
    public void dealMessage(@RequestBody MessageRecord msg){
        log.info(msg.toString());
        log.info("下游消费消息+++++++++++");
    }

}
