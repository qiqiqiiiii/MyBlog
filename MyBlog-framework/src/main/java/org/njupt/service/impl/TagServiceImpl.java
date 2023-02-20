package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.TagDto;
import org.njupt.domain.dto.TagUpdateDto;
import org.njupt.domain.entity.Tag;
import org.njupt.domain.vo.PageVo;
import org.njupt.domain.vo.TagListVo;
import org.njupt.domain.vo.TagVo;
import org.njupt.mapper.TagMapper;
import org.njupt.service.TagService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{
    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagDto tagDto) {
        //分页查询
        Page<Tag> page = new Page<>(pageNum,pageSize);

        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(tagDto.getName()),Tag::getName, tagDto.getName());
        queryWrapper.like(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark, tagDto.getRemark());
        page(page,queryWrapper);
        List<Tag> tags = page.getRecords();
        //封装数据返回
        return ResponseResult.okResult(new PageVo(tags,page.getTotal()));
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,id);
        remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult detailTag(Long id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(TagUpdateDto tagUpdateDto) {
        Tag tag = BeanCopyUtils.copyBean(tagUpdateDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tags = list();
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(tags, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}
