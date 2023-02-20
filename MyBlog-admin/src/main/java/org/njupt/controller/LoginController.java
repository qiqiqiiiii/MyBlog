package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.LoginUserDto;
import org.njupt.domain.entity.User;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.exception.SystemException;
import org.njupt.service.LoginService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "用户登录/退出接口",description = "后台用户登录/退出接口")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    @ApiOperation(value = "后台用户登录",notes = "实现后台用户登录")
    public ResponseResult login(@RequestBody LoginUserDto loginUserDto){
        if(!StringUtils.hasText(loginUserDto.getUserName())){
            //提示必须要传用户名
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        User user = BeanCopyUtils.copyBean(loginUserDto, User.class);
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    @ApiOperation(value = "用户退出",notes = "实现后台用户退出")
    public ResponseResult logout(){
        return loginService.logout();
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        return loginService.getInfo();
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        return loginService.getRouters();
    }
}
