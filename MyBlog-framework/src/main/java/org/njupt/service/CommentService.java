package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.Comment;

public interface CommentService extends IService<Comment> {
    /**
     * 查看评论
     */
    ResponseResult commentList(String commentType,Long articleId,Integer pageNum,Integer pageSize);

    /**
     * 添加评论
     */
    ResponseResult addComment(Comment comment);

}
