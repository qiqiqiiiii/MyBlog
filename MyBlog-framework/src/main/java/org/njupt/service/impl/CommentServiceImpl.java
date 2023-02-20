package org.njupt.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.Comment;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.exception.SystemException;
import org.njupt.mapper.CommentMapper;
import org.njupt.service.CommentService;
import org.njupt.service.UserService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.domain.vo.CommentVo;
import org.njupt.domain.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    /**
     * 查看评论
     */
    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对评论类型进行判断
        queryWrapper.eq(Comment::getType,commentType)
                //对articleId进行判断(当评论类型是0的时候，才能查文章id)
                .eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId)
                //根评论rootId=-1
                .eq(Comment::getRootId, SystemConstants.COMMENT_ROOT)
                .orderByAsc(Comment::getCreateTime);

        //分页查询
        Page<Comment> page=new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVoList=toCommentVoList(page.getRecords());

        //查询所有对应根评论的子评论，并且赋值给对应的属性
        for(CommentVo commentVo:commentVoList){
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo);
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    /**
     * 添加评论
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能未空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);//创建时间，创建人...会通过mybatis-plus的@TableField自动添加
        return ResponseResult.okResult();
    }


    /**
     * CommentVo中的username和toCommentUserName赋值
     */
    private List<CommentVo> toCommentVoList(List<Comment> commentList){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        //遍历vo集合
        List<CommentVo> list = commentVos.stream()
                .map(new Function<CommentVo, CommentVo>() {
                    @Override
                    public CommentVo apply(CommentVo commentVo) {
                        //通过creatBy查询用户的昵称并赋值
                        String userName = userService.getById(commentVo.getCreateBy()).getNickName();
                        commentVo.setUsername(userName);

                        //通过toCommentUserId查询用户的昵称并赋值
                        //如果toCommentUserId不为-1才进行查询
                        if (!commentVo.getToCommentUserId().equals(SystemConstants.COMMENT_ROOT)) {
                            String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                            commentVo.setToCommentUserName(toCommentUserName);
                        }
                        return commentVo;
                    }
                }).collect(Collectors.toList());
        return list;
    }

    /**
     * 根据根评论的id查询对应的子评论的集合
     * @param commentVo
     * @return
     */
    private List<CommentVo> getChildren(CommentVo commentVo){
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getToCommentId,commentVo.getId())
                .orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(wrapper);
         return toCommentVoList(comments);
    }
}
