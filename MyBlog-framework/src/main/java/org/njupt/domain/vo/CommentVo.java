package org.njupt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long articleId;
    private List<CommentVo> children;
    private String content;
    private Long createBy;
    private Date createTime;
    private Long id;
    private Long rootId;
    private Long toCommentId;
    private Long toCommentUserId;
    /**
     * 被评论者的nickName
     */
    private String toCommentUserName;
    /**
     * 评论者的nickName
     */
    private String username;

}
