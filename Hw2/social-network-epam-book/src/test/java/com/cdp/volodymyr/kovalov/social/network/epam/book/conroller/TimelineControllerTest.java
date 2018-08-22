package com.cdp.volodymyr.kovalov.social.network.epam.book.conroller;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.service.TimelineService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class TimelineControllerTest {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    private MockMvc mockMvc;
    @Mock
    private TimelineService service;
    @InjectMocks
    private TimelineController controller;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getTimelinesByOwner() throws Exception {
        String owner = "owner";
        String author = "author";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(author);

        when(service.getTimelinesByOwner(owner)).thenReturn(singletonList(expectedTimelineDto));

        MvcResult mvcResult = mockMvc.perform(get("/users/{owner}/timelines", owner))
                .andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    public void addNoteToTimelineByOwner() throws Exception {
        String owner = "owner";
        String author = "author";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);
        timelineDto.setAuthor(author);

        doNothing().when(service).addNoteToTimelineByOwner(eq(owner), refEq(timelineDto));

        mockMvc.perform(post("/users/{owner}/timelines", owner)
                .content(JACKSON.writeValueAsBytes(timelineDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        verify(service).addNoteToTimelineByOwner(eq(owner), refEq(timelineDto));
    }

    @Test
    public void getFriendTimelines() throws Exception {
        String username = "username";
        String friendUsername = "friendUsername";
        String author = "author";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(author);

        when(service.getFriendTimelines(username, friendUsername)).thenReturn(singletonList(expectedTimelineDto));

        MvcResult mvcResult = mockMvc.perform(get("/users//{username}/friends/{friendUsername}/timelines", username,
                friendUsername)).andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    public void addNoteToFriendTimeline() throws Exception {
        String username = "username";
        String friendUsername = "friendUsername";
        String author = "author";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);
        timelineDto.setAuthor(author);

        doNothing().when(service).addNoteToFriendTimeline(eq(username), eq(friendUsername), refEq(timelineDto));

        mockMvc.perform(post("/users/{username}/friends/{friendUsername}/timelines", username, friendUsername)
                .content(JACKSON.writeValueAsBytes(timelineDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        verify(service).addNoteToFriendTimeline(eq(username), eq(friendUsername), refEq(timelineDto));
    }
}