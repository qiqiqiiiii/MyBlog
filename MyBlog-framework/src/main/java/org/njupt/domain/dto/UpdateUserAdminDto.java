package org.njupt.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserAdminDto {
    private String email;
    private Long id;
    private String nickName;
    private String sex;
    private String status;
    private String userName;
    private List<Long> roleIds;
}
