package com.chqiuu.zhi-mo.api.article.dto;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private String author;
    private String category;
    private Integer status;
    private String sourceUrl;
    private String createTime;
    private String updateTime;
}
