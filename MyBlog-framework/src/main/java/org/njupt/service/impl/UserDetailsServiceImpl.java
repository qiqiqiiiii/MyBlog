package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.njupt.constants.SystemConstants;
import org.njupt.domain.entity.User;
import org.njupt.mapper.MenuMapper;
import org.njupt.mapper.UserMapper;
import org.njupt.domain.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User user = userMapper.selectOne(queryWrapper);

        //判断是否查到用户，如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }

        //如果是后台用户才需要查询权限封装
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> permsList = menuMapper.selectPermsByUserId(user.getId());
            //根据用户查询权限信息，添加到LoginUser中
            return new LoginUser(user,permsList);
        }

        //返回用户信息
        return new LoginUser(user,null);
        
    }


}
