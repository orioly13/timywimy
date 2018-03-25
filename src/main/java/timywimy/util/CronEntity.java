package timywimy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CronEntity {
    //cuts off when searching for
    public final static Integer MAX_INSTANCES_COUNT = 100;
    //can only calculate a month in advance
    public final static Integer MAX_DAYS_COUNT = 31;

    private final static String CRON_PARTS_SEPARATOR = " ";
    private final static String SEPARATOR_WITHIN_ONE_PART = ",";
    private final static String ANY_CHAR = "*";

    private final static Integer MIN_MINUTES_OF_HOUR = 0;
    private final static Integer MAX_MINUTES_OF_HOUR = 59;
    private final static Integer MIN_HOURS_OF_DAY = 0;
    private final static Integer MAX_HOURS_OF_DAY = 23;
    private final static Integer MIN_DAYS_OF_MONTH = 1;
    private final static Integer MAX_DAYS_OF_MONTH = 31;
    private final static Integer MIN_MONTHS_OF_YEAR = 1;//JANUARY
    private final static Integer MAX_MONTHS_OF_YEAR = 12;//DECEMBER
    private final static Integer MIN_DAYS_OF_WEEK = 1;//MONDAY
    private final static Integer MAX_DAYS_OF_WEEK = 7;//SUNDAY

    private String cron;
    //    private ZoneId zone;
    private List<Integer> minutesOfHour;
    private List<Integer> hoursOfDay;
    private List<Integer> daysOfMonth;
    private List<Integer> monthsOfYear;
    private List<Integer> daysOfWeek;


    public static boolean assertCron(String cron) {
        String[] split = cron.split(CRON_PARTS_SEPARATOR);
        if (split.length != 5) {
            return false;
        }
        return assertUnits(split[0], MIN_MINUTES_OF_HOUR, MAX_MINUTES_OF_HOUR) &&
                assertUnits(split[1], MIN_HOURS_OF_DAY, MAX_HOURS_OF_DAY) &&
                assertUnits(split[2], MIN_DAYS_OF_MONTH, MAX_DAYS_OF_MONTH) &&
                assertUnits(split[3], MIN_MONTHS_OF_YEAR, MAX_MONTHS_OF_YEAR) &&
                assertUnits(split[4], MIN_DAYS_OF_WEEK, MAX_DAYS_OF_WEEK);
    }

    private static boolean assertUnits(String units, Integer min, Integer max) {
        String[] splitUnits = units.split(SEPARATOR_WITHIN_ONE_PART);
        if (splitUnits.length == 1 && splitUnits[0].equals(ANY_CHAR)) {
            return true;
        }
        for (String stringUnit : splitUnits) {
            Integer unit;
            try {
                unit = Integer.parseInt(stringUnit);
            } catch (Exception e) {
                return false;
            }
            if (unit < min || unit > max) {
                return false;
            }
        }
        return true;
    }

    //m  h  dm my dw
    //*  *  *  *  *
    public CronEntity(String cron) {
        String[] split = cron.split(CRON_PARTS_SEPARATOR);
        if (split.length != 5) {
            throw new IllegalArgumentException("Unable to parse cron entity from cron string");
        }
        //minutes splitter
        minutesOfHour = fillUnits(split[0], "minutes of hour", MIN_MINUTES_OF_HOUR, MAX_MINUTES_OF_HOUR);
        hoursOfDay = fillUnits(split[1], "hours of day", MIN_HOURS_OF_DAY, MAX_HOURS_OF_DAY);
        daysOfMonth = fillUnits(split[2], "days of month", MIN_DAYS_OF_MONTH, MAX_DAYS_OF_MONTH);
        monthsOfYear = fillUnits(split[3], "months of year", MIN_MONTHS_OF_YEAR, MAX_MONTHS_OF_YEAR);
        daysOfWeek = fillUnits(split[4], "days of week", MIN_DAYS_OF_WEEK, MAX_DAYS_OF_WEEK);

        this.cron = cron;
    }

    private List<Integer> fillUnits(String units, String unitName, Integer min, Integer max) {
        String[] splitUnits = units.split(SEPARATOR_WITHIN_ONE_PART);
        if (splitUnits.length == 1 && splitUnits[0].equals(ANY_CHAR)) {
            List<Integer> result = new ArrayList<>();
            for (int i = min; i <= max; i++) {
                result.add(i);
            }
            return result;
        }
        //add all numbers
        Set<Integer> setUnits = new HashSet<>();
        for (String stringUnit : splitUnits) {
            Integer unit = Integer.parseInt(stringUnit);
            if (unit < min || unit > max) {
                throw new IllegalArgumentException(unitName + " should be between" + min + "-" + max);
            }
            setUnits.add(unit);
        }
        //add to list and sort
        List<Integer> list = new ArrayList<>(setUnits);
        list.sort(Integer::compareTo);
        return list;
    }

    public boolean validateLocalDateTime(LocalDateTime localDateTime) {
        return (minutesOfHour == null || minutesOfHour.contains(localDateTime.getMinute())) &&
                (hoursOfDay == null || hoursOfDay.contains(localDateTime.getHour())) &&
                (daysOfMonth == null || daysOfMonth.contains(localDateTime.getDayOfMonth())) &&
                (monthsOfYear == null || monthsOfYear.contains(localDateTime.getMonthValue())) &&
                (daysOfWeek == null || daysOfWeek.contains(localDateTime.getDayOfWeek().getValue()));
    }

    public boolean validateLocalDate(LocalDate localDate) {
        return (daysOfMonth == null || daysOfMonth.contains(localDate.getDayOfMonth())) &&
                (monthsOfYear == null || monthsOfYear.contains(localDate.getMonthValue())) &&
                (daysOfWeek == null || daysOfWeek.contains(localDate.getDayOfWeek().getValue()));
    }

    public List<LocalDateTime> nextLocalDateTimeList(LocalDateTime localDateTime, Integer days, Integer count) {
        days = days == null ? MAX_DAYS_COUNT : days;
        count = count == null ? MAX_INSTANCES_COUNT : count;
        if (days > MAX_DAYS_COUNT) {
            throw new IllegalArgumentException("max days is " + MAX_DAYS_COUNT);
        }
        if (days <= 0) {
            throw new IllegalArgumentException("days must not be negative");
        }
        if (count > MAX_INSTANCES_COUNT) {
            throw new IllegalArgumentException("max amount of dates is " + MAX_INSTANCES_COUNT);
        }
        if (count <= 0) {
            throw new IllegalArgumentException("dates amount must be positive");
        }
        List<LocalDateTime> possibleDateTimes = new ArrayList<>();
        //add all days that pass date pass
        for (int i = 0; i <= days; i++) {
            LocalDateTime next = localDateTime.plusDays(i);
            if (validateLocalDate(next.toLocalDate())) {
                possibleDateTimes.add(next);
            }
        }
        //find the closest possible date-time to the one given (may be the one given)
        LocalDateTime startDateTime;
        int startDateIndex;
        if (validateLocalDateTime(localDateTime)) {
            startDateTime = localDateTime;
            startDateIndex = 0;
        } else {
            Pair<Integer, LocalDateTime> pair = findStartDateTime(possibleDateTimes, localDateTime);
            startDateTime = pair.getSecond();
            startDateIndex = pair.getFirst();
        }
        //event can't occur in next DAYS
        if (startDateTime == null) {
            return new ArrayList<>();
        }
        //create events
        List<LocalDateTime> result = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        indexes.add(getIndex(minutesOfHour, startDateTime.getMinute()));
        indexes.add(getIndex(hoursOfDay, startDateTime.getHour()));
        indexes.add(startDateIndex);
        boolean done = false;
        //check minutes and hours
//        if (indexes.get(0) == minutesOfHour.size()) {
//            indexes.set(0, 0);
//            indexes.set(1, indexes.get(1) + 1);
//            if (indexes.get(1) == hoursOfDay.size()) {
//                indexes.set(1, 0);
//                indexes.set(2, indexes.get(2) + 1);
//                if (indexes.get(2) == possibleDateTimes.size()) {
//                    done = true;
//                }
//            }
//        }
        while (!done) {
            int dateIndex = indexes.get(2);
            int hourIndex = indexes.get(1);
            //iterate through minutes
            for (int minute = indexes.get(0); minute < minutesOfHour.size(); minute++) {
                result.add(possibleDateTimes.get(dateIndex).
                        withHour(hoursOfDay.get(hourIndex)).
                        withMinute(minutesOfHour.get(minute)));
                if (result.size() == count) {
                    done = true;
                    break;
                }
            }
            //increase hours and days
            if (hourIndex < hoursOfDay.size() - 1) {
                indexes.set(0, 0);
                indexes.set(1, hourIndex + 1);
            } else {
                if (dateIndex < possibleDateTimes.size() - 1) {
                    indexes.set(0, 0);
                    indexes.set(1, 0);
                    indexes.set(2, dateIndex + 1);
                } else {
                    done = true;
                }
            }

        }
        return result;
    }


    private Pair<Integer, LocalDateTime> findStartDateTime(List<LocalDateTime> possibleDateTimes, LocalDateTime givenDate) {
        for (LocalDateTime possibleDateTime : possibleDateTimes) {
            for (Integer hourOfDay : hoursOfDay) {
                for (Integer minOfHour : minutesOfHour) {
                    LocalDateTime possibleWithHM = possibleDateTime.withHour(hourOfDay).withMinute(minOfHour);
                    if (possibleWithHM.isAfter(givenDate)) {
                        return new Pair<>(possibleDateTimes.indexOf(possibleDateTime), possibleWithHM);
                    }
                }
            }
        }
        return new Pair<>(-1, null);
    }


    public String getCron() {
        return cron;
    }

    //index if found in list (0/size-1) 5:[3,5,7]- 1
    //index if next is bigger (0/(size-1)) 4:[3,5,7]- 2
    //(size) if  8:[3,5,7] - 3
    private int getIndex(List<Integer> possibleValues, int givenValue) {
        for (int i = 0; i < possibleValues.size(); i++) {
            Integer possibleValue = possibleValues.get(i);
            if (possibleValue.equals(givenValue) || possibleValue.compareTo(givenValue) > 0) {
                return i;
            }
        }

        return possibleValues.size();
    }
}
