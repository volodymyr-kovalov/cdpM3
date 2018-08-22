package com.cdp.volodymyr.kovalov.social.network.epam.book.conroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.FriendService;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping(path = "/users/{username}/friends")
    public List<UserDto> getFriends(@PathVariable String username) {
        return friendService.getFriends(username);
    }

    @Secured("ROLE_USER")
    @PostMapping(path = "/users/{username}/friends")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable String username, @RequestBody UserDto userDto) {
        friendService.addFriend(username, userDto);
    }
}
