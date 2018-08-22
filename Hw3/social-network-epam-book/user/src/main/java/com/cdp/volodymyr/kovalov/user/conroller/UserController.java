package com.cdp.volodymyr.kovalov.user.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public UserDto getUserByUsername(@RequestParam String username) {
        return service.getUserByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody UserDto userDto) {
        service.createUser(userDto);
    }
}
