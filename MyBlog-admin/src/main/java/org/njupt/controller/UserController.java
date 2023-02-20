package org.njupt.controller;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.dto.AddUserDto;
import org.njupt.domain.dto.UpdateUserAdminDto;
import org.njupt.domain.dto.UserListDto;
import org.njupt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return userService.getUserList(pageNum,pageSize,userListDto);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserDetail(@PathVariable Long id){
        return userService.getDetailById(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserAdminDto updateUserAdminDto){
        return userService.updateUser(updateUserAdminDto);
    }


}
