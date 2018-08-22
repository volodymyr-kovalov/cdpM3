package com.cdp.volodymyr.kovalov.timeline.service;

import java.util.List;

import com.cdp.volodymyr.kovalov.timeline.dto.TimelineDto;

public interface TimelineService {

    List<TimelineDto> getTimelinesByOwner(String owner);

    void addNoteToTimelineByOwner(String owner, TimelineDto userDto);

    List<TimelineDto> getFriendTimelines(String username, String friendUsername);

    void addNoteToFriendTimeline(String username, String friendUsername, TimelineDto timelineDto);
}
