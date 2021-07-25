package com.mawei.service;

import com.mawei.entity.pojo.MessageRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "rabbit-product")
@Component
public interface DictFeignClient {

    @PostMapping ("/demo/dealMsg")
    public void dealMsg(@RequestBody  MessageRecord msg);

    @GetMapping("/sleep")
    public String sleep();
}
