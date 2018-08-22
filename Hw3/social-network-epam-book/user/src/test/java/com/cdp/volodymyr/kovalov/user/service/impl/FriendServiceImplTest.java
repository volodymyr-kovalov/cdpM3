package com.cdp.volodymyr.kovalov.user.service.impl;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cdp.volodymyr.kovalov.user.converter.UserDtoConverter;
import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.entity.User;
import com.cdp.volodymyr.kovalov.user.repository.UserRepository;
import com.cdp.volodymyr.kovalov.user.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class FriendServiceImplTest {

    private static final String USER_DOES_NOT_EXIST = "User with username %s does not exist";
    private static final String USERS_ARE_FRIENDS = "Users %s and %s are friends";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDtoConverter userDtoConverter;
    @InjectMocks
    private FriendServiceImpl friendService;

    @Test
    public void addFriend() {
        String username = "username";
        String name = "name";
        String friendUsername = "friendUsername";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        user.setFriends(new HashSet<>());
        friendUser.setFriends(new HashSet<>());

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        friendService.addFriend(username, userDto);

        verify(userRepository).findOne(username);
        verify(userRepository).findOne(friendUsername);
    }

    @Test
    public void addFriendWhenUserDoesNotExist() {
        String username = "username";
        String name = "name";
        String friendUsername = "friendUsername";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        when(userRepository.findOne(username)).thenReturn(null);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USER_DOES_NOT_EXIST, username))));

        friendService.addFriend(username, userDto);
    }

    @Test
    public void addFriendWhenAlreadyFriends() {
        String username = "username";
        String name = "name";
        String friendUsername = "friendUsername";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        Set<User> userSet = new HashSet<>();
        userSet.add(friendUser);
        user.setFriends(userSet);

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERS_ARE_FRIENDS, username, friendUsername))));

        friendService.addFriend(username, userDto);
    }

    @Test
    public void getFriends() {
        String username = "username";
        String friendUsername = "friendUsername";
        String name = "name";

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(friendUsername);
        expectedUserDto.setName(name);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        Set<User> userSet = new HashSet<>();
        userSet.add(friendUser);
        user.setFriends(userSet);

        when(userRepository.findOne(username)).thenReturn(user);
        when(userDtoConverter.toUserDtoList(userSet)).thenReturn(singletonList(expectedUserDto));

        UserDto actualUserDto = friendService.getFriends(username).get(0);

        assertTrue(reflectionEquals(actualUserDto, expectedUserDto));
    }
}