package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.PageDTO;
import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.User;
import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;


    @GetMapping("/questions")
    public String myQuestions(Model model,
                              HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "5") Integer size){
        model.addAttribute("section","questions");
        model.addAttribute("sectionName","我的提问");
        Integer userId = null;
        User user=null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    userId=user.getId();
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        if(user == null)  return "redirect:/" ;


        PageDTO pageInfo = questionService.listById(userId,page,size);
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }

    @GetMapping("/replies")
    public String recentReplies(Model model){
        model.addAttribute("section","replies");
        model.addAttribute("sectionName","最新回复");

        return  "profile";
    }
}
