package org.njupt.domain.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.njupt.domain.entity.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MenuVo {
    private List<MenuVo> children=new ArrayList<>();
    private String component;
    private Date createTime;
    private String icon;
    private Long id;
    private String menuName;
    private String menuType;
    private Integer orderNum;
    private Long parentId;
    private String path;
    private String perms;
    private String status;
    private String visible;
}
