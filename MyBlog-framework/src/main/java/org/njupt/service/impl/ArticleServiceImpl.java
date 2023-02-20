package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.ArticleDto;
import org.njupt.domain.dto.ArticleListDto;
import org.njupt.domain.entity.Article;
import org.njupt.domain.entity.ArticleTag;
import org.njupt.domain.entity.Category;
import org.njupt.domain.vo.*;
import org.njupt.mapper.ArticleMapper;
import org.njupt.service.ArticleService;
import org.njupt.service.ArticleTagService;
import org.njupt.service.CategoryService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private ArticleTagService articleTagService;

    /**
     * 查询热门文章，封装成ResponseResult返回
     */
    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章，status=0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);

        //按照浏览量进行排序，view_count
        queryWrapper.orderByDesc(Article::getViewCount);

        //最多只查询10条(第一页，每页10条记录)
        Page<Article> page = new Page<>(SystemConstants.HOTARTICLE_PAGE_NUM, SystemConstants.HOTARTICLE_NUM);
        page(page,queryWrapper);//page方法是IService中自带的方法
        //articleMapper.selectPage(page, queryWrapper);

        List<Article> articles = page.getRecords();

        //List<Article>集合中有很多无用的数据不需要传给前端，通过bean拷贝传给List<HotArticleVo>
        /*采用工具类来进行Bean的拷贝
        List<HotArticleVo> hotArticleVos=new ArrayList<>();
        for(Article art:articles){
            HotArticleVo hotArticleVo = new HotArticleVo();
            BeanUtils.copyProperties(art,hotArticleVo);
            hotArticleVos.add(hotArticleVo);
        }*/
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(hotArticleVos);
    }


    /**
     * 根据分类，页码查询文章
     */
    @Override
    public ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果 有categoryId
        /*if(null!=categoryId) {
            queryWrapper.eq(Article::getCategoryId,categoryId);
        }*/
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);

        //文章状态是发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //置顶，按照is_top的降序排
        queryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //查询categoryName
        List<Article> articleList = page.getRecords();
        //通过Article中的categoryId来查询categoryName
        /*采用For循环来进行查找
        for(Article article:articleList){
            //注意：这里article的categoryId不能为空，否则会出现空指针异常
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/
        //采用stream流的方式
        articleList = articleList.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        //获取分类id，查询分类信息，获取分类名称
                        // 把分类名称传递给article
                        //Article实体类中添加了@Accessors(chain = true),set方法的返回值类型是Article
                        return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                    }
                }).collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据文章的id来获取文章的详细信息
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //从redis中查询文章的浏览量
        Integer viewCount = redisCache.getCacheMapValue("viewCount", id.toString());
        articleDetailVo.setViewCount((Long.valueOf(viewCount)));
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(null!=category){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }


    /**
     * 更新浏览次数
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新Redis中对应id的浏览量
        //TODO 这里的viewCount可以修改为常量
        redisCache.incrementCacheMapValue("viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }


    @Override
    @Transactional //注意！！这里需要开启事务
    public ResponseResult addArticle(ArticleDto articleDto) {
        //转换成Article
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        //保存博文和tag的关系
        List<Long> tagIds = articleDto.getTags();
        List<ArticleTag> articleTags = tagIds.stream()
                .map(along -> new ArticleTag(article.getId(), along))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        Page<Article> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        page(page,queryWrapper);
        List<Article> articleList = page.getRecords();
        List<AdminArticleListVo> adminArticleListVos = BeanCopyUtils.copyBeanList(articleList, AdminArticleListVo.class);
        return ResponseResult.okResult(new PageVo(adminArticleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getDetails(Long id) {
        Article article = getById(id);
        AdminArticleDetailVo vo = BeanCopyUtils.copyBean(article, AdminArticleDetailVo.class);
        //将tags赋值给vo
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        List<Long> tags = articleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        vo.setTags(tags);
        return ResponseResult.okResult(vo);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(AdminArticleDetailVo adminArticleDetailVo) {
        Article article = BeanCopyUtils.copyBean(adminArticleDetailVo, Article.class);
        updateById(article);
        //将对应关系删除后保存
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,adminArticleDetailVo.getId());
        articleTagService.remove(wrapper);

        List<Long> tags = adminArticleDetailVo.getTags();
        List<ArticleTag> articleTags = tags.stream()
                .map(along -> new ArticleTag(article.getId(), along))
                .collect(Collectors.toList());
        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
