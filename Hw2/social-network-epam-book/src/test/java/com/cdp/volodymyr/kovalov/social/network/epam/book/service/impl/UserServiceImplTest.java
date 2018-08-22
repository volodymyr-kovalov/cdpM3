package com.cdp.volodymyr.kovalov.social.network.epam.book.service.impl;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cdp.volodymyr.kovalov.social.network.epam.book.converter.UserDtoConverter;
import com.cdp.volodymyr.kovalov.social.network.epam.book.dto.UserDto;
import com.cdp.volodymyr.kovalov.social.network.epam.book.security.UserRole;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.Role;
import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.RoleRepository;
import com.cdp.volodymyr.kovalov.social.network.epam.book.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME_IS_NOT_AVAILABLE_MESSAGE = "Username %s is not available";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private UserRepository repository;
    @Mock
    private UserDtoConverter converter;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl service;

    @Test
    public void getUserByUsername() {
        String username = "username";
        String name = "name";
        Date dateOfBirth = Date.valueOf(LocalDate.now());

        User user = new User();
        user.setDateOfBirth(dateOfBirth);
        user.setName(name);
        user.setUsername(username);

        UserDto expectedUserDto = new UserDto();
        copyProperties(user, expectedUserDto);
        expectedUserDto.setDateOfBirth(user.getDateOfBirth().toLocalDate());

        when(repository.findOne(username)).thenReturn(user);
        when(converter.toUserDto(user)).thenReturn(expectedUserDto);

        UserDto actualUserDto = service.getUserByUsername(username);

        assertTrue(reflectionEquals(actualUserDto, expectedUserDto));
    }

    @Test
    public void createUser() {
        String username = "username";
        String name = "name";
        Date dateOfBirth = Date.valueOf(LocalDate.now());

        User user = new User();
        user.setDateOfBirth(dateOfBirth);
        user.setName(name);
        user.setUsername(username);

        UserDto userDto = new UserDto();
        copyProperties(user, userDto);
        userDto.setDateOfBirth(user.getDateOfBirth().toLocalDate());

        Role role = new Role();
        role.setRoleName(UserRole.USER.toString());
        role.setRoleId(1);

        when(repository.isUsernameExist(username)).thenReturn(false);
        when(repository.save(refEq(user))).thenReturn(user);
        when(converter.toUser(userDto)).thenReturn(user);
        when(roleRepository.findByRoleName(UserRole.USER.toString())).thenReturn(role);

        service.createUser(userDto);

        verify(repository).isUsernameExist(username);
        verify(repository).save(refEq(user));
    }

    @Test
    public void createUserShouldThrowIllegalStateException() {
        String username = "username";
        String name = "name";
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setUsername(username);
        expectedUserDto.setName(name);

        when(repository.isUsernameExist(username)).thenReturn(true);

        thrown.expect(IllegalStateException.class);
        thrown.expect(hasProperty("message", is(String.format(USERNAME_IS_NOT_AVAILABLE_MESSAGE, username))));

        service.createUser(expectedUserDto);
    }
}