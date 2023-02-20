package org.njupt.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Strings;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.LoginUser;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.utils.JwtUtil;
import org.njupt.utils.RedisCache;
import org.njupt.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = httpServletRequest.getHeader("token");
        if(!Strings.hasText(token)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析获取userId
        Claims claims=null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时 token非法
            //filter这里不受SpringMVC管理，所有不能抛出异常，响应告诉前端需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //将result转换成json格式
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }
        //从Redis中获取用户信息
        String userId = claims.getSubject();
        LoginUser loginUser = (LoginUser) redisCache.getCacheObject("bloglogin:" + userId);
        if(Objects.isNull(loginUser)){
            //如果在Redis中获取不到，响应告诉前端要重新登录
            ResponseResult result=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse,JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
