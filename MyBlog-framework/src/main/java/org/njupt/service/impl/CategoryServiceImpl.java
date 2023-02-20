package org.njupt.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Strings;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddCategoryDto;
import org.njupt.domain.dto.UpdateCategoryDto;
import org.njupt.domain.entity.Article;
import org.njupt.domain.entity.Category;
import org.njupt.domain.vo.*;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.mapper.CategoryMapper;
import org.njupt.service.ArticleService;
import org.njupt.service.CategoryService;

import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                }).collect(Collectors.toSet());
        //查询分类表
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        wrapper.in(Category::getId,categoryIds);
        List<Category> categoryList = list(wrapper);

        /*//采用lambda语法
        List<Category> categories = listByIds(categoryIds);
        Set<Category> categoryList = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUE_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toSet());*/

        List<BlogCategoryVo> result = BeanCopyUtils.copyBeanList(categoryList, BlogCategoryVo.class);
        return ResponseResult.okResult(result);

    }

    @Override
    public ResponseResult listAllCategory() {
        //不用考虑有没有文章添加了分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories = list(wrapper);
        List<AdminCategoryVo> adminCategoryVos = BeanCopyUtils.copyBeanList(categories, AdminCategoryVo.class);
        return ResponseResult.okResult(adminCategoryVos);
    }

    @Override
    public void export(HttpServletResponse response){
        //获取需要导出的数据
        List<Category> list = list();
        List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(list, ExcelCategoryVo.class);
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类表.xlsx",response);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            // 重置response
            response.reset();
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize, String name, String status) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(name),Category::getName,name);
        queryWrapper.eq(Strings.hasText(status),Category::getStatus,status);
        page(page,queryWrapper);
        List<Category> categories = page.getRecords();
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(categories, CategoryListVo.class);
        return ResponseResult.okResult(new PageVo(categoryListVos,page.getTotal()));
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDetailCategory(Long id) {
        Category category = getById(id);
        CategoryListVo categoryListVo = BeanCopyUtils.copyBean(category, CategoryListVo.class);
        return ResponseResult.okResult(categoryListVo);
    }

    @Override
    public ResponseResult updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
