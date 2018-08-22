package com.cdp.volodymyr.kovalov.social.network.epam.book.service.impl;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cdp.volodymyr.kovalov.social.network.epam.book.converter.TimelineDtoConverter;
import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Timeline;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.TimelineRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.UserRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.FriendService;

@RunWith(MockitoJUnitRunner.class)
public class TimelineServiceImplTest {

    private static final String USERS_ARE_NOT_FRIENDS = "User with username %s is not friend of user with username %s";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private TimelineRepository timelineRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendService friendService;
    @Mock
    private TimelineDtoConverter converter;
    @InjectMocks
    private TimelineServiceImpl timelineService;

    @Test
    public void getTimelinesByOwner() {
        Long id = 1L;
        String author = "author";
        String owner = "owner";
        String noteText = "noteText";

        Timeline timeline = new Timeline();
        timeline.setId(id);
        timeline.setAuthor(author);
        timeline.setOwner(owner);
        timeline.setNoteText(noteText);

        List<Timeline> timelineList = singletonList(timeline);

        TimelineDto expectedTimelineDto = new TimelineDto();
        copyProperties(timeline, expectedTimelineDto);

        when(timelineRepository.findByOwner(owner)).thenReturn(timelineList);
        when(converter.toTimelineDtoList(timelineList)).thenReturn(singletonList(expectedTimelineDto));

        TimelineDto actualTimelineDto = timelineService.getTimelinesByOwner(owner).get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto, "id", "owner"));
    }

    @Test
    public void addNoteToTimelineByOwner() {
        String author = "author";
        String owner = "owner";
        String noteText = "noteText";
        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setAuthor(author);
        timelineDto.setNoteText(noteText);

        Timeline timeline = new Timeline();
        timeline.setOwner(owner);
        timeline.setAuthor(owner);
        timeline.setNoteText(noteText);

        when(timelineRepository.save(refEq(timeline))).thenReturn(new Timeline());

        timelineService.addNoteToTimelineByOwner(owner, timelineDto);

        verify(timelineRepository, times(1)).save(refEq(timeline));
    }

    @Test
    public void getFriendTimelines() {
        Long id = 1L;
        String author = "author";
        String friendUsername = "friendUsername";
        String noteText = "noteText";

        String username = "username";

        Timeline timeline = new Timeline();
        timeline.setId(id);
        timeline.setAuthor(author);
        timeline.setOwner(friendUsername);
        timeline.setNoteText(noteText);

        List<Timeline> timelineList = singletonList(timeline);

        TimelineDto expectedTimelineDto = new TimelineDto();
        copyProperties(timeline, expectedTimelineDto);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        Set<User> userSet = new HashSet<>();
        userSet.add(friendUser);
        user.setFriends(userSet);

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        when(timelineRepository.findByOwner(friendUsername)).thenReturn(timelineList);
        when(converter.toTimelineDtoList(timelineList)).thenReturn(singletonList(expectedTimelineDto));

        TimelineDto actualTimelineDto = timelineService.getFriendTimelines(username, friendUsername).get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto, "id", "owner"));
    }

    @Test
    public void getFriendTimelinesShouldThrowIllegalStateException() {
        String friendUsername = "friendUsername";
        String username = "username";

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        user.setFriends(new HashSet<>());

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username))));

        timelineService.getFriendTimelines(username, friendUsername);
    }

    @Test
    public void addNoteToFriendTimeline() {
        String username = "userName";
        String friendUsername = "friendUsername";
        String noteText = "noteText";
        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setAuthor(username);
        timelineDto.setNoteText(noteText);

        Timeline timeline = new Timeline();
        timeline.setOwner(friendUsername);
        timeline.setAuthor(username);
        timeline.setNoteText(noteText);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);

        Set<User> userSet = new HashSet<>();
        userSet.add(friendUser);
        user.setFriends(userSet);

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        when(timelineRepository.save(refEq(timeline))).thenReturn(new Timeline());

        timelineService.addNoteToFriendTimeline(username, friendUsername, timelineDto);

        verify(timelineRepository, times(1)).save(refEq(timeline));
    }

    @Test
    public void addNoteToFriendTimelineShouldThrowIllegalStateException() {
        String username = "username";
        String friendUsername = "friendUsername";
        String noteText = "noteText";
        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setAuthor(username);
        timelineDto.setNoteText(noteText);

        User user = new User();
        user.setUsername(username);

        User friendUser = new User();
        friendUser.setUsername(friendUsername);
        user.setFriends(new HashSet<>());

        when(userRepository.findOne(username)).thenReturn(user);
        when(userRepository.findOne(friendUsername)).thenReturn(friendUser);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username))));

        timelineService.addNoteToFriendTimeline(username, friendUsername, timelineDto);
    }
}