package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.ArticleDto;
import org.njupt.domain.dto.ArticleListDto;
import org.njupt.domain.entity.Article;
import org.njupt.domain.vo.AdminArticleDetailVo;


public interface ArticleService extends IService<Article> {

    /**
     * 查询热门文章，封装成ResponseResult返回
     */
    ResponseResult hotArticleList();

    /**
     * 根据分类，页码查询文章
     */
    ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 根据文章的id来获取文章的详细信息
     */
    ResponseResult getArticleDetail(Long id);

    /**
     * 更新浏览次数
     */
    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getDetails(Long id);

    ResponseResult updateArticle(AdminArticleDetailVo adminArticleDetailVo);

    ResponseResult deleteArticle(Long id);
}
