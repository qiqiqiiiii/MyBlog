package org.njupt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddUserDto;
import org.njupt.domain.dto.UpdateUserAdminDto;
import org.njupt.domain.dto.UserListDto;
import org.njupt.domain.entity.Role;
import org.njupt.domain.entity.User;
import org.njupt.domain.entity.UserRole;
import org.njupt.domain.vo.*;
import org.njupt.enums.AppHttpCodeEnum;
import org.njupt.exception.SystemException;
import org.njupt.mapper.UserMapper;
import org.njupt.service.RoleService;
import org.njupt.service.UserRoleService;
import org.njupt.service.UserService;
import org.njupt.utils.BeanCopyUtils;
import org.njupt.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        //获取用户当前id
        Long userId = SecurityUtils.getUserId();
        //根据用id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //这里也可以使用UserInfoVo
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }

        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICK_NAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码进行加密
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);

        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userListDto.getUserName()), User::getUserName, userListDto.getUserName())
                .eq(StringUtils.hasText(userListDto.getPhonenumber()), User::getPhonenumber, userListDto.getPhonenumber())
                .eq(StringUtils.hasText(userListDto.getStatus()), User::getStatus, userListDto.getStatus());
        page(page, queryWrapper);
        List<User> users = page.getRecords();
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(users, UserListVo.class);
        return ResponseResult.okResult(new PageVo(userListVos, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult addUser(AddUserDto addUserDto) {
        //判断是否为空
        if (!StringUtils.hasText(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        //判断是否已经存在
        if (userNameExist(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (phoneNumberExist(addUserDto.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (emailExist(addUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);

        //对密码进行加密
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);

        //保存user_role表
        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(aLong -> new UserRole(user.getId(), aLong))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        if (SecurityUtils.getUserId().equals(id)) {
            throw new SystemException(AppHttpCodeEnum.DELETE_CURRENT_USER);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getDetailById(Long id) {
        //先将用户信息查出来
        User user = getById(id);
        UserDetailVo userDetailVo = BeanCopyUtils.copyBean(user, UserDetailVo.class);
        //查询用户所关联的角色id列表
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        //查询所有的角色列表
        List<Role> roles = roleService.list();

        UpdateUserVo updateUserVo = new UpdateUserVo(roleIds, roles, userDetailVo);
        return ResponseResult.okResult(updateUserVo);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UpdateUserAdminDto updateUserAdminDto) {
        //先将User保存
        User user = BeanCopyUtils.copyBean(updateUserAdminDto, User.class);
        updateById(user);
        //将user_role表中的数据更新
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(queryWrapper);
        List<Long> roleIds = updateUserAdminDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(aLong -> new UserRole(updateUserAdminDto.getId(), aLong))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }


    /**
     * UserName重复判断
     */
    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        int count = count(queryWrapper);
        return count > 0;
    }

    /**
     * NickName重复判断
     */
    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        int count = count(queryWrapper);
        return count > 0;
    }

    /**
     * NickName重复判断
     */
    private boolean phoneNumberExist(String phoneNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phoneNumber);
        int count = count(queryWrapper);
        return count > 0;
    }

    /**
     * email重复判断
     */
    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        int count = count(queryWrapper);
        return count > 0;
    }

}
