package com.cdp.volodymyr.kovalov.social.network.epam.book.service;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;

public interface UserService {

    UserDto getUserByUsername(String email);

    void createUser(UserDto userDto);
}
