package com.cloudcode.springboot.community.service;

import com.cloudcode.springboot.community.dto.CommentDTO;
import com.cloudcode.springboot.community.enums.CommentTypeEnum;
import com.cloudcode.springboot.community.enums.NotificationStatusEnum;
import com.cloudcode.springboot.community.enums.NotificationTypeEnum;
import com.cloudcode.springboot.community.exception.CustomizeErrorCode;
import com.cloudcode.springboot.community.exception.CustomizeException;
import com.cloudcode.springboot.community.mapper.CommentMapper;
import com.cloudcode.springboot.community.mapper.NotificationMapper;
import com.cloudcode.springboot.community.mapper.QuestionMapper;
import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.Comment;
import com.cloudcode.springboot.community.model.Notification;
import com.cloudcode.springboot.community.model.Question;
import com.cloudcode.springboot.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //更新回复数
            while (dbComment.getType() == CommentTypeEnum.COMMENT.getType()) {
                dbComment = commentMapper.selectById(comment.getParentId());
            }
            questionService.incComment(dbComment.getParentId());
            //更新子评论数
            commentMapper.updateCommentCount(dbComment.getId(),dbComment.getCommentCount()+1);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);
        } else {
            //回复问题
            Question question = questionMapper.findQuestionById(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionService.incComment(comment.getParentId());//更新回复数
            //创建通知
            createNotify(comment,question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
        }
    }

    private void createNotify(Comment comment, Integer receiver, NotificationTypeEnum notificationType) {
        Notification notification=new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listById(Integer id, Integer type) {
        List<Comment> comments = commentMapper.listByQuestionId(id, type);
        if (comments.size() == 0) return new ArrayList<>();
        //获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人对象并转换为map
        List<User> users = new ArrayList<>();
        for (Integer userId : userIds) {
            User user = userMapper.findById(userId);
            users.add(user);
        }
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOs;
    }
}
