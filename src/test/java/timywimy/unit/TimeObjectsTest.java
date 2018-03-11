package timywimy.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timywimy.model.common.converters.DurationConverter;
import timywimy.model.common.converters.ZoneIdConverter;
import timywimy.model.common.util.DateTimeZone;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@RunWith(BlockJUnit4ClassRunner.class)
public class TimeObjectsTest {

    private static final Logger log = LoggerFactory.getLogger(TimeObjectsTest.class);

    @Test
    public void dateTimeZone() {
        //now
//        System.out.println("now");
        DateTimeZone dtz0 = new DateTimeZone(LocalDate.of(2018, 3, 10),
                LocalTime.MIDNIGHT.plusHours(12), ZoneId.of("Europe/Helsinki"));
        log.info("{}, zoned {}", dtz0, dtz0.getZonedDateTime());
        //now copy
        DateTimeZone dtz1 = new DateTimeZone(dtz0.getDate(), dtz0.getTime(), dtz0.getZone());
        log.info("{}, zoned {}", dtz1, dtz1.getZonedDateTime());
        //no zone
        DateTimeZone dtz2 = new DateTimeZone(dtz0.getDate(), dtz0.getTime(), null);
        log.info("{}, zoned {}", dtz2, dtz2.getZonedDateTime());
        //zo time
        DateTimeZone dtz3 = new DateTimeZone(dtz0.getDate(), null, dtz0.getZone());
        log.info("{}, zoned {}", dtz3, dtz3.getZonedDateTime());
        //no time zone
        DateTimeZone dtz4 = new DateTimeZone(dtz0.getDate(), null, null);
        log.info("{}, zoned {}", dtz4, dtz4.getZonedDateTime());
        //empty DTZ
        DateTimeZone dtz5 = new DateTimeZone(null, null, null);
        log.info("{}, zoned {}", dtz5, dtz5.getZonedDateTime());

        Assert.assertTrue(dtz0.equals(dtz1));
        Assert.assertTrue(dtz0.compareTo(dtz1) == 0);
        Assert.assertTrue(dtz0.isBefore(dtz2));
        Assert.assertTrue(dtz0.compareTo(dtz2) < 0);
        Assert.assertTrue(dtz0.isAfter(dtz3));
        Assert.assertTrue(dtz0.compareTo(dtz3) > 0);
        Assert.assertTrue(dtz0.isAfter(dtz4));
        Assert.assertTrue(dtz0.compareTo(dtz4) > 0);
        Assert.assertTrue(dtz0.isBefore(dtz5));
        Assert.assertTrue(dtz0.compareTo(dtz5) < 0);

    }

    @Test
    public void zoneIdConverter() {
        ZoneIdConverter converter = new ZoneIdConverter();
        String zone1 = converter.convertToDatabaseColumn(ZoneId.of("Europe/Samara"));
        log.info("{}", zone1);
        Assert.assertEquals(zone1, "Europe/Samara");
        String zone2 = converter.convertToDatabaseColumn(ZoneId.of("UTC"));
        log.info("{}", zone2);
        Assert.assertEquals(zone2, "UTC");
        String zone3 = converter.convertToDatabaseColumn(ZoneId.of("+0"));
        log.info("{}", zone3);
        Assert.assertEquals(zone3, "Z");

        Assert.assertEquals(converter.convertToEntityAttribute("Europe/Samara"), ZoneId.of("Europe/Samara"));
        Assert.assertEquals(converter.convertToEntityAttribute("UTC"), ZoneId.of("UTC"));
        Assert.assertEquals(converter.convertToEntityAttribute("Z"), ZoneId.of("+0"));

    }

    @Test
    public void durationConverter() {
        DurationConverter converter = new DurationConverter();
        Duration days3 = Duration.ofDays(3);
        Assert.assertEquals(converter.convertToDatabaseColumn(days3), "3d0h0m0s");
        Duration hour3 = Duration.ofHours(3);
        Assert.assertEquals(converter.convertToDatabaseColumn(hour3), "0d3h0m0s");
        Duration minutes3 = Duration.ofMinutes(3);
        Assert.assertEquals(converter.convertToDatabaseColumn(minutes3), "0d0h3m0s");
        Duration seconds3 = Duration.ofSeconds(3);
        Assert.assertEquals(converter.convertToDatabaseColumn(seconds3), "0d0h0m3s");

        Duration d3h4m5s6 = Duration.ofDays(3);
        d3h4m5s6 = d3h4m5s6.plusHours(4);
        d3h4m5s6 = d3h4m5s6.plusMinutes(5);
        d3h4m5s6 = d3h4m5s6.plusSeconds(6);
        Assert.assertEquals(converter.convertToDatabaseColumn(d3h4m5s6), "3d4h5m6s");

        Assert.assertEquals(converter.convertToEntityAttribute("3d4h5m6s"), d3h4m5s6);
        Assert.assertEquals(converter.convertToEntityAttribute("3d0h0m0s"), days3);
        Assert.assertEquals(converter.convertToEntityAttribute("0d3h0m0s"), hour3);
        Assert.assertEquals(converter.convertToEntityAttribute("0d0h3m0s"), minutes3);
        Assert.assertEquals(converter.convertToEntityAttribute("0d0h0m3s"), seconds3);
        //todo test above 21 days and other

    }
}
