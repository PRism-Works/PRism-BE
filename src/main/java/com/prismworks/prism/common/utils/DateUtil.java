package com.prismworks.prism.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.Getter;

public class DateUtil {

    @Getter
    public enum DatePattern{
        DATE_TIME("yyyy-MM-dd HH:mm:ss"),
        DATE("yyyy-MM-dd"),
        TIME("HH:mm:ss");

        private final String pattern;

        private DatePattern(String pattern) {
            this.pattern = pattern;
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatDate(LocalDateTime localDateTime, DatePattern pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return localDateTime.format(dateTimeFormatter);
    }
}
