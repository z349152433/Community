package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.QuestionDTO;
import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.Question;
import com.cloudcode.springboot.community.model.User;
import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(HttpServletRequest request,Model model){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length!=0){
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user=userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        List<QuestionDTO> questionList=questionService.list();
        model.addAttribute("questions",questionList);
        return "index";
    }
}
