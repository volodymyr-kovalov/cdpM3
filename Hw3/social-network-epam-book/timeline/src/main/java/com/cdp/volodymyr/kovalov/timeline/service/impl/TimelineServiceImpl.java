package com.cdp.volodymyr.kovalov.timeline.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdp.volodymyr.kovalov.timeline.converter.TimelineDtoConverter;
import com.cdp.volodymyr.kovalov.timeline.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.timeline.entity.Timeline;
import com.cdp.volodymyr.kovalov.timeline.repository.TimelineRepository;
import com.cdp.volodymyr.kovalov.timeline.service.FriendApiFaignClient;
import com.cdp.volodymyr.kovalov.timeline.service.TimelineService;

@Transactional(readOnly = true)
@Service
public class TimelineServiceImpl implements TimelineService {

    private static final String USERS_ARE_NOT_FRIENDS = "User with username %s is not friend of user with username %s";

    @Autowired
    private TimelineRepository repository;
    @Autowired
    private TimelineDtoConverter converter;
    @Autowired
    private FriendApiFaignClient friendApiFaignClient;

    @Override
    public List<TimelineDto> getTimelinesByOwner(String owner) {
        return converter.toTimelineDtoList(repository.findByOwner(owner));
    }

    @Override
    @Transactional
    public void addNoteToTimelineByOwner(String owner, TimelineDto timelineDto) {
        repository.save(getTimeline(owner, owner, timelineDto));
    }

    @Override
    public List<TimelineDto> getFriendTimelines(String username, String friendUsername) {
        if (friendApiFaignClient.isFriend(username, friendUsername)) {
            return getTimelinesByOwner(friendUsername);
        } else {
            throw new IllegalStateException(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username));
        }
    }

    @Override
    @Transactional
    public void addNoteToFriendTimeline(String username, String friendUsername, TimelineDto timelineDto) {
        if (friendApiFaignClient.isFriend(username, friendUsername)) {
            repository.save(getTimeline(friendUsername, username, timelineDto));
        } else {
            throw new IllegalStateException(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username));
        }
    }

    private Timeline getTimeline(String owner, String author, TimelineDto timelineDto) {
        Timeline timeline = new Timeline();
        timeline.setOwner(owner);
        timeline.setAuthor(author);
        timeline.setNoteText(timelineDto.getNoteText());
        return timeline;
    }
}