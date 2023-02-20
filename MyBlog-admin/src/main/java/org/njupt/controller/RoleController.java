package org.njupt.controller;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddRoleDto;
import org.njupt.domain.dto.RoleChangeStatusDto;
import org.njupt.domain.dto.UpdateRoleDto;
import org.njupt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.getRoleList(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleChangeStatusDto roleChangeStatusDto){
        return roleService.changeStatus(roleChangeStatusDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getDetail(@PathVariable Long id){
        return roleService.getDetail(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }

}
