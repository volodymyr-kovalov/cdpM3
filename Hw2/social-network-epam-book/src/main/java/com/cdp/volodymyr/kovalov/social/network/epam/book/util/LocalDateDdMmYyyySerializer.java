package com.cdp.volodymyr.kovalov.social.network.epam.book.util;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateDdMmYyyySerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void serialize(LocalDate date, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        if (date == null) {
            jsonGenerator.writeString(StringUtils.EMPTY);
            return;
        }
        jsonGenerator.writeString(date.format(FORMATTER));
    }
}
