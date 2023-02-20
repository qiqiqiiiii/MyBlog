package org.njupt.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 评论表(Comment)实体类
 *
 * @author makejava
 * @since 2023-02-07 18:45:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_comment")
@ApiModel(description = "添加评论实体")
public class Comment implements Serializable {
    private static final long serialVersionUID = 451388064362058185L;

    @TableId
    private Long id;
    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    private String type;
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 根评论id
     */
    private Long rootId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 所回复的目标评论的userid
     */
    private Long toCommentUserId;
    /**
     * 回复目标评论id
     */
    private Long toCommentId;
    @TableField(fill= FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @TableLogic
    private Integer delFlag;


}

