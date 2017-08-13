package com.gzfgeh.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${cupSize}")
    private String cupSize;

    @Value("${age}")
    private Integer age;

    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value="/hello/{id}", method= RequestMethod.GET)
    public String say(@PathVariable Integer id) {
        return "id = "+id;
//        return girlProperties.getCupSize();
    }
}
