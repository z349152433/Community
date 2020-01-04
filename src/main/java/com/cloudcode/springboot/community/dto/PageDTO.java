package com.cloudcode.springboot.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPageParma(Integer totalCount, Integer page, Integer size) {

        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        this.totalPage=totalPage;

        this.currentPage=page;

        pages.add(page);
        for (int i = 1; i < 4; i++) {
            if (page - i > 0) pages.add(0,page - i);
            if (page + i <= totalPage) pages.add(page + i);
        }


        if (page == 1) this.showPrevious = false;
        else this.showPrevious = true;
        if (page == totalPage) this.showNext = false;
        else this.showNext = true;

        if (pages.contains(1)) this.showFirstPage = false;
        else this.showFirstPage = true;
        if (pages.contains(totalPage)) this.showEndPage = false;
        else this.showEndPage = true;


    }
}
