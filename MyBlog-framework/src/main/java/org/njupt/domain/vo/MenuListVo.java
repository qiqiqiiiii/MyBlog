package org.njupt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuListVo {
    private String component;
    private String icon;
    private Long id;
    private Integer isFrame;
    private String menuName;
    private String menuType;
    private Integer orderNum;
    private Long parentId;
    private String path;
    private String perms;
    private String remark;
    private String status;
    private String visible;
}
