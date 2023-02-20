package org.njupt.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "更新用户信息dto")
public class UpdateUserDto {
    @ApiModelProperty(notes = "头像")
    private String avatar;

    @ApiModelProperty(notes = "邮箱地址")
    private String email;

    @ApiModelProperty(notes = "用户Id")
    private Long id;

    @ApiModelProperty(notes = "用户昵称")
    private String nickName;

    @ApiModelProperty(notes = "用户性别")
    private String sex;

}
