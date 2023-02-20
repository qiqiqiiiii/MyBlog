package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.ArticleDto;
import org.njupt.domain.dto.ArticleListDto;
import org.njupt.domain.vo.AdminArticleDetailVo;
import org.njupt.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
@Api(value = "博文接口",description = "处理博文的接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    @ApiOperation(value = "添加博文接口",notes = "实现添加博文")
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    @GetMapping("/list")
    @ApiOperation(value="博文列表接口",notes = "实现后台查看博文列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码"),
            @ApiImplicitParam(name="pageSize",value="每页的条数"),
    })
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.listArticle(pageNum,pageSize,articleListDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="查询文章详情接口",notes = "实现文章详情接口")
    @ApiImplicitParam(name="id",value = "文章Id")
    public ResponseResult getDetailById(@PathVariable Long id){
        return articleService.getDetails(id);
    }

    @PutMapping
    @ApiOperation(value="更新文章接口",notes = "实现更新文章")
    public ResponseResult updateArticle(@RequestBody AdminArticleDetailVo adminArticleDetailVo){
        return articleService.updateArticle(adminArticleDetailVo);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="更新文章接口",notes = "实现更新文章")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }


}
