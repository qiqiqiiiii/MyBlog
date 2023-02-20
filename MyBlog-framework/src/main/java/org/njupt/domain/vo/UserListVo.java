package org.njupt.domain.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListVo {
    private String avatar;
    private Date createTime;
    private String email;
    private Long id;
    private String nickName;
    private String phonenumber;
    private String sex;
    private String status;
    private Long updateBy;
    private Date updateTime;
    private String userName;
}
