package com.cloudcode.springboot.community.mapper;

import com.cloudcode.springboot.community.dto.QuestionDTO;
import com.cloudcode.springboot.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void insertQuestion(Question question);

    @Select("select count(1) from question")
    Integer questionCount();

    @Select("select * from question order by gmt_create DESC")
    List<Question> listAll();

    @Select("select * from question where creator=#{userId}")
    List<Question> listById(Integer userId);

    @Select("select count(1) from question where creator=#{userId}")
    Integer questionCountById(Integer userId);

    @Select("select * from question where id=#{id}")
    Question findQuestionById(Integer id);

    @Update("update question set title = #{title}, description = #{description}, gmt_modified = #{gmtModified}, tag = #{tag}  where id=#{id}")
    Integer updateQuestion(Question question);

    @Update("update question set view_count = #{viewCount}  where id = #{id} ")
    void updateViewCount(Integer id, Integer viewCount);

    @Update("update question set comment_count = #{commentCount}  where id = #{id}")
    void updateCommentCount(Integer id, Integer commentCount);

    @Select("select * from question where tag regexp #{tagString} and id != #{id}")
    List<Question> listRelatedQuestionByTag(String tagString, Integer id);
}
