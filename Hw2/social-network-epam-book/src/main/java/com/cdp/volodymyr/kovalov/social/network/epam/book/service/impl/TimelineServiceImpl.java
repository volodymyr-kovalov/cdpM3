package com.cdp.volodymyr.kovalov.social.network.epam.book.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdp.volodymyr.kovalov.social.network.epam.book.converter.TimelineDtoConverter;
import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Timeline;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.TimelineRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.UserRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.TimelineService;

@Transactional(readOnly = true)
@Service
public class TimelineServiceImpl implements TimelineService {

    private static final String USERS_ARE_NOT_FRIENDS = "User with username %s is not friend of user with username %s";
    private static final String USER_DOES_NOT_EXIST = "User with username %s does not exist";

    @Autowired
    private TimelineRepository repository;
    @Autowired
    private TimelineDtoConverter converter;
    @Autowired
    private UserRepository userRepository;

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
        User user = userRepository.findOne(username);
        validateUserExisting(user, username);

        User friendUser = userRepository.findOne(friendUsername);
        validateUserExisting(friendUser, friendUsername);

        if (user.getFriends().contains(friendUser)) {
            return getTimelinesByOwner(friendUsername);
        } else {
            throw new IllegalStateException(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username));
        }
    }

    @Override
    @Transactional
    public void addNoteToFriendTimeline(String username, String friendUsername, TimelineDto timelineDto) {
        User user = userRepository.findOne(username);
        validateUserExisting(user, username);

        User friendUser = userRepository.findOne(friendUsername);
        validateUserExisting(friendUser, friendUsername);

        if (user.getFriends().contains(friendUser)) {
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

    private void validateUserExisting(User user, String username) {
        if (Objects.isNull(user)) {
            throw new IllegalStateException(String.format(USER_DOES_NOT_EXIST, username));
        }
    }
}