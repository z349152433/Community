package com.cloudcode.springboot.community.service;

import com.cloudcode.springboot.community.dto.PageDTO;
import com.cloudcode.springboot.community.dto.QuestionDTO;
import com.cloudcode.springboot.community.mapper.QuestionMapper;
import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.Question;
import com.cloudcode.springboot.community.model.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public PageDTO list(Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();

        PageHelper.startPage(page,size);

        List<Question> questions=questionMapper.listAll();
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageDTO.setQuestions(questionDTOList);
        Integer totalCount = questionMapper.questionCount();

        pageDTO.setPageParma(totalCount, page, size);

        if (page < 1) page = 1;
        if (page > pageDTO.getTotalPage()) page = pageDTO.getTotalPage();
        return pageDTO;
    }

    public PageDTO listById(Integer userId, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        PageHelper.startPage(page,size);

        List<Question> questions=questionMapper.listById(userId);
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        pageDTO.setQuestions(questionDTOList);
        Integer totalCount = questionMapper.questionCountById(userId);

        pageDTO.setPageParma(totalCount, page, size);

        if (page < 1) page = 1;
        if (page > pageDTO.getTotalPage()) page = pageDTO.getTotalPage();
        return pageDTO;
    }

    public QuestionDTO findQuestionById(Integer id) {
        Question question=questionMapper.findQuestionById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建新的问题
            question.setGmtCreate(currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertQuestion(question);
        }else{
            //更新
            question.setGmtModified(currentTimeMillis());
            questionMapper.updateQuestion(question);
        }

    }
}
