package org.njupt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.njupt.annotation.SystemLog;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.LoginUserDto;
import org.njupt.domain.entity.User;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.exception.SystemException;
import org.njupt.service.BlogLoginService;
import org.njupt.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "登录/退出",description = "登录/退出相关接口")
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName="用户登录")
    @ApiOperation(value="登录",notes="实现登录功能")
    public ResponseResult login(@RequestBody LoginUserDto loginUserDto){
        if(!StringUtils.hasText(loginUserDto.getUserName())){
            //提示：必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        User user = BeanCopyUtils.copyBean(loginUserDto, User.class);
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @SystemLog(businessName="用户退出")
    @ApiOperation(value="退出",notes="实现退出功能")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
