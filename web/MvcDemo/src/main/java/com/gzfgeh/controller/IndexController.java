package com.gzfgeh.controller;

import com.gzfgeh.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.util.Date;

@Controller
@RequestMapping("/home")
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/getPerson")
    public void getPerson(String name, PrintWriter pw){
        pw.write("hello, "+name);
    }

    @ResponseBody
    @RequestMapping("/user")
    public  User get(){
        User u = new User();
        u.setId(1);
        u.setName("gzfgeh");
        u.setBirth(new Date());
        return u;
    }

}
