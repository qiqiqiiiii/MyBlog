package org.njupt.service.impl;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.LoginUser;
import org.njupt.domain.entity.User;
import org.njupt.service.BlogLoginService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.JwtUtil;
import org.njupt.utils.RedisCache;
import org.njupt.domain.vo.BlogUserLoginVo;
import org.njupt.domain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("blogLoginService")
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
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
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);

        //把token和userinfo封装返回
        //把User转换成userinfo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);

    }

    @Override
    public ResponseResult logout() {
        //解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser)authentication.getPrincipal();
        //获取UserId
        String userId = loginUser.getUser().getId().toString();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
