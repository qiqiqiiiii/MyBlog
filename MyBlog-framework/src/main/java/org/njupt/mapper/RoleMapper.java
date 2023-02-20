package org.njupt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.njupt.domain.entity.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long userId);
}
