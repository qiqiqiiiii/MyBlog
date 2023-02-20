package org.njupt.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 标签(Tag)实体类
 *
 * @author makejava
 * @since 2023-02-10 20:11:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_tag")
@ApiModel(description = "标签实体类")
public class Tag implements Serializable {
    private static final long serialVersionUID = -28409455015659031L;

    @TableId
    private Long id;
    /**
     * 标签名
     */
    private String name;
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
    /**
     * 备注
     */
    private String remark;


}

