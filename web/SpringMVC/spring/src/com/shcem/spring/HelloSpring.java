package com.shcem.spring;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/hello", method = RequestMethod.GET)
public class HelloSpring {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String printHello(ModelMap model) {
		model.addAttribute("msg", "Spring MVC Hello World");
		model.addAttribute("name", "yuntao");
		return "hello";
	}
}
