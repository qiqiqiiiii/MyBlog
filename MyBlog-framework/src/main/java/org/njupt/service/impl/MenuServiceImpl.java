package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.Menu;
import org.njupt.domain.vo.MenuDetailVo;
import org.njupt.domain.vo.MenuListVo;
import org.njupt.domain.vo.MenuTreeVo;
import org.njupt.domain.vo.MenuVo;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.mapper.MenuMapper;
import org.njupt.service.MenuService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> queryPermsByUserId(Long userId) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPE_CATALOGUE, SystemConstants.MENU_TYPE_BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            List<String> perms = list.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        MenuMapper menuMapper = getBaseMapper();//可以通过getBaseMapper()来获取menuMapper;
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        return perms;
    }

    @Override
    public List<MenuVo> queryRoutersByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        //首先将所有符合的MenuVo都找出来
        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            menus = menuMapper.selectRoutersAll();
        } else {
            //否则，获取当前用户的所有的Menu
            menus = menuMapper.selectRouters(userId);
        }
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        List<MenuVo> menuVoList = builderMenuVoTree(menuVos, SystemConstants.MENU_PARENT);
        return menuVoList;

    }

    private List<MenuVo> builderMenuVoTree(List<MenuVo> menuVos, Long parentId) {
        List<MenuVo> list = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menuVos)))
                .collect(Collectors.toList());
        return list;
    }

    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
        List<MenuVo> children = menuVos.stream()
                .filter(m -> m.getParentId().equals(menuVo.getId()))
                .map(m -> m.setChildren(getChildren(m, menuVos)))
                .collect(Collectors.toList());
        return children;
    }

    /*
    //自己的写法
    @Override
    public List<MenuVo> queryRoutersByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus=null;
        //判断是否是管理员
        //如果是，则返回所有符合要求的Menu
        if(SecurityUtils.isAdmin()){
            //找到所有父
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getParentId,SystemConstants.MENU_PARENT)
                    .eq(Menu::getStatus,SystemConstants.MENU_STATUS_NORMAL)
                    .in(Menu::getMenuType,SystemConstants.MENU_TYPE_MENU,SystemConstants.MENU_TYPE_CATALOGUE);
            menus = list(wrapper);
        }else{
            //找到所有父
            menus = menuMapper.selectRoutersByUserId(userId, SystemConstants.MENU_PARENT);
        }
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);

        for(MenuVo menuVo:menuVos) {
            //找所有子
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL)
                    .in(Menu::getMenuType, SystemConstants.MENU_TYPE_MENU, SystemConstants.MENU_TYPE_CATALOGUE)
                    .eq(Menu::getParentId,menuVo.getId());
            List<Menu> list = list(wrapper);
            List<MenuVo> children = BeanCopyUtils.copyBeanList(list, MenuVo.class);
            menuVo.setChildren(children);
        }
        return menuVos;
    }*/

    @Override
    public ResponseResult listMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        wrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menuList = list(wrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(menuList, MenuListVo.class);
        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDetail(Long id) {
        Menu menu = getById(id);
        MenuDetailVo menuDetailVo = BeanCopyUtils.copyBean(menu, MenuDetailVo.class);
        return ResponseResult.okResult(menuDetailVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //判断是不是父菜单
        if (menu.getParentId().equals(SystemConstants.MENU_PARENT)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        //判断是否还有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId);
        List<Menu> list = list(wrapper);
        if (list.size() != 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "存在子菜单不允许删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }

    @Override
    public List<MenuTreeVo> treeSelect() {
        List<Menu> menus = null;
        //首先查所有的
/*
        if (!Objects.isNull(id) && !id.equals(SystemConstants.ROLE_SUPER_ADMIN)) {
            MenuMapper menuMapper = getBaseMapper();
            menus = menuMapper.getMenusByRoleId(id);
        } else {*/

            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
            menus = list(wrapper);
        //}

        List<MenuTreeVo> menuTreeVos = menus.stream()
                .map(menu -> new MenuTreeVo(null, menu.getId(), menu.getMenuName(), menu.getParentId()))
                .collect(Collectors.toList());
        List<MenuTreeVo> treeVos = builderTree(menuTreeVos, SystemConstants.MENU_PARENT);
        return treeVos;
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        List<MenuTreeVo> menuTreeVos = treeSelect();

        List<Menu> menus=null;
        if (!id.equals(SystemConstants.ROLE_SUPER_ADMIN)){
            MenuMapper menuMapper = getBaseMapper();
            menus = menuMapper.getMenusByRoleId(id);
        }else{
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
            menus = list(wrapper);
        }
        List<Long> ids = menus.stream().map(menu -> menu.getId()).collect(Collectors.toList());
        Map<String,Object> map=new HashMap<>();
        map.put("menus",menuTreeVos);
        map.put("checkedKeys",ids);
        return ResponseResult.okResult(map);
    }

    private List<MenuTreeVo> builderTree(List<MenuTreeVo> menuTreeVos, Long parentId) {
        List<MenuTreeVo> list = menuTreeVos.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(parentId))
                .map(menuTreeVo -> menuTreeVo.setChildren(getTreeChildren(menuTreeVo, menuTreeVos)))
                .collect(Collectors.toList());
        return list;
    }

    private List<MenuTreeVo> getTreeChildren(MenuTreeVo menuTreeVo, List<MenuTreeVo> menuTreeVos) {
        List<MenuTreeVo> children = menuTreeVos.stream()
                .filter(m -> m.getParentId().equals(menuTreeVo.getId()))
                .map(m -> m.setChildren(getTreeChildren(m, menuTreeVos)))
                .collect(Collectors.toList());
        return children;
    }
}
