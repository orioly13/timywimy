package com.timywimy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class TimeFormatUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_ZONE_PATTERN = "yyyy-MM-dd HH:mm ZZ";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_ZONE_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_ZONE_PATTERN);

    private TimeFormatUtil() {
    }
//    public static <T extends Comparable<? super T>> boolean isBetween(T value, T start, T end) {
//        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
//    }

    //toString
    public static String toString(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMATTER);
    }

    public static String toString(LocalTime time) {
        return time == null ? "" : time.format(TIME_FORMATTER);
    }

    public static String toString(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_TIME_FORMATTER);
    }

    public static String toString(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? "" : zonedDateTime.format(DATE_TIME_ZONE_FORMATTER);
    }

    //parse
    public static LocalDate parseLocalDate(String str) {
        return StringUtil.isEmpty(str) ? null : LocalDate.parse(str, DATE_FORMATTER);
    }

    public static LocalTime parseLocalTime(String str) {
        return StringUtil.isEmpty(str) ? null : LocalTime.parse(str, TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String str) {
        return StringUtil.isEmpty(str) ? null : LocalDateTime.parse(str, DATE_TIME_FORMATTER);
    }

    public static ZonedDateTime parseZonedDateTime(String str) {
        return StringUtil.isEmpty(str) ? null : ZonedDateTime.parse(str, DATE_TIME_ZONE_FORMATTER);
    }

}
