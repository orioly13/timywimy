package timywimy.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;


public class TimeFormatUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private TimeFormatUtil() {
    }

    public static String toString(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.getDisplayName(TextStyle.NARROW, Locale.ENGLISH);
    }

    public static String toString(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMATTER);
    }

    public static LocalDate parseLocalDate(String str) {
        return StringUtil.isEmpty(str) ? null : LocalDate.parse(str, DATE_FORMATTER);
    }

    public static String toString(LocalTime time) {
        return time == null ? null : time.format(TIME_FORMATTER);
    }

    public static LocalTime parseLocalTime(String str) {
        return StringUtil.isEmpty(str) ? null : LocalTime.parse(str, TIME_FORMATTER);
    }

    public static String toString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String str) {
        return StringUtil.isEmpty(str) ? null : LocalDateTime.parse(str, DATE_TIME_FORMATTER);
    }

    public static String toString(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ZonedDateTime parseZonedDateTime(String str) {
        return StringUtil.isEmpty(str) ? null : ZonedDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
    }

}
