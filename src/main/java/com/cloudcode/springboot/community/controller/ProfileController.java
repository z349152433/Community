package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.PageDTO;
import com.cloudcode.springboot.community.model.User;
import com.cloudcode.springboot.community.service.NotificationService;
import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/questions")
    public String myQuestions(Model model,
                              HttpServletRequest request,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "5") Integer size) {
        model.addAttribute("section", "questions");
        model.addAttribute("sectionName", "我的提问");

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return "redirect:/";


        PageDTO pageInfo = questionService.listById(user.getId(), page, size);
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }

    @GetMapping("/replies")
    public String recentReplies(Model model,
                                HttpServletRequest request,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        model.addAttribute("section", "replies");
        model.addAttribute("sectionName", "最新回复");

        User user = (User) request.getSession().getAttribute("user");
        PageDTO pageInfo =notificationService.listById(user.getId(),page,size);
        model.addAttribute("pageInfo", pageInfo);
        return "profile";
    }
}
