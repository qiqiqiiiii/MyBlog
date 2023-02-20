package org.njupt.service;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getInfo();

    ResponseResult getRouters();
}
