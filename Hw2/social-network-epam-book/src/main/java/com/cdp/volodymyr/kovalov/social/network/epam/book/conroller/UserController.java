package com.cdp.volodymyr.kovalov.social.network.epam.book.conroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createUser(@RequestBody UserDto userDto) {
        service.createUser(userDto);
    }
}
