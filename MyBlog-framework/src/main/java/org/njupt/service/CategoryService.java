package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddCategoryDto;
import org.njupt.domain.dto.UpdateCategoryDto;
import org.njupt.domain.entity.Category;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface CategoryService extends IService<Category> {
    /**
     * 查询文章分类列表
     * @return
     */
    ResponseResult getCategoryList();

    ResponseResult listAllCategory();


    void export(HttpServletResponse response);

    ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getDetailCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto);

    ResponseResult deleteCategory(Long id);

}
