package org.njupt.controller;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.Menu;
import org.njupt.domain.vo.MenuTreeVo;
import org.njupt.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult listMenu(String status,String menuName){
        return menuService.listMenu(status,menuName);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult getDetailById(@PathVariable Long id){
        return menuService.getDetail(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        return menuService.deleteMenu(menuId);
    }

    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        List<MenuTreeVo> menuTreeVos = menuService.treeSelect();
        return ResponseResult.okResult(menuTreeVos);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTree(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }
}
