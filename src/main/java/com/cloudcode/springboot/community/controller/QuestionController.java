package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.CommentDTO;
import com.cloudcode.springboot.community.dto.QuestionDTO;
import com.cloudcode.springboot.community.enums.CommentTypeEnum;
import com.cloudcode.springboot.community.model.Question;
import com.cloudcode.springboot.community.service.CommentService;
import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){
        QuestionDTO questionDTO= questionService.findQuestionById(id);
        List<CommentDTO> comments=commentService.listById(id, CommentTypeEnum.QUESTION.getType());
        List<Question> relatedQuestions=questionService.listRelatedQuestionsByTag(questionDTO.getId(),questionDTO.getTag());
        //增加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
