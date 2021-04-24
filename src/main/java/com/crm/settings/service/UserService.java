package com.crm.settings.service;

import com.crm.settings.domain.User;
import com.crm.exception.LoginException;

import java.util.List;

public interface UserService {
    User login(String loginName, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
