package org.njupt.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.entity.Article;
import org.njupt.service.ArticleService;
import org.njupt.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //查询文章信息 id ViewCount
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);

        //alt+回车可以转换为lambda类型
        //redisCache中的setCacheMap要求Map是<String,T>类型
        Map<String, Integer> viewCountMap = articleList.stream()
                .collect(Collectors.toMap(article1 -> article1.getId().toString(), article -> {
                    //如果返回值是Long, eg：1L在redis中不能递增，必须使用Integer类型
                    return article.getViewCount().intValue();
                }));

        //存储到redis中
        redisCache.setCacheMap("viewCount",viewCountMap);
    }
}
