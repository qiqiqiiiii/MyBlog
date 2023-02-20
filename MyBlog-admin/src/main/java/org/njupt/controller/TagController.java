package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.TagDto;
import org.njupt.domain.dto.TagUpdateDto;
import org.njupt.domain.vo.PageVo;
import org.njupt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签",description = "标签相关接口")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    @ApiOperation(value = "展示标签接口",notes = "根据条件展示标签")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value = "页码"),
            @ApiImplicitParam(name="pageSize",value = "一页的条数")
    })
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.pageTagList(pageNum,pageSize, tagDto);
    }

    @PostMapping
    @ApiOperation(value = "添加标签接口",notes = "实现添加标签")
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="删除标签接口",notes = "实现标签删除")
    @ApiImplicitParam(name = "id",value = "标签Id")
    public ResponseResult delete(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="标签详情接口",notes = "展示标签详情")
    @ApiImplicitParam(name = "id",value = "标签Id")
    public ResponseResult detailTag(@PathVariable Long id){
        return tagService.detailTag(id);
    }

    @PutMapping
    @ApiOperation(value="更新标签接口",notes = "实现更新标签")
    public ResponseResult updateTag(@RequestBody TagUpdateDto tagUpdateDto){
        return tagService.updateTag(tagUpdateDto);
    }

    @GetMapping("listAllTag")
    @ApiOperation(value="查询标签接口",notes = "实现查询所有标签")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}
