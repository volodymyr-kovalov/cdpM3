package com.cdp.volodymyr.kovalov.social.network.epam.book.service;

import java.util.List;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;

public interface FriendService {

    void addFriend(String username, UserDto userDto);

    List<UserDto> getFriends(String username);
}
