package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Strings;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddLinkDto;
import org.njupt.domain.dto.UpdateLinkDto;
import org.njupt.domain.entity.Link;
import org.njupt.domain.vo.LinkDetailVo;
import org.njupt.domain.vo.LinkListVo;
import org.njupt.domain.vo.PageVo;
import org.njupt.mapper.LinkMapper;

import org.njupt.service.LinkService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.domain.vo.LinkVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper,Link> implements LinkService{
    /**
     * 查询所有的审核通过的友链
     */
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_APPROVED);
        List<Link> linkList = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, String name, String status) {
        Page<Link> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.hasText(name),Link::getName,name);
        queryWrapper.eq(Strings.hasText(status),Link::getStatus,status);
        page(page,queryWrapper);
        List<Link> links = page.getRecords();
        List<LinkListVo> linkListVos = BeanCopyUtils.copyBeanList(links, LinkListVo.class);
        return ResponseResult.okResult(new PageVo(linkListVos,page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult detailLink(Long id) {
        Link link = getById(id);
        LinkDetailVo linkDetailVo = BeanCopyUtils.copyBean(link, LinkDetailVo.class);
        return ResponseResult.okResult(linkDetailVo);
    }

    @Override
    public ResponseResult updateLink(UpdateLinkDto updateLinkDto) {
        Link link = BeanCopyUtils.copyBean(updateLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}