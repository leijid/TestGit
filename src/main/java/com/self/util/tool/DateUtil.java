/**
 * leijid
 *
 * DateUtil.java
 *
 * 2014��9��16��
 */
package com.self.util.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author leijid
 * 
 */
public class DateUtil {

    private static final Logger logger = Logger.getLogger(DateUtil.class);

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static Date stringToDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e);
        }
        return date;
    }

    public static Calendar addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    public static Calendar addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar;
    }

    public static Calendar addYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar;
    }

    public static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date date = getCalendarTime(cal, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return date;
    }

    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        Date date = getCalendarTime(cal, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return date;
    }

    private static Date getCalendarTime(Calendar cal, int dayOfMonth) {
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    public static void main(String[] args) {
        System.out.println(getFirstDayOfMonth());
        System.out.println(getLastDayOfMonth());
    }

}
