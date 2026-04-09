package com.chqiuu.zhi-mo.api.article.controller;

import com.chqiuu.zhi-mo.api.article.dto.ArticleDTO;
import com.chqiuu.zhi-mo.api.article.dto.ArticleSaveDTO;
import com.chqiuu.zhi-mo.api.article.service.ArticleService;
import com.chqiuu.zhi-mo.common.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/list")
    public Result<List<ArticleDTO>> list() {
        return Result.ok(articleService.getArticleList());
    }

    @GetMapping("/{id}")
    public Result<ArticleDTO> getById(@PathVariable Long id) {
        return Result.ok(articleService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody @Validated ArticleSaveDTO dto) {
        articleService.saveArticle(dto);
        return Result.ok();
    }

    @PutMapping
    public Result<Void> update(@RequestBody @Validated ArticleSaveDTO dto) {
        articleService.updateArticle(dto);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.ok();
    }

    @PostMapping("/import")
    public Result<ArticleDTO> importArticle(@RequestBody ArticleSaveDTO dto) {
        return Result.ok(articleService.importArticle(dto));
    }
}
