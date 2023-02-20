package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.njupt.annotation.SystemLog;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddCommentDto;
import org.njupt.domain.entity.Comment;
import org.njupt.service.CommentService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value="博文评论",notes="查看博文评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="articleId",value="博文ID"),
            @ApiImplicitParam(name="pageNum",value="页码"),
            @ApiImplicitParam(name="pageSize",value="一页条数")
    })
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }

    @PostMapping
    @ApiOperation(value="添加评论",notes="添加博文/友链评论")
    @SystemLog(businessName="添加评论")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    @ApiOperation(value="友链评论",notes="查看友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码"),
            @ApiImplicitParam(name="pageSize",value="一页条数")
    })
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }

}
