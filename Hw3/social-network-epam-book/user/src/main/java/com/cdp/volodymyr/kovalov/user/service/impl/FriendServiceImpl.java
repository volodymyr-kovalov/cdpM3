package com.cdp.volodymyr.kovalov.user.service.impl;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdp.volodymyr.kovalov.user.converter.UserDtoConverter;
import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.entity.User;
import com.cdp.volodymyr.kovalov.user.repository.UserRepository;
import com.cdp.volodymyr.kovalov.user.service.FriendService;

@Transactional(readOnly = true)
@Service
public class FriendServiceImpl implements FriendService {

    private static final String USER_DOES_NOT_EXIST = "User with username %s does not exist";
    private static final String USERS_ARE_FRIENDS = "Users %s and %s are friends";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoConverter userDtoConverter;

    @Override
    @Transactional
    public void addFriend(String username, UserDto userDto) {
        User user = userRepository.findOne(username);
        validateUserExisting(user, username);

        String friendUsername = userDto.getUsername();
        User friendUser = userRepository.findOne(friendUsername);
        validateUserExisting(friendUser, friendUsername);

        Set<User> userFriends = user.getFriends();

        if (userFriends.contains(friendUser)) {
            throw new IllegalStateException(String.format(USERS_ARE_FRIENDS, username, friendUsername));
        }

        userFriends.add(friendUser);
        friendUser.getFriends().add(user);
    }

    @Override
    public List<UserDto> getFriends(String username) {
        Set<User> friendUserNameSet = userRepository.findOne(username).getFriends();
        if (!friendUserNameSet.isEmpty()) {
            return userDtoConverter.toUserDtoList(friendUserNameSet);
        }
        return emptyList();
    }

    @Override
    public Boolean isFriend(String username, String friendUsername) {
        User user = userRepository.findOne(username);
        validateUserExisting(user, username);

        User friendUser = userRepository.findOne(friendUsername);
        validateUserExisting(friendUser, friendUsername);
        return user.getFriends().contains(friendUser);
    }

    private void validateUserExisting(User user, String username) {
        if (Objects.isNull(user)) {
            throw new IllegalStateException(String.format(USER_DOES_NOT_EXIST, username));
        }
    }
}