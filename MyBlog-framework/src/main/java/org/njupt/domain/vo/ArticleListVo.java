package org.njupt.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {
    private Long id;
    private String title;
    private String summary;
    private String categoryName;
    private String thumbnail;
    private String isTop;
    private Long viewCount;
    private Date createTime;

}
