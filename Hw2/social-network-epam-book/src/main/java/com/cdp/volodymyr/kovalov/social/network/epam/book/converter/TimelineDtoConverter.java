package com.cdp.volodymyr.kovalov.social.network.epam.book.converter;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Timeline;

@Component
public class TimelineDtoConverter {

    public TimelineDto toTimelineDto(Timeline timeline) {
        if (isNull(timeline)) {
            return null;
        }
        TimelineDto timelineDto = new TimelineDto();
        copyProperties(timeline, timelineDto);
        return timelineDto;
    }

    public List<TimelineDto> toTimelineDtoList(List<Timeline> timelineList) {
        if (timelineList.isEmpty()) {
            return emptyList();
        }
        return timelineList.stream().map(this::toTimelineDto).collect(Collectors.toList());
    }
}
