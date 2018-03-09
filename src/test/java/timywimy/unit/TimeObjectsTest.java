package timywimy.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import timywimy.model.common.converters.DurationConverter;
import timywimy.model.common.converters.ZoneIdConverter;
import timywimy.model.common.util.DateTimeZone;

import java.time.Duration;
import java.time.ZoneId;

@RunWith(BlockJUnit4ClassRunner.class)
public class TimeObjectsTest {

    @Test
    public void dateTimeZone() {
        //now
//        System.out.println("now");
        DateTimeZone dtz0 = DateTimeZone.now();
//        System.out.println(dtz0.toString());
        //now copy
        DateTimeZone dtz1 = new DateTimeZone(dtz0.getDate(), dtz0.getTime(), dtz0.getZone());
        //today no zone
//        System.out.println("no zone");
        DateTimeZone dtz2 = new DateTimeZone(dtz0.getDate(), dtz0.getTime(), null);
//        System.out.println(dtz2.toString());
//        System.out.println(dtz2.getZonedDateTime());
        //today no time
//        System.out.println("no time");
        DateTimeZone dtz3 = new DateTimeZone(dtz0.getDate(), null, dtz0.getZone());
//        System.out.println(dtz3.toString());
//        System.out.println(dtz3.getZonedDateTime());
        //today no zone and time
//        System.out.println("no time and zone");
        DateTimeZone dtz4 = new DateTimeZone(dtz0.getDate(), null, null);
//        System.out.println(dtz4.toString());
//        System.out.println(dtz4.getZonedDateTime());

        Assert.assertTrue(dtz0.equals(dtz1));
        Assert.assertTrue(dtz0.compareTo(dtz1) == 0);
        Assert.assertTrue(dtz0.isBefore(dtz2));
        Assert.assertTrue(dtz0.compareTo(dtz2) < 0);
        Assert.assertTrue(dtz0.isAfter(dtz3));
        Assert.assertTrue(dtz0.compareTo(dtz3) > 0);
        Assert.assertTrue(dtz0.isBefore(dtz4));
        Assert.assertTrue(dtz0.compareTo(dtz4) < 0);

    }

    @Test
    public void zoneIdConverter() {
        ZoneIdConverter converter = new ZoneIdConverter();
        Assert.assertEquals(converter.convertToDatabaseColumn(ZoneId.of("Europe/Samara")), "Europe/Samara");
        Assert.assertEquals(converter.convertToDatabaseColumn(ZoneId.of("UTC")), "UTC");
        Assert.assertEquals(converter.convertToDatabaseColumn(ZoneId.of("+0")), "Z");

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
