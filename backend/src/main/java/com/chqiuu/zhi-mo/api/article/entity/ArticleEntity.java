package com.chqiuu.zhi-mo.api.article.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chqiuu.zhi-mo.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class ArticleEntity extends BaseEntity {

    private String title;

    private String summary;

    private String content;

    private String coverImage;

    private String author;

    private String category;

    private Integer status;

    private String sourceUrl;
}
