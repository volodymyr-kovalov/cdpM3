package com.cdp.volodymyr.kovalov.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdp.volodymyr.kovalov.user.converter.UserDtoConverter;
import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.repository.UserRepository;
import com.cdp.volodymyr.kovalov.user.service.UserService;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAME_IS_NOT_AVAILABLE_MESSAGE = "Username %s is not available";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoConverter converter;

    @Override
    public UserDto getUserByUsername(String username) {
        return converter.toUserDto(userRepository.findOne(username));
    }

    @Override
    @Transactional
    public void createUser(UserDto userDto) {
        String username = userDto.getUsername();
        if (userRepository.isUsernameExist(username)) {
            throw new IllegalStateException(String.format(USERNAME_IS_NOT_AVAILABLE_MESSAGE, username));
        } else {
            userRepository.save(converter.toUser(userDto));
        }
    }
}