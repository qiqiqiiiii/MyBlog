package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.TagDto;
import org.njupt.domain.dto.TagUpdateDto;
import org.njupt.domain.entity.Tag;

public interface TagService extends IService<Tag> {
    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult detailTag(Long id);

    ResponseResult updateTag(TagUpdateDto tagUpdateDto);

    ResponseResult listAllTag();
}
