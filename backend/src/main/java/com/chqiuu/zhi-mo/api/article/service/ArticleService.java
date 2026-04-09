package com.chqiuu.zhi-mo.api.article.service;

import com.chqiuu.zhi-mo.api.article.dto.ArticleDTO;
import com.chqiuu.zhi-mo.api.article.dto.ArticleSaveDTO;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getArticleList();
    ArticleDTO getById(Long id);
    void saveArticle(ArticleSaveDTO dto);
    void updateArticle(ArticleSaveDTO dto);
    void deleteArticle(Long id);
    ArticleDTO importArticle(ArticleSaveDTO dto);
}
