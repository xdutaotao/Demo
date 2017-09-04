package com.gzfgeh.spring.controller;

import com.gzfgeh.spring.dao.model.User;
import com.gzfgeh.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by guzhenfu on 2017/8/31.
 */
@RestController
public class HelloSpring {
    @Autowired
    private UserService service;

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

    //localhost:8080/data  post  '{"name":"gzfgeh","age":"11"}'
    @RequestMapping(value = "/getdata", method = { RequestMethod.POST, RequestMethod.GET })
    public User getData(@RequestBody User user){
        System.out.printf("ddd"+user.getName());
        return user;
    }

    //localhost:8080/getUserData  post  '{"name":"gzfgeh","age":"11"}'
    @RequestMapping(value = "/getUserData", method = {RequestMethod.POST, RequestMethod.GET})
    public User getUserData(@RequestBody User user){
        return service.selectById(user.getId());
    }

}
