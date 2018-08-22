package com.cdp.volodymyr.kovalov.timeline.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user")
public interface FriendApiFaignClient {

    @GetMapping("/is-friend")
    Boolean isFriend(@RequestParam("username") String username, @RequestParam("friendUsername") String friendUsername);
}
