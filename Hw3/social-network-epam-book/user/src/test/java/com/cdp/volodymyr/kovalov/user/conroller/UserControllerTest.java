package com.cdp.volodymyr.kovalov.user.conroller;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    private MockMvc mockMvc;
    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void createUser() throws Exception {
        String username = "username";
        String name = "name";
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setName(name);

        doNothing().when(service).createUser(refEq(userDto));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JACKSON.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());

        verify(service).createUser(refEq(userDto));
    }

}