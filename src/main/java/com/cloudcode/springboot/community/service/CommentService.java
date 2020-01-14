package com.cloudcode.springboot.community.service;

import com.cloudcode.springboot.community.enums.CommentTypeEnum;
import com.cloudcode.springboot.community.exception.CustomizeErrorCode;
import com.cloudcode.springboot.community.exception.CustomizeException;
import com.cloudcode.springboot.community.mapper.CommentMapper;
import com.cloudcode.springboot.community.mapper.QuestionMapper;
import com.cloudcode.springboot.community.model.Comment;
import com.cloudcode.springboot.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper  questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment= commentMapper.selectById(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            //回复问题
            Question question = questionMapper.findQuestionById(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
    }

    public Integer findParentIdById(Integer id) {
        return commentMapper.findParentIdById(id);
    }


    public Comment findById(Integer parentId) {
        return commentMapper.findById(parentId);
    }
}
