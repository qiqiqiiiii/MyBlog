package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags = "分类",description = "分类相关接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ApiOperation(value="分类列表",notes="获取分类列表")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}
