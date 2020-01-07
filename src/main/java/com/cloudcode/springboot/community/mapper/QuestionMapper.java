package com.cloudcode.springboot.community.mapper;

import com.cloudcode.springboot.community.dto.QuestionDTO;
import com.cloudcode.springboot.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void insertQuestion(Question question);

    @Select("select count(1) from question")
    Integer questionCount();

    @Select("select * from question")
    List<Question> listAll();

    @Select("select * from question where creator=#{userId}")
    List<Question> listById(Integer userId);

    @Select("select count(1) from question where creator=#{userId}")
    Integer questionCountById(Integer userId);

    @Select("select * from question where id=#{id}")
    Question findQuestionById(Integer id);
}
