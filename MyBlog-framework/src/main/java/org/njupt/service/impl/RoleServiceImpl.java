package org.njupt.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddRoleDto;
import org.njupt.domain.dto.RoleChangeStatusDto;
import org.njupt.domain.dto.UpdateRoleDto;
import org.njupt.domain.entity.Role;
import org.njupt.domain.entity.RoleMenu;
import org.njupt.domain.vo.PageVo;
import org.njupt.domain.vo.RoleDetailVo;
import org.njupt.domain.vo.RoleListVo;
import org.njupt.mapper.RoleMapper;
import org.njupt.service.RoleMenuService;
import org.njupt.service.RoleService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> queryRoleKeyByUserId(Long userId) {
        //判断是否是管理员
        if(userId==1L){
            List<String> roles=new ArrayList<>();
            roles.add("admin");
            return roles;
        }
        //否则查询用户所具有的角色信息
        RoleMapper roleMapper = getBaseMapper();
        List<String> roles = roleMapper.selectRoleKeyByUserId(userId);
        return roles;
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        wrapper.orderByAsc(Role::getRoleSort);
        page(page,wrapper);
        List<Role> roles = page.getRecords();
        List<RoleListVo> roleListVos = BeanCopyUtils.copyBeanList(roles, RoleListVo.class);
        return ResponseResult.okResult(new PageVo(roleListVos,page.getTotal()));
    }


    @Override
    public ResponseResult changeStatus(RoleChangeStatusDto roleChangeStatusDto) {
        Role role = getById(roleChangeStatusDto.getRoleId());
        role.setStatus(roleChangeStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        List<Long> menuIds = addRoleDto.getMenuIds();
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(aLong -> new RoleMenu(role.getId(), aLong)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDetail(Long id) {
        Role role = getById(id);
        RoleDetailVo roleDetailVo = BeanCopyUtils.copyBean(role, RoleDetailVo.class);
        return ResponseResult.okResult(roleDetailVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新role表
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        //将role_menu表中原有的数据删除
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,role.getId());
        roleMenuService.remove(queryWrapper);

        List<Long> menuIds = updateRoleDto.getMenuIds();
        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(aLong -> new RoleMenu(role.getId(), aLong)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        /* //将role_menu表中原有的数据删除
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(queryWrapper);*/
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        List<Role> roles = list(queryWrapper);
        return ResponseResult.okResult(roles);
    }


}
