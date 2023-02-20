package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "博文",description = "博文相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    @ApiOperation(value="热门博文",notes = "获取一定数量的热门博文")
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        return articleService.hotArticleList();

    }

    @GetMapping("/articleList")
    @ApiOperation(value = "分类博文列表",notes = "获取一页分类的博文")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码"),
            @ApiImplicitParam(name="pageSize",value="一页条数"),
            @ApiImplicitParam(name="categoryId",value="博文类别")
    })
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId) {
        return articleService.articleList(categoryId, pageNum, pageSize);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="博文详情",notes = "获取博文详情")
    @ApiImplicitParam(name="id",value="博文ID")
    public ResponseResult getArticleDetail(@PathVariable Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "博文浏览次数",notes = "更新博文浏览次数")
    @ApiImplicitParam(name="id",value="博文ID")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }

}
