package com.cloudcode.springboot.community.cache;

import com.cloudcode.springboot.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> getTagList() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO language = new TagDTO();
        language.setCategoryName("开发语言");
        language.setTags(Arrays.asList("Javascript", "Java", "c", "c++", "Python","c#","go","kotlin","sql","swift","ruby","php","vb","rust","css","html"));
        tagDTOS.add(language);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("开发框架");
        framework.setTags(Arrays.asList("Spring", "Springboot", "SpringMVC", "mybatis", "SpringCloud", "Vue", "React", "Angular"));
        tagDTOS.add(framework);
        return tagDTOS;
    }


    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = getTagList();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalidTags = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining());
        return invalidTags;
    }
}
