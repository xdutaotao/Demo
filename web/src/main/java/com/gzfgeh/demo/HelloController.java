package com.gzfgeh.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Value("${cupSize}")
    private String cupSize;

    @Value("${age}")
    private Integer age;

    @Autowired
    private GirlProperties girlProperties;

    @GetMapping(value="/hello/{id}")
    public String say(@PathVariable Integer id) {
        return "id = "+id;
//        return girlProperties.getCupSize();
    }
}
