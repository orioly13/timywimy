package timywimy.model.common.converters;

import timywimy.util.StringUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Converter
public class DurationConverter implements AttributeConverter<Duration, String> {
    private static final Pattern pattern = Pattern.compile("([0-9]{1,2})d([0-9]{1,2})h([0-9]{1,2})m([0-9]{1,2})s");
    private static final int MAX_DAYS = 21;

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return "0d0h0m0s";
        }
        StringBuilder sb = new StringBuilder("");

        long secondsFull = duration.getSeconds();
        long days = secondsFull / (24 * 60 * 60);
        long hours = (secondsFull - (days * 24 * 60 * 60)) / (60 * 60);
        long minutes = (secondsFull - (days * 24 * 60 * 60) - (hours * 60 * 60)) / 60;
        long seconds = (secondsFull - (days * 24 * 60 * 60) - (hours * 60 * 60)) - minutes * 60;
        sb.append(days).append("d").
                append(hours).append("h").
                append(minutes).append("m").
                append(seconds).append("s");
        return sb.toString();
    }

    @Override
    public Duration convertToEntityAttribute(String dbName) {
        if (StringUtil.isEmpty(dbName)) {
            return null;
        }
        Matcher matcher = pattern.matcher(dbName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("string does not match pattern");
        }
        int days = Integer.parseInt(matcher.group(1));
        int hours = Integer.parseInt(matcher.group(2));
        int minutes = Integer.parseInt(matcher.group(3));
        int seconds = Integer.parseInt(matcher.group(4));
        if (days > MAX_DAYS) {
            throw new IllegalArgumentException("there can't be more than 7 days");
        }
        if (hours > 23) {
            throw new IllegalArgumentException("there can't be more than 23 hours");
        }
        if (minutes > 59) {
            throw new IllegalArgumentException("there can't be more than 59 minutes");
        }
        if (seconds > 59) {
            throw new IllegalArgumentException("there can't be more than 59 seconds");
        }
        return Duration.ofSeconds(days * 24 * 60 * 60 + hours * 60 * 60 + minutes * 60 + seconds);
    }

}
