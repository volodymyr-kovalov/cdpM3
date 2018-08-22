package com.cdp.volodymyr.kovalov.social.network.epam.book.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdp.volodymyr.kovalov.social.network.epam.book.converter.UserDtoConverter;
import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.security.UserRole;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Role;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.RoleRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.UserRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.UserService;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAME_IS_NOT_AVAILABLE_MESSAGE = "Username %s is not available";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoConverter converter;
    @Autowired
    private RoleRepository roleRepository;

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
            User user = converter.toUser(userDto);
            setDefaultRole(user);
            userRepository.save(user);
        }
    }

    private void setDefaultRole(User user) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.findByRoleName(UserRole.USER.toString()));
        user.setRole(roleSet);
    }
}