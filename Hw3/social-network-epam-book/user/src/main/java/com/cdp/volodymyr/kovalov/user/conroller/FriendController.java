package com.cdp.volodymyr.kovalov.user.conroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cdp.volodymyr.kovalov.user.dto.UserDto;
import com.cdp.volodymyr.kovalov.user.service.FriendService;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping(path = "/users/{username}/friends")
    public List<UserDto> getFriends(@PathVariable String username) {
        return friendService.getFriends(username);
    }

    @PostMapping(path = "/users/{username}/friends")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable String username, @RequestBody UserDto userDto) {
        friendService.addFriend(username, userDto);
    }

    @GetMapping("/is-friend")
    public Boolean isFriend(@RequestParam String username, @RequestParam String friendUsername) {
        return friendService.isFriend(username, friendUsername);
    }
}
