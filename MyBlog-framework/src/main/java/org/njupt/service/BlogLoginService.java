package org.njupt.service;

import org.njupt.domain.ResponseResult;
import org.njupt.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
