package com.chqiuu.zhi-mo.api.article.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class ArticleSaveDTO {

    private Long id;

    @NotBlank(message = "标题不能为空")
    @Length(max = 200, message = "标题长度不能超过200")
    private String title;

    @Length(max = 500, message = "摘要长度不能超过500")
    private String summary;

    @NotBlank(message = "正文不能为空")
    private String content;

    private String coverImage;

    @Length(max = 50, message = "作者长度不能超过50")
    private String author;

    @Length(max = 20, message = "分类长度不能超过20")
    private String category;

    private String sourceUrl;
}
