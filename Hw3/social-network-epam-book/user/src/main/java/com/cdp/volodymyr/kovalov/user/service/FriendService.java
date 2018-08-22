package com.cdp.volodymyr.kovalov.user.service;

import java.util.List;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;


public interface FriendService {

    void addFriend(String username, UserDto userDto);

    List<UserDto> getFriends(String username);

    Boolean isFriend(String username, String friendUsername);
}
