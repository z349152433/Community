package com.cloudcode.springboot.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
