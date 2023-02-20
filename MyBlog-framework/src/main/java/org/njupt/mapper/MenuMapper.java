package org.njupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.njupt.domain.entity.Menu;
import org.njupt.domain.vo.MenuVo;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu>{
    List<String> selectPermsByUserId(Long userId);
    List<Menu> selectRoutersAll();
    List<Menu> selectRouters(Long userId);

    //自己的写法
    List<Menu> selectRoutersByUserId(@Param("userId") Long userId, @Param("parentId") Long parentId);

    List<Menu> getMenusByRoleId(Long id);
}
