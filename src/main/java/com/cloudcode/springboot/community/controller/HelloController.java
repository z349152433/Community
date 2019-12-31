package com.cloudcode.springboot.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String index(Model model,@RequestParam(name = "name",required = false,defaultValue = "world") String name){
        model.addAttribute("name",name);
        return "hello";
    }
}
