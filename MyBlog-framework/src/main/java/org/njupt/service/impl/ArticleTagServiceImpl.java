package org.njupt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.domain.entity.ArticleTag;
import org.njupt.mapper.ArticleTagMapper;
import org.njupt.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
