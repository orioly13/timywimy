package timywimy.web.dto.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import timywimy.web.util.converters.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@JsonPropertyOrder(value = {"date", "time", "zone"})
public class DateTimeZone {
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime time;
    @JsonSerialize(using = ZoneIdSerializer.class)
    @JsonDeserialize(using = ZoneIdDeserializer.class)
    private ZoneId zone;

    public DateTimeZone(){}

    public DateTimeZone(LocalDate date, LocalTime time, ZoneId zone) {
        this.date = date;
        this.time = time;
        this.zone = zone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ZoneId getZone() {
        return zone;
    }

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }
}