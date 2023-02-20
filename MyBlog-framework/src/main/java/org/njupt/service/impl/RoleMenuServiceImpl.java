package org.njupt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.domain.entity.RoleMenu;
import org.njupt.mapper.RoleMenuMapper;
import org.njupt.service.RoleMenuService;
import org.springframework.stereotype.Service;

@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
