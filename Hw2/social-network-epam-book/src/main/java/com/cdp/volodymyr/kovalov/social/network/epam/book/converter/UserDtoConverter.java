package com.cdp.volodymyr.kovalov.social.network.epam.book.converter;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;

@Component
public class UserDtoConverter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto toUserDto(User user) {
        if (isNull(user)) {
            return null;
        }
        UserDto userDto = new UserDto();
        copyProperties(user, userDto);
        userDto.setDateOfBirth(user.getDateOfBirth().toLocalDate());
        return userDto;
    }

    public User toUser(UserDto userDto) {
        if (isNull(userDto)) {
            return null;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setDateOfBirth(Date.valueOf(userDto.getDateOfBirth()));
        return user;
    }

    public List<UserDto> toUserDtoList(Set<User> userSet) {
        if (userSet.isEmpty()) {
            return emptyList();
        }
        return userSet.stream().map(this::toUserDto).collect(Collectors.toList());
    }
}
