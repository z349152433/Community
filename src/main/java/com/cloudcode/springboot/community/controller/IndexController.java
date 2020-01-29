package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.PageDTO;

import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size
                        ) {

        PageDTO pageInfo = questionService.list(page,size);
        model.addAttribute("pageInfo", pageInfo);
        return "index";
    }
}
