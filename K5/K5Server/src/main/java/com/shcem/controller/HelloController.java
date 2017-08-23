package com.shcem.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by GUZHENFU on 2017/8/18.
 */
@RestController
public class HelloController {

    @RequestMapping("/hello/{id}")
    public String index(){
        return "Hello Spring Boot nia";
    }
}
