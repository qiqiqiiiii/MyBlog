package org.njupt.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加博文dto")
public class ArticleDto {
    @ApiModelProperty(notes = "标题")
    private String title;

    @ApiModelProperty(notes = "缩略图")
    private String thumbnail;

    @ApiModelProperty(notes = "是否置顶 0否，1是")
    private String isTop;

    @ApiModelProperty(notes = "是否允许评论 1是，0否")
    private String isComment;

    @ApiModelProperty(notes = "文章内容")
    private String content;

    @ApiModelProperty(notes = "文章所属标签")
    private List<Long> tags;

    @ApiModelProperty(notes = "文章所属分类")
    private Long categoryId;

    @ApiModelProperty(notes = "文章摘要")
    private String summary;

    @ApiModelProperty(notes = "状态 0已发布，1草稿")
    private String status;

}
