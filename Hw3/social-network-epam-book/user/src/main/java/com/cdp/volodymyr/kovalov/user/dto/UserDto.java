package com.cdp.volodymyr.kovalov.user.dto;

import java.time.LocalDate;

import com.cdp.volodymyr.kovalov.user.util.LocalDateDdMmYyyyDeserializer;
import com.cdp.volodymyr.kovalov.user.util.LocalDateDdMmYyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserDto {

    private String username;
    private String name;

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
}
