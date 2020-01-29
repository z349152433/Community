package com.cloudcode.springboot.community.controller;

import com.cloudcode.springboot.community.dto.FileUploadDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileUploadController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileUploadDTO upload(){
        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setSuccess(1);
        fileUploadDTO.setUrl("/images/tiger.png");
        return fileUploadDTO;
    }
}
