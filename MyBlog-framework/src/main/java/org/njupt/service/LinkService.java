package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddLinkDto;
import org.njupt.domain.dto.UpdateLinkDto;
import org.njupt.domain.entity.Link;

public interface LinkService extends IService<Link> {
    /**
     * 查询所有的审核通过的友链
     */
    ResponseResult getAllLink();

    ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult detailLink(Long id);

    ResponseResult updateLink(UpdateLinkDto updateLinkDto);

    ResponseResult deleteLink(Long id);

}
