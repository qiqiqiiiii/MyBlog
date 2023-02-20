package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.Menu;
import org.njupt.domain.vo.MenuTreeVo;
import org.njupt.domain.vo.MenuVo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<String> queryPermsByUserId(Long userId);

    List<MenuVo> queryRoutersByUserId(Long userId);

    ResponseResult listMenu(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult getDetail(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    List<MenuTreeVo> treeSelect();

    ResponseResult roleMenuTreeSelect(Long id);
}
