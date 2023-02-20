package org.njupt.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加评论dto")
public class AddCommentDto {
    @ApiModelProperty(notes ="文章Id")
    private Long articleId;

    @ApiModelProperty(notes = "评论类型（0代表文章评论，1代表友链评论）")
    private String type;

    @ApiModelProperty(notes = "根评论Id,-1表示没有根评论")
    private Long rootId;

    @ApiModelProperty(notes = "回复目标评论Id")
    private Long toCommentId;

    @ApiModelProperty("所回复的目标评论的userId")
    private Long toCommentUserId;

    @ApiModelProperty(notes = "评论内容")
    private String content;
}
