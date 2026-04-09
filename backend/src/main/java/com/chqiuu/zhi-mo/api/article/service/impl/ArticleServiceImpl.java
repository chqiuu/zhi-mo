package com.chqiuu.zhi-mo.api.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chqiuu.zhi-mo.api.article.dto.ArticleDTO;
import com.chqiuu.zhi-mo.api.article.dto.ArticleSaveDTO;
import com.chqiuu.zhi-mo.api.article.entity.ArticleEntity;
import com.chqiuu.zhi-mo.api.article.mapper.ArticleMapper;
import com.chqiuu.zhi-mo.api.article.service.ArticleService;
import com.chqiuu.zhi-mo.common.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ArticleDTO> getArticleList() {
        LambdaQueryWrapper<ArticleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ArticleEntity::getCreateTime);
        return articleMapper.selectList(wrapper).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleDTO getById(Long id) {
        ArticleEntity entity = articleMapper.selectById(id);
        if (entity == null) {
            throw new UserException("文章不存在");
        }
        return toDTO(entity);
    }

    @Override
    public void saveArticle(ArticleSaveDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        articleMapper.insert(entity);
    }

    @Override
    public void updateArticle(ArticleSaveDTO dto) {
        if (dto.getId() == null) {
            throw new UserException("文章ID不能为空");
        }
        ArticleEntity entity = articleMapper.selectById(dto.getId());
        if (entity == null) {
            throw new UserException("文章不存在");
        }
        BeanUtils.copyProperties(dto, entity);
        articleMapper.updateById(entity);
    }

    @Override
    public void deleteArticle(Long id) {
        articleMapper.deleteById(id);
    }

    @Override
    public ArticleDTO importArticle(ArticleSaveDTO dto) {
        ArticleEntity entity = new ArticleEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus(0);
        articleMapper.insert(entity);
        return toDTO(entity);
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setCreateTime(entity.getCreateTime() != null ? entity.getCreateTime().format(FORMATTER) : null);
        dto.setUpdateTime(entity.getUpdateTime() != null ? entity.getUpdateTime().format(FORMATTER) : null);
        return dto;
    }
}
