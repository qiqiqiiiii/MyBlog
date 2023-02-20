package org.njupt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.domain.entity.UserRole;
import org.njupt.mapper.UserRoleMapper;
import org.njupt.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
