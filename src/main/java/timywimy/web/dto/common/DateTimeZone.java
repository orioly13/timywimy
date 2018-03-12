package timywimy.web.dto.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@JsonPropertyOrder(value = {"date", "time", "zone"})
public class DateTimeZone {

    private LocalDate date;
    private LocalTime time;
    private ZoneId zone;

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