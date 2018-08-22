package com.cdp.volodymyr.kovalov.timeline.conroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cdp.volodymyr.kovalov.timeline.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.timeline.service.TimelineService;

@RestController
@RequestMapping("/users")
public class TimelineController {

    @Autowired
    private TimelineService service;

    @GetMapping(path = "/{owner}/timelines")
    public List<TimelineDto> getTimelinesByOwner(@PathVariable String owner) {
        return service.getTimelinesByOwner(owner);
    }

    @PostMapping(path = "/{owner}/timelines")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addNoteToTimelineByOwner(@PathVariable String owner, @RequestBody TimelineDto timelineDto) {
        service.addNoteToTimelineByOwner(owner, timelineDto);
    }

    @GetMapping(path = "/{username}/friends/{friendUsername}/timelines")
    public List<TimelineDto> getFriendTimelines(@PathVariable String username, @PathVariable String friendUsername) {
        return service.getFriendTimelines(username, friendUsername);
    }

    @PostMapping(path = "/{username}/friends/{friendUsername}/timelines")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addNoteToFriendTimeline(@PathVariable String username, @PathVariable String friendUsername,
                                        @RequestBody TimelineDto timelineDto) {
        service.addNoteToFriendTimeline(username, friendUsername, timelineDto);
    }
}
