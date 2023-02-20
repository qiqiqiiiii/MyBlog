package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.njupt.annotation.SystemLog;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.RegisterUserDto;
import org.njupt.domain.dto.UpdateUserDto;
import org.njupt.domain.entity.User;
import org.njupt.service.UserService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户",description = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation(value="用户信息",notes="查看用户信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName="更新用户信息")
    @ApiOperation(value="更新用户信息",notes="实现用户更新")
    public ResponseResult updateUserInfo(@RequestBody UpdateUserDto updateUserDto){
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @SystemLog(businessName = "用户注册")
    @ApiOperation(value="用户注册",notes="实现用户注册")
    public ResponseResult register(@RequestBody RegisterUserDto registerUserDto){
        User user = BeanCopyUtils.copyBean(registerUserDto, User.class);
        return userService.register(user);
    }
}
