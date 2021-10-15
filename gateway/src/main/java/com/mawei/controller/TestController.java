package com.mawei.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private static ThreadLocal<String> a = new ThreadLocal<String>();

    @GetMapping("/test")
    public String test(){
        return "hahaha";
    }
}
