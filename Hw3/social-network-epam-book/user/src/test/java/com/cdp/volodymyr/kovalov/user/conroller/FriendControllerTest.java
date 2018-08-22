package com.cdp.volodymyr.kovalov.user.conroller;

import static org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.service.FriendService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class FriendControllerTest {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private FriendService friendService;
    @InjectMocks
    private FriendController controller;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getFriends() throws Exception {
        String username = "username";
        String name = "name";
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(username);
        expectedUserDto.setName(name);

        when(friendService.getFriends(username)).thenReturn(Collections.singletonList(expectedUserDto));

        MvcResult mvcResult = mockMvc.perform(get("/users/{username}/friends", username))
                .andExpect(status().isOk()).andReturn();

        List<UserDto> actualUserDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<UserDto>>() {
                });
        UserDto actualUserDto = actualUserDtoList.get(0);

        assertTrue(reflectionEquals(actualUserDto, expectedUserDto));
    }

    @Test
    public void addFriend() throws Exception {
        String username = "username";
        String friendUsername = "friendUsername";
        String name = "name";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        doNothing().when(friendService).addFriend(eq(username), refEq(userDto));

        mockMvc.perform(post("/users/{username}/friends", username)
                .content(JACKSON.writeValueAsBytes(userDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        verify(friendService).addFriend(eq(username), refEq(userDto));
    }


    @Test
    public void isFriend() throws Exception {
        String username = "username";
        String friendUsername = "friendUsername";

        when(friendService.isFriend(username, friendUsername)).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(get("/is-friend")
                .param("username", username)
                .param("friendUsername", friendUsername))
                .andExpect(status().isOk()).andReturn();

        Boolean result = Boolean.valueOf(mvcResult.getResponse().getContentAsString());

        verify(friendService).isFriend(username, friendUsername);

        assertTrue(result);
    }
}