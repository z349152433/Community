package com.cloudcode.springboot.community.service;

import com.cloudcode.springboot.community.dto.NotificationDTO;
import com.cloudcode.springboot.community.dto.PageDTO;
import com.cloudcode.springboot.community.mapper.NotificationMapper;
import com.cloudcode.springboot.community.model.Notification;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PageDTO listById(Integer id, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        PageHelper.startPage(page, size);
        List<Notification> notifications=notificationMapper.listByReceiverId(id);
        List<NotificationDTO> notificationDTOList=new ArrayList<>();

        return null;
    }
}
