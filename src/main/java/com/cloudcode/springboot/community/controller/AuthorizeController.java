package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.AccessTokenDto;
import com.cloudcode.springboot.community.dto.GithubUser;
import com.cloudcode.springboot.community.mapper.UserMapper;
import com.cloudcode.springboot.community.model.User;
import com.cloudcode.springboot.community.provider.Githubprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private Githubprovider githubprovider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirect_uri);
        accessTokenDto.setState(state);
        String accessToken = githubprovider.getAccessToken(accessTokenDto);
        GithubUser githubuser = githubprovider.getUser(accessToken);
        if (githubuser != null && githubuser.getId() != null) {
            //登陆成功
            //创建user对象并写入数据库
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubuser.getName());
            user.setAccountId(String.valueOf(githubuser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubuser.getAvatarUrl());
            userMapper.insertUser(user);
            //将token写入cookie实现持久化登陆状态
            response.addCookie(new Cookie("token", token));

            return "redirect:/";
        } else {
            //登陆失败
            return "redirect:/";
        }
    }

}
