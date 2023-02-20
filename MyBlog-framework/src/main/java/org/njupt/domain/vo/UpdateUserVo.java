package org.njupt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.njupt.domain.entity.Role;
import org.njupt.domain.entity.User;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserDetailVo user;
}
