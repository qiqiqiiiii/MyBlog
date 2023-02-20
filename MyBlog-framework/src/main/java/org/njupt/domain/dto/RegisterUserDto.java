package org.njupt.domain.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户注册dto")
public class RegisterUserDto {
    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "昵称")
    private String nickName;

    @ApiModelProperty(notes = "用户名")
    private String userName;

    @ApiModelProperty(notes = "密码")
    private String password;
}
