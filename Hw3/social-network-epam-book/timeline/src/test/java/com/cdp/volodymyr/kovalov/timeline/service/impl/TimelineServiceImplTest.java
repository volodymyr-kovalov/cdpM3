package com.cdp.volodymyr.kovalov.timeline.service.impl;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cdp.volodymyr.kovalov.timeline.converter.TimelineDtoConverter;
import com.cdp.volodymyr.kovalov.timeline.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.timeline.entity.Timeline;
import com.cdp.volodymyr.kovalov.timeline.repository.TimelineRepository;
import com.cdp.volodymyr.kovalov.timeline.service.FriendApiFaignClient;

@RunWith(MockitoJUnitRunner.class)
public class TimelineServiceImplTest {

    private static final String USERS_ARE_NOT_FRIENDS = "User with username %s is not friend of user with username %s";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private TimelineRepository repository;
    @Mock
    private FriendApiFaignClient friendApiFaignClient;
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

        when(repository.findByOwner(owner)).thenReturn(timelineList);
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

        when(repository.save(refEq(timeline))).thenReturn(new Timeline());

        timelineService.addNoteToTimelineByOwner(owner, timelineDto);

        verify(repository, times(1)).save(refEq(timeline));
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

        when(friendApiFaignClient.isFriend(username, friendUsername)).thenReturn(true);
        when(repository.findByOwner(friendUsername)).thenReturn(timelineList);
        when(converter.toTimelineDtoList(timelineList)).thenReturn(singletonList(expectedTimelineDto));

        TimelineDto actualTimelineDto = timelineService.getFriendTimelines(username, friendUsername).get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto, "id", "owner"));
    }

    @Test
    public void getFriendTimelinesShouldThrowIllegalStateException() {
        String friendUsername = "friendUsername";
        String username = "username";

        when(friendApiFaignClient.isFriend(username, friendUsername)).thenReturn(false);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username))));

        timelineService.getFriendTimelines(username, friendUsername);
    }

    @Test
    public void addNoteToFriendTimeline() {
        String userName = "userName";
        String friendUsername = "friendUsername";
        String noteText = "noteText";
        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setAuthor(userName);
        timelineDto.setNoteText(noteText);

        Timeline timeline = new Timeline();
        timeline.setOwner(friendUsername);
        timeline.setAuthor(userName);
        timeline.setNoteText(noteText);

        when(friendApiFaignClient.isFriend(userName, friendUsername)).thenReturn(true);
        when(repository.save(refEq(timeline))).thenReturn(new Timeline());

        timelineService.addNoteToFriendTimeline(userName, friendUsername, timelineDto);

        verify(repository, times(1)).save(refEq(timeline));
    }

    @Test
    public void addNoteToFriendTimelineShouldThrowIllegalStateException() {
        String username = "username";
        String friendUsername = "friendUsername";
        String noteText = "noteText";
        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setAuthor(username);
        timelineDto.setNoteText(noteText);

        when(friendApiFaignClient.isFriend(username, friendUsername)).thenReturn(false);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERS_ARE_NOT_FRIENDS, friendUsername, username))));

        timelineService.addNoteToFriendTimeline(username, friendUsername, timelineDto);
    }
}
