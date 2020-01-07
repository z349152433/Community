package com.cloudcode.springboot.community.service;

import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        if (dbUser == null) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertUser(user);
        } else {
            //更新token
            user.setGmtModified(System.currentTimeMillis());
            user.setAvatarUrl(dbUser.getAvatarUrl());
            user.setName(dbUser.getName());
            user.setToken(dbUser.getToken());
            userMapper.update(dbUser);
        }
    }
}
