package timywimy.unit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timywimy.util.CronEntity;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(BlockJUnit4ClassRunner.class)
public class CronEntityTest {

    private static final Logger log = LoggerFactory.getLogger(CronEntityTest.class);

    @Test
    public void cronEntityCreate() {
        CronEntity cronEntity0 = new CronEntity("* * * * *");
        CronEntity cronEntity1 = new CronEntity("0 0 1 1 1");
        CronEntity cronEntity2 = new CronEntity("0,3,4,7,7 3,2,1 6,4,5 7,12 1,7");
    }

    @Test
    public void validateCreate() {
        CronEntity cronEntity0 = new CronEntity("* * * * *");
        cronEntity0.validateLocalDateTime(LocalDateTime.of(2000, 12, 12, 12, 12));
        CronEntity cronEntity1 = new CronEntity("0 0 1 1 1");
        CronEntity cronEntity2 = new CronEntity("0,3,4,7,7 3,2,1 6,4,5 7,12 1,7");
        cronEntity2.validateLocalDateTime(LocalDateTime.of(2000, 7, 4, 3, 4));
    }

    @Test
    public void createFromCron() {
        CronEntity cronEntity0 = new CronEntity("30 19 1,2,3,4,27,28,29,30,31 2,3 1,2,3,5,6,7");
        List<LocalDateTime> localDateTimes = cronEntity0.nextLocalDateTimeList(
                LocalDateTime.of(2018, 2, 27, 20, 0),
                25, 10);
        Assert.assertEquals(4, localDateTimes.size());

        CronEntity cronEntity1 = new CronEntity("30 19 27 2 1,2,3,4,5,6,7");
        List<LocalDateTime> localDateTimes1 = cronEntity1.nextLocalDateTimeList(
                LocalDateTime.of(2018, 2, 27, 20, 0),
                25, 10);
        Assert.assertEquals(0, localDateTimes1.size());
    }
}
