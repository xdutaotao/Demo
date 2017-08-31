package com.gzfgeh.spring.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.gzfgeh.spring.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guzhenfu on 2017/8/31.
 */
@RestController
public class HelloSpring {


    //localhost:8080/hello
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    //localhost:8080/hello/{index}
    @RequestMapping("/hello/{index}")
    public String helloIndex(@PathVariable("index") int i){
        return "hello world" + i;
    }

    //localhost:8080/params?username=gzfgeh&age=111
    @RequestMapping("/params")
    public String helloParams(String username, String age){
        return username + age;
    }

    //localhost:8080/map?username=gzfgeh&age=111
    @PostMapping("/map")
    public String getMapParams(@RequestBody Map<String, Object> map){
        return map.get("username").toString() + map.get("age").toString();
    }

    //localhost:8080/data  post  '{"name":"gzfgeh","age":"11"}'
    @ResponseBody
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public User getPostData(String params){
        System.out.println(params);
        User user = new User();
        user.setName("gzfgeh");
        user.setAge("27");
        return user;
    }


    @RequestMapping(value = "/getdata", method = RequestMethod.POST)
    public User getData(User user){
        System.out.printf("ddd"+user.getName());
        return user;
    }

}
