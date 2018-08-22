package com.cdp.volodymyr.kovalov.social.network.epam.book.util;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDdMmYyyyDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        if (StringUtils.isEmpty(parser.getValueAsString())) {
            return null;
        }
        return LocalDate.parse(parser.getValueAsString(), FORMATTER);
    }
}
