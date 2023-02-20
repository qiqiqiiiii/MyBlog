package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddRoleDto;
import org.njupt.domain.dto.RoleChangeStatusDto;
import org.njupt.domain.dto.UpdateRoleDto;
import org.njupt.domain.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> queryRoleKeyByUserId(Long userId);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(RoleChangeStatusDto roleChangeStatusDto);


    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getDetail(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();

}
