package org.njupt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddUserDto;
import org.njupt.domain.dto.UpdateUserAdminDto;
import org.njupt.domain.dto.UserListDto;
import org.njupt.domain.entity.User;

public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUserById(Long id);

    ResponseResult getDetailById(Long id);

    ResponseResult updateUser(UpdateUserAdminDto updateUserAdminDto);
}
