package com.cdp.volodymyr.kovalov.social.network.epam.book.dto;

import java.time.LocalDate;

import com.cdp.volodymyr.kovalov.social.network.epam.book.util.LocalDateDdMmYyyyDeserializer;
import com.cdp.volodymyr.kovalov.social.network.epam.book.util.LocalDateDdMmYyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDto {

    private String username;
    private String name;
    private String password;

    @JsonSerialize(using = LocalDateDdMmYyyySerializer.class)
    @JsonDeserialize(using = LocalDateDdMmYyyyDeserializer.class)
    private LocalDate dateOfBirth;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
