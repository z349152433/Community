package com.cloudcode.springboot.community.mapper;

import com.cloudcode.springboot.community.dto.CommentDTO;
import com.cloudcode.springboot.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment (parent_id,type,commentator,gmt_create,gmt_modified,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id =#{id}")
    Comment selectById(Integer id);

    @Select("select * from comment where parent_id=#{id} and type=#{type}")
    List<Comment> listByQuestionId(Integer id, Integer type);
}
