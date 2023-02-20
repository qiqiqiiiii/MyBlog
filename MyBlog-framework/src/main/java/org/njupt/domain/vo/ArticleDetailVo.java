package org.njupt.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String thumbnail;
    private Long categoryId;
    @TableField(exist = false)
    private String categoryName;
    private Long viewCount;
    private String isComment;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;

}
