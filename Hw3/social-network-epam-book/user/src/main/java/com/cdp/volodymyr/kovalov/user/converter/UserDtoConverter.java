package com.cdp.volodymyr.kovalov.user.converter;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.entity.User;

@Component
public class UserDtoConverter {

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
        copyProperties(userDto, user);
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
