package com.shcem.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by guzhenfu on 2017/8/30.
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    //localhost:8080/hello/spring
    @RequestMapping(value = "/spring", method = RequestMethod.GET)
    public String printHello(ModelMap model){
        model.addAttribute("msg", "Spring MVC Hello World");
        model.addAttribute("name", "shcem");
        return "hello";
    }

    //localhost:8080/hello/restful/123
    @RequestMapping(value = "/restful/{id}", method = RequestMethod.GET)
    public String restfulHello(@PathVariable("id")Integer id, Map<String ,String> model){
        model.put("msg", "Spring MVC Hello World");
        model.put("name", "shcem");
        return id + "hello";
    }
}
