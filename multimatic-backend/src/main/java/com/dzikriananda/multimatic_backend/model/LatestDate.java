package com.dzikriananda.multimatic_backend.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Getter
@Setter
public class LatestDate {
    private String date;

    public LatestDate(Timestamp date) {
        LocalDateTime localDateTime = date.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
        String formattedDate = localDateTime.format(formatter);
        this.date = formattedDate;
    }
}
