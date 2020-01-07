package com.cloudcode.springboot.community.mapper;

import com.cloudcode.springboot.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into USER (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id=#{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(String accountId);

    @Update("update user set name=#{name} ,token=#{token}, gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where account_ad=#{accountId}")
    void update(User dbUser);

}
