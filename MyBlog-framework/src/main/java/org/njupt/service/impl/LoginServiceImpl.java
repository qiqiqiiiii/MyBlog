package org.njupt.service.impl;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.LoginUser;
import org.njupt.domain.entity.User;
import org.njupt.domain.vo.AdminUserInfoVo;
import org.njupt.domain.vo.MenuVo;
import org.njupt.domain.vo.UserInfoVo;
import org.njupt.service.LoginService;
import org.njupt.service.MenuService;
import org.njupt.service.RoleService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.JwtUtil;
import org.njupt.utils.RedisCache;
import org.njupt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public ResponseResult login(User user) {
        //判断是否通过认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //把用户信息存入Redis
        redisCache.setCacheObject("login:"+userId,loginUser);

        //封装后返回
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo() {
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();
        //获取用户信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //根据用户id查询角色信息
        List<String> roles = roleService.queryRoleKeyByUserId(user.getId());
        //根据用户Id查询权限信息
        List<String> permissions = menuService.queryPermsByUserId(user.getId());
        return ResponseResult.okResult(new AdminUserInfoVo(permissions,roles,userInfoVo));
    }

    @Override
    public ResponseResult getRouters() {
        //获取当前登录的用户
        Long userId = SecurityUtils.getUserId();
        List<MenuVo> menuVos = menuService.queryRoutersByUserId(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("menus",menuVos);
        return ResponseResult.okResult(map);
    }
}
