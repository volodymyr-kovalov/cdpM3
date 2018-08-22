package com.cdp.volodymyr.kovalov.user.service;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;

public interface UserService {

    UserDto getUserByUsername(String email);

    void createUser(UserDto userDto);
}
