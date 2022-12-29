package congestion.calculator;

import congestion.calculator.entity.CityEntity;
import congestion.calculator.entity.CityHolidays;
import congestion.calculator.entity.CityHolidayMonths;
import congestion.calculator.entity.CityTaxDays;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Boolean isWeekend(CityTaxDays cityTaxDays, int day) {
        if(cityTaxDays == null) return false;

        if(cityTaxDays.isMonday() == false && day == Calendar.MONDAY) return true;
        if(cityTaxDays.isTuesday() == false && day == Calendar.TUESDAY) return true;
        if(cityTaxDays.isWednesday() == false && day == Calendar.WEDNESDAY) return true;
        if(cityTaxDays.isThursday() == false && day == Calendar.THURSDAY) return true;
        if(cityTaxDays.isFriday() == false && day == Calendar.FRIDAY) return true;
        if(cityTaxDays.isSaturday() == false && day == Calendar.SATURDAY) return true;
        if(cityTaxDays.isSunday() == false && day == Calendar.SUNDAY) return true;

        return false;
    }

    public static Boolean isHolidayMonth(CityHolidayMonths cityHolidayMonths, int month) {
        if(cityHolidayMonths == null) return false;

        if(cityHolidayMonths.isJanuary() == true && month == (Calendar.JANUARY+1)) return true;
        if(cityHolidayMonths.isFebruary() == true && month == (Calendar.FEBRUARY+1)) return true;
        if(cityHolidayMonths.isMarch() == true && month == (Calendar.MARCH+1)) return true;
        if(cityHolidayMonths.isApril() == true && month == (Calendar.APRIL+1)) return true;
        if(cityHolidayMonths.isMay() == true && month == (Calendar.MAY+1)) return true;
        if(cityHolidayMonths.isJune() == true && month == (Calendar.JUNE+1)) return true;
        if(cityHolidayMonths.isJuly() == true && month == (Calendar.JULY+1)) return true;
        if(cityHolidayMonths.isAugust() == true && month == (Calendar.AUGUST+1)) return true;
        if(cityHolidayMonths.isSeptember() == true && month == (Calendar.SEPTEMBER+1)) return true;
        if(cityHolidayMonths.isOctober() == true && month == (Calendar.OCTOBER+1)) return true;
        if(cityHolidayMonths.isNovember() == true && month == (Calendar.NOVEMBER+1)) return true;
        if(cityHolidayMonths.isDecember() == true && month == (Calendar.DECEMBER+1)) return true;

        return false;
    }

    public static Boolean isTaxFreeDay(Date date, CityEntity cityEntity) {
        Set<CityHolidays> publicHolidays = cityEntity.getCityHolidays();
        if(publicHolidays == null || publicHolidays.isEmpty())
            return false;

        if (publicHolidays.stream().filter(holiday -> DateUtils.isSameDay(holiday.getDate(), date)).count() > 0 )
            return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInRange(
                        DateUtils.addDays(holiday.getDate(), -(cityEntity.getCityTaxRules().getNumberOfTaxFreeDaysBeforeHoliday())),
                        holiday.getDate(),
                        date
                ))
                .count() > 0 )
            return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInRange(
                        holiday.getDate(),
                        DateUtils.addDays(holiday.getDate(), cityEntity.getCityTaxRules().getNumberOfTaxFreeDaysAfterHoliday()),
                        date
                ))
                .count() > 0 )
            return true;

        return false;
    }

    public static boolean isDateInRange(final Date min, final Date max, final Date date){
        return !(date.before(min) || date.after(max));
    }

    public static String removeTime(Date date) {
        return dateFormat.format(date);
    }

    public static Date objectToDate(Object date) throws ParseException {
        if(date instanceof String)
            return dateAndTimeFormat.parse(date.toString());
        else
            return dateAndTimeFormat.parse(dateAndTimeFormat.format(((Date)date)));
    }

    public static void sortDateByAsc(List<Date> dates) {
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date object1, Date object2) {
                return object1.compareTo(object2);
            }
        });
    }
}
