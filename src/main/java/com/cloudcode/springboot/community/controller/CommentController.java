package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.CommentDTO;
import com.cloudcode.springboot.community.dto.ResultDTO;
import com.cloudcode.springboot.community.enums.CommentTypeEnum;
import com.cloudcode.springboot.community.exception.CustomizeErrorCode;
import com.cloudcode.springboot.community.mapper.CommentMapper;
import com.cloudcode.springboot.community.model.Comment;
import com.cloudcode.springboot.community.model.User;
import com.cloudcode.springboot.community.service.CommentService;
import com.cloudcode.springboot.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        //增加回复数
        if(comment.getType() == CommentTypeEnum.QUESTION.getType()){//评论的是问题
            questionService.incComment(comment.getParentId());
        }else {//评论的是评论
            Comment dbComment=commentService.findById(comment.getParentId());
            while (dbComment.getParentId() ==CommentTypeEnum.COMMENT.getType()){
                dbComment=commentService.findById(dbComment.getParentId());
            }
            questionService.incComment(dbComment.getParentId());
        }
        return ResultDTO.okOf();
    }
}
