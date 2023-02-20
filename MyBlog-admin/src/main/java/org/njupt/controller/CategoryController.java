package org.njupt.controller;

import io.swagger.annotations.Api;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddCategoryDto;
import org.njupt.domain.dto.UpdateCategoryDto;
import org.njupt.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/content/category")
@Api(value = "分类", description = "分类相关的接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        return categoryService.listAllCategory();
    }

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('context:category:export')")
    public void export(HttpServletResponse response) {
        categoryService.export(response);
    }

    @GetMapping("/list")
    public ResponseResult listCategory(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.listCategory(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getDetailCategory(@PathVariable Long id){
        return categoryService.getDetailCategory(id);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto){
        return categoryService.updateCategory(updateCategoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }
}
