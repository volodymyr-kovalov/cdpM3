package com.cdp.volodymyr.kovalov.social.network.epam.book.security;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.TimelineDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Role;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Timeline;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.TimelineRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class SecurityIntegrationTest {

    private static final ObjectMapper JACKSON = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Rollback
    @WithMockUser(authorities = "ROLE_USER")
    public void getTimelinesByOwner() throws Exception {
        String owner = "owner";
        String author = "author";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(author);

        timelineRepository.deleteAll();

        Timeline timeline = new Timeline();
        timeline.setOwner(owner);
        timeline.setAuthor(author);
        timeline.setNoteText(noteText);
        timelineRepository.save(timeline);

        MvcResult mvcResult = mockMvc.perform(get("/users/{owner}/timelines", owner))
                .andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    @Rollback
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void getTimelinesByOwnerWithAdminRole() throws Exception {
        String owner = "owner";
        String author = "author";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(author);

        timelineRepository.deleteAll();

        Timeline timeline = new Timeline();
        timeline.setOwner(owner);
        timeline.setAuthor(author);
        timeline.setNoteText(noteText);
        timelineRepository.save(timeline);

        MvcResult mvcResult = mockMvc.perform(get("/users/{owner}/timelines", owner))
                .andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    public void getTimelinesByOwnerWithoutAuthentication() throws Exception {
        String owner = "owner";

        mockMvc.perform(get("/users/{owner}/timelines", owner))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @WithMockUser(authorities = "ROLE_USER")
    public void addNoteToTimelineByOwnerWithUserRole() throws Exception {
        String owner = "owner";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        timelineRepository.deleteAll();

        mockMvc.perform(post("/users/{owner}/timelines", owner)
                .content(JACKSON.writeValueAsBytes(timelineDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        Timeline actualTimeline = timelineRepository.findByOwner(owner).get(0);

        assertThat(actualTimeline.getAuthor(), is(owner));
        assertThat(actualTimeline.getNoteText(), is(noteText));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void addNoteToTimelineByOwnerWithAdminRole() throws Exception {
        String owner = "owner";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        mockMvc.perform(post("/users/{owner}/timelines", owner)
                .content(JACKSON.writeValueAsBytes(timelineDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addNoteToTimelineByOwnerWithoutAuthentication() throws Exception {
        String owner = "owner";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        mockMvc.perform(post("/users/{owner}/timelines", owner)
                .content(JACKSON.writeValueAsBytes(timelineDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_USER")
    public void getFriendTimelinesWithUserRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(username);

        timelineRepository.deleteAll();

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        Timeline timeline = new Timeline();
        timeline.setOwner(friendUsername);
        timeline.setAuthor(username);
        timeline.setNoteText(noteText);
        timelineRepository.save(timeline);

        MvcResult mvcResult = mockMvc.perform(
                get("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername))
                .andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void getFriendTimelinesWithAdminRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String noteText = "noteText";

        TimelineDto expectedTimelineDto = new TimelineDto();
        expectedTimelineDto.setNoteText(noteText);
        expectedTimelineDto.setAuthor(username);

        timelineRepository.deleteAll();

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        Timeline timeline = new Timeline();
        timeline.setOwner(friendUsername);
        timeline.setAuthor(username);
        timeline.setNoteText(noteText);
        timelineRepository.save(timeline);

        MvcResult mvcResult = mockMvc.perform(
                get("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername))
                .andExpect(status().isOk()).andReturn();

        List<TimelineDto> actualTimelineDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<TimelineDto>>() {
                });
        TimelineDto actualTimelineDto = actualTimelineDtoList.get(0);

        assertTrue(reflectionEquals(actualTimelineDto, expectedTimelineDto));
    }

    @Test
    public void getFriendTimelinesWithoutAuthentication() throws Exception {
        String username = "user";
        String friendUsername = "admin";

        mockMvc.perform(
                get("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_USER")
    public void addNoteToFriendTimelineWithUserRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        timelineRepository.deleteAll();

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        mockMvc.perform(
                post("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername)
                        .content(JACKSON.writeValueAsBytes(timelineDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        Timeline actualTimeline = timelineRepository.findByOwner(friendUsername).get(0);

        assertThat(actualTimeline.getAuthor(), is(username));
        assertThat(actualTimeline.getNoteText(), is(noteText));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void addNoteToFriendTimelineWithAdminRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        mockMvc.perform(
                post("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername)
                        .content(JACKSON.writeValueAsBytes(timelineDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addNoteToFriendTimelineWithoutAuthenticate() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String noteText = "noteText";

        TimelineDto timelineDto = new TimelineDto();
        timelineDto.setNoteText(noteText);

        mockMvc.perform(
                post("/users/{" + username + "}/friends/{" + friendUsername + "}/timelines", username, friendUsername)
                        .content(JACKSON.writeValueAsBytes(timelineDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_USER")
    public void getFriendsWithUserRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String name = "name";

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(friendUsername);
        expectedUserDto.setName(name);

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        MvcResult mvcResult = mockMvc.perform(get("/users/{" + username + "}/friends", username))
                .andExpect(status().isOk()).andReturn();

        List<UserDto> actualUserDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<UserDto>>() {
                });
        UserDto actualUserDto = actualUserDtoList.get(0);

        assertTrue(reflectionEquals(actualUserDto, expectedUserDto, "password", "dateOfBirth"));
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void getFriendsWithAdminRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String name = "name";

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(friendUsername);
        expectedUserDto.setName(name);

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        MvcResult mvcResult = mockMvc.perform(get("/users/{" + username + "}/friends", username))
                .andExpect(status().isOk()).andReturn();

        List<UserDto> actualUserDtoList = JACKSON.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<UserDto>>() {
                });
        UserDto actualUserDto = actualUserDtoList.get(0);

        assertTrue(reflectionEquals(actualUserDto, expectedUserDto, "password", "dateOfBirth"));
    }

    @Test
    public void getFriendsWithoutAuthenticate() throws Exception {
        String username = "user";

        mockMvc.perform(get("/users/{" + username + "}/friends", username))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_USER")
    public void addFriendWithUserRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String name = "name";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(friendUsername);
        expectedUserDto.setName(name);

        User user = userRepository.findOne(username);
        User friendUser = userRepository.findOne(friendUsername);

        assertFalse(user.getFriends().contains(friendUser));

        mockMvc.perform(post("/users/{" + username + "}/friends", username)
                .content(JACKSON.writeValueAsBytes(userDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());

        user = userRepository.findOne(username);
        friendUser = userRepository.findOne(friendUsername);

        assertTrue(user.getFriends().contains(friendUser));
    }

    @Test
    @Rollback
    @Transactional
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void addFriendWithAdminRole() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String name = "name";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        mockMvc.perform(post("/users/{" + username + "}/friends", username)
                .content(JACKSON.writeValueAsBytes(userDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    @Rollback
    @Transactional
    public void addFriendWithoutAuthentication() throws Exception {
        String username = "user";
        String friendUsername = "admin";
        String name = "name";
        UserDto userDto = new UserDto();
        userDto.setUsername(friendUsername);
        userDto.setName(name);

        mockMvc.perform(post("/users/{" + username + "}/friends", username)
                .content(JACKSON.writeValueAsBytes(userDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isFound());
    }

    @Test
    @Rollback
    @Transactional
    public void createUserWithoutAuthentication() throws Exception {
        String username = "username";
        String name = "name";
        String password = "password";
        String dateOfBirth = "2000-11-11";

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setName(name);
        userDto.setPassword(password);
        userDto.setDateOfBirth(LocalDate.parse(dateOfBirth));

        assertNull(userRepository.findOne(username));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JACKSON.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());

        User user = userRepository.findOne(username);

        Role expectedRole = new Role();
        expectedRole.setRoleId(1);
        expectedRole.setRoleName(UserRole.USER.toString());

        assertThat(user.getUsername(), is(username));
        assertThat(user.getName(), is(name));
        assertThat(user.getDateOfBirth().toString(), is(dateOfBirth));
        assertThat(user.getRole().size(), is(1));
        assertTrue(user.getRole().contains(expectedRole));
    }
}