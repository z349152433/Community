package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.AccessTokenDto;
import com.cloudcode.springboot.community.dto.GithubUser;
import com.cloudcode.springboot.community.provider.Githubprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private Githubprovider githubprovider;

    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id("Iv1.cc8a97fc7f369230");
        accessTokenDto.setClient_secret("3f3092014723b0dac218b34036ef9af3f7c8d1df");
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDto.setState(state);
        String accessToken = githubprovider.getAccessToken(accessTokenDto);
        GithubUser user = githubprovider.getUser(accessToken);
        System.out.println(user.getId());
        System.out.println(user.getName());
        return "index";
    }

}
