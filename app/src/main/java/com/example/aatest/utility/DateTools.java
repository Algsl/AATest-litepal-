package com.example.aatest.utility;

import java.io.Serializable;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by l1843 on 2017/2/8.
 */

public class DateTools implements Serializable {
    private static final long serialVersionUID = -3098985139095632110L;
    private DateTools() { }
    public static String dateFormat(String sdate, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        java.sql.Date date = java.sql.Date.valueOf(sdate);
        String dateString = formatter.format(date);
        return dateString;
    }
    public static long getIntervalDays(String sd, String ed) {
        return ((java.sql.Date.valueOf(ed)).getTime() -
                (java.sql.Date .valueOf(sd)).getTime()) / (3600 * 24 * 1000);
    }
    public static int getInterval(String beginMonth, String endMonth) {
        int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));
        int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth .indexOf("-") + 1));
        int intEndYear = Integer.parseInt(endMonth.substring(0, 4));
        int intEndMonth = Integer.parseInt(endMonth.substring(endMonth .indexOf("-") + 1));
        return ((intEndYear - intBeginYear) * 12) + (intEndMonth - intBeginMonth) + 1;
    }
    public static Date getDate(String sDate, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);
        return fmt.parse(sDate, pos);
    }
    public static String getCurrentYear() {
        return getFormatCurrentTime("yyyy");
    }
    public static String getBeforeYear() {
        String currentYear = getFormatCurrentTime("yyyy");
        int beforeYear = Integer.parseInt(currentYear) - 1;
        return "" + beforeYear;
    }
    public static String getCurrentMonth() {
        return getFormatCurrentTime("MM");
    }
    public static String getCurrentDay() {
        return getFormatCurrentTime("dd");
    }
    public static String getCurrentDate() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd");
    }
    public static String getCurrentDateTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    public static String getFormatDate(Date date) { return getFormatDateTime(date, "yyyy-MM-dd"); }
    public static String getFormatDate(String format) { return getFormatDateTime(new Date(), format); }
    public static String getCurrentTime() {
        return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    public static String getFormatTime(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd HH:mm:ss"); }
    public static String getFormatShortTime(Date date) {
        return getFormatDateTime(date, "yyyy-MM-dd");
    }
    public static String getFormatCurrentTime(String format) { return getFormatDateTime(new Date(), format); }
    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format); return sdf.format(date);
    }
    public static Date getDateObj(int year, int month, int day) {
        Calendar c = new GregorianCalendar();
        c.set(year, month - 1, day);
        return c.getTime();
    }
    public static String getDateTomorrow(String date) {
        Date tempDate = null;
        if (date.indexOf("/") > 0)
            tempDate = getDateObj(date, "[/]");
        if (date.indexOf("-") > 0)
            tempDate = getDateObj(date, "[-]");
        tempDate = getDateAdd(tempDate, 1);
        return getFormatDateTime(tempDate, "yyyy/MM/dd");
    }
    public static String getDateOffset(String date, int offset) {
        // Date tempDate = getDateObj(date, "[/]");
        Date tempDate = null;
        if (date.indexOf("/") > 0)
            tempDate = getDateObj(date, "[/]");
        if (date.indexOf("-") > 0)
            tempDate = getDateObj(date, "[-]");
        tempDate = getDateAdd(tempDate, offset);
        return getFormatDateTime(tempDate, "yyyy/MM/dd");
    }
    public static Date getDateObj(String argsDate, String split) {
        String[] temp = argsDate.split(split);
        int year = new Integer(temp[0]).intValue();
        int month = new Integer(temp[1]).intValue();
        int day = new Integer(temp[2]).intValue();
        return getDateObj(year, month, day);
    }
    public static Date getDateFromString(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date resDate = null;
        try {
            resDate = sdf.parse(dateStr);
        } catch (Exception e) { e.printStackTrace(); }
        return resDate;
    }
    public static Date getDateObj() {
        Calendar c = new GregorianCalendar();
        return c.getTime();
    }
    public static int getDaysOfCurMonth() {
        int curyear = new Integer(getCurrentYear()).intValue();
        int curMonth = new Integer(getCurrentMonth()).intValue();
        int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
            mArray[1] = 29;
        }
        return mArray[curMonth - 1];
    }
    public static int getDaysOfCurMonth(final String time) {
        if (time.length() != 7) {
            throw new NullPointerException("参数的格式必须是yyyy-MM");
        }
        String[] timeArray = time.split("-");
        int curyear = new Integer(timeArray[0]).intValue();
        int curMonth = new Integer(timeArray[1]).intValue();
        if (curMonth > 12) {
            throw new NullPointerException("参数的格式必须是yyyy-MM，而且月份必须小于等于12。");
        }
        int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
            mArray[1] = 29;
        }
        if (curMonth == 12) {
            return mArray[0];
        }
        return mArray[curMonth - 1];
    }
    public static int getDayofWeekInMonth(String year, String month, String weekOfMonth, String dayOfWeek) {
        Calendar cal = new GregorianCalendar();
        int y = new Integer(year).intValue();
        int m = new Integer(month).intValue();
        cal.clear();
        cal.set(y, m - 1, 1);
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, new Integer(weekOfMonth).intValue());
        cal.set(Calendar.DAY_OF_WEEK, new Integer(dayOfWeek).intValue());
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public static Date getDate(int year, int month, int date, int hourOfDay, int minute, int second) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }
    public static int getDayOfWeek(String year, String month, String day) {
        Calendar cal = new GregorianCalendar(new Integer(year).intValue(),
                new Integer(month).intValue() - 1, new Integer(day).intValue());
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    public static int getDayOfWeek(String date) {
        String[] temp = null;
        if (date.indexOf("/") > 0) {
            temp = date.split("/");
        }
        if (date.indexOf("-") > 0) {
            temp = date.split("-");
        }
        return getDayOfWeek(temp[0], temp[1], temp[2]);
    }
    public static String getChinaDayOfWeek(String date) {
        String[] weeks = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }
    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    public static int getWeekOfYear(String year, String month, String day) {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(new Integer(year).intValue(), new Integer(month).intValue() - 1,
                new Integer(day).intValue());
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    public static Date getDateAdd(Date date, int amount) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.DATE, amount);
        return cal.getTime();
    }
    public static String getFormatDateAdd(Date date, int amount, String format) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.DATE, amount);
        return getFormatDateTime(cal.getTime(), format);
    }
    public static String getFormatCurrentAdd(int amount, String format) {
        Date d = getDateAdd(new Date(), amount);
        return getFormatDateTime(d, format);
    }
    public static String getFormatYestoday(String format) { return getFormatCurrentAdd(-1, format); }
    public static String getYestoday(String sourceDate, String format) {
        return getFormatDateAdd(getDateFromString(sourceDate, format), -1, format);
    }
    public static String getFormatTomorrow(String format) { return getFormatCurrentAdd(1, format); }
    public static String getFormatDateTommorrow(String sourceDate, String format) {
        return getFormatDateAdd(getDateFromString(sourceDate, format), 1, format); }
    public static String getCurrentDateString(String dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }
    public static String getCurTimeByFormat(String format) {
        Date newdate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(newdate);
    }
    public static long getDiff(String startTime, String endTime) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = ft.parse(startTime);
            Date endDate = ft.parse(endTime);
            diff = startDate.getTime() - endDate.getTime();
            diff = diff / 1000;
        } catch (ParseException e) { e.printStackTrace(); }
        return diff;
    }
    public static String getHour(long second) {
        long hour = second / 60 / 60;
        long minute = (second - hour * 60 * 60) / 60;
        long sec = (second - hour * 60 * 60) - minute * 60;
        return hour + "小时" + minute + "分钟" + sec + "秒";
    }
    public static String getDateTime(long microsecond) {
        return getFormatDateTime(new Date(microsecond), "yyyy-MM-dd HH:mm:ss");
    }
    public static String getDateByAddFltHour(float flt) {
        int addMinute = (int) (flt * 60);
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.MINUTE, addMinute);
        return getFormatDateTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }
    public static String getDateByAddHour(String datetime, int minute) {
        String returnTime = null;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = ft.parse(datetime);
            cal.setTime(date);
            cal.add(GregorianCalendar.MINUTE, minute);
            returnTime = getFormatDateTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) { e.printStackTrace();}
        return returnTime;
    }
    public static int getDiffHour(String startTime, String endTime) {
        long diff = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = ft.parse(startTime);
            Date endDate = ft.parse(endTime);
            diff = startDate.getTime() - endDate.getTime();
            diff = diff / (1000 * 60 * 60);
        } catch (ParseException e) { e.printStackTrace(); }
        return new Long(diff).intValue();
    }
    public static String getYearSelect(String selectName, String value, int startYear, int endYear) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");

            }
        }
        return sb.toString();
    }
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, boolean hasBlank) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, boolean hasBlank, String js) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, String js) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getMonthSelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 12; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getMonthSelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 12; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getDaySelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 31; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getDaySelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 31; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static int countWeekend(String startDate, String endDate) {
        int result = 0;
        Date sdate = null;
        Date edate = null;
        if (startDate.indexOf("/") > 0 && endDate.indexOf("/") > 0) {
            sdate = getDateObj(startDate, "/");
            edate = getDateObj(endDate, "/");
        }
        if (startDate.indexOf("-") > 0 && endDate.indexOf("-") > 0) {
            sdate = getDateObj(startDate, "-");
            edate = getDateObj(endDate, "-");
        }
        int sumDays = Math.abs(getDiffDays(startDate, endDate));
        int dayOfWeek = 0;
        for (int i = 0; i <= sumDays; i++) {
            dayOfWeek = getDayOfWeek(getDateAdd(sdate, i));
            if (dayOfWeek == 1 || dayOfWeek == 7) {
                result++;
            }
        }
        return result;
    }
    public static int getDiffDays(String startDate, String endDate) {
        long diff = 0;
        SimpleDateFormat ft = null;
        if (startDate.indexOf("/") > 0 && endDate.indexOf("/") > 0) {
            ft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        if (startDate.indexOf("-") > 0 && endDate.indexOf("-") > 0) {
            ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        try {
            Date sDate = ft.parse(startDate + " 00:00:00");
            Date eDate = ft.parse(endDate + " 00:00:00");
            diff = eDate.getTime() - sDate.getTime();
            diff = diff / 86400000;// 1000*60*60*24;
        } catch (ParseException e) { e.printStackTrace(); }
        return (int) diff;
    }
    public static String[] getArrayDiffDays(String startDate, String endDate) {
        int LEN = 0;
        if (startDate.equals(endDate)) {
            return new String[] { startDate };
        }
        Date sdate = null;
        if (startDate.indexOf("/") > 0 && endDate.indexOf("/") > 0) {
            sdate = getDateObj(startDate, "/");
        }
        if (startDate.indexOf("-") > 0 && endDate.indexOf("-") > 0) {
            sdate = getDateObj(startDate, "-");
        }
        LEN = getDiffDays(startDate, endDate);
        String[] dateResult = new String[LEN + 1];
        dateResult[0] = startDate;
        for (int i = 1; i < LEN + 1; i++) {
            if (startDate.indexOf("/") > 0 && endDate.indexOf("/") > 0) {
                dateResult[i] = getFormatDateTime(getDateAdd(sdate, i), "yyyy/MM/dd");
            }
            if (startDate.indexOf("-") > 0 && endDate.indexOf("-") > 0) {
                dateResult[i] = getFormatDateTime(getDateAdd(sdate, i), "yyyy-MM-dd");
            }
        }
        return dateResult;
    }
    public static boolean isInStartEnd(String srcDate, String startDate, String endDate) {
        if (startDate.compareTo(srcDate) <= 0 && endDate.compareTo(srcDate) >= 0) {
            return true;
        } else {
            return false;
        }
    }
    public static String getQuarterSelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 4; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String getQuarterSelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer("");
        sb.append("");
        if (hasBlank) {
            sb.append("");
        }
        for (int i = 1; i <= 4; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("");
            } else {
                sb.append("");
            }
        }
        return sb.toString();
    }
    public static String changeDate(String argDate) {
        if (argDate == null || argDate.trim().equals("")) {
            return "";
        }
        String result = "";
        if (argDate.length() == 10 && argDate.indexOf("/") > 0) {
            return argDate;
        } String[] str = argDate.split("[.]");
        int LEN = str.length;
        for (int i = 0; i < LEN; i++) {
            if (str[i].length() == 1) {
                if (str[i].equals("0")) {
                    str[i] = "01";
                }
            } else {
                str[i] = "0" + str[i];
            }
        }
        if (LEN == 1) {
            result = argDate + "/01/01";
        }
        if (LEN == 2) {
            result = str[0] + "/" + str[1] + "/01";
        }
        if (LEN == 3) {
            result = str[0] + "/" + str[1] + "/" + str[2];
        }
        return result;
    }
    public static String changeDateWithSplit(String argDate, String split) {
        if (argDate == null || argDate.trim().equals("")) {
            return "";
        }
        if (split == null || split.trim().equals("")) {
            split = "-";
        }
        String result = "";
        if (argDate.length() == 10 && argDate.indexOf("/") > 0) {
            return argDate;
        }
        if (argDate.length() == 10 && argDate.indexOf("-") > 0) {
            return argDate;
        }
        String[] str = argDate.split("[.]");
        int LEN = str.length;
        for (int i = 0; i < LEN; i++) {
            if (str[i].length() == 1) {
                if (str[i].equals("0")) {
                    str[i] = "01";
                } else {
                    str[i] = "0" + str[i];
                }
            }
        }
        if (LEN == 1) {
            result = argDate + split + "01" + split + "01";
        }
        if (LEN == 2) {
            result = str[0] + split + str[1] + split + "01";
        }
        if (LEN == 3) {
            result = str[0] + split + str[1] + split + str[2];
        }
        return result;
    }
    public static int getNextMonthDays(String argDate) {
        String[] temp = null;
        if (argDate.indexOf("/") > 0) {
            temp = argDate.split("/");
        }
        if (argDate.indexOf("-") > 0) {
            temp = argDate.split("-");
        }
        Calendar cal = new GregorianCalendar(new Integer(temp[0]).intValue(),
                new Integer(temp[1]).intValue() - 1, new Integer(temp[2]).intValue());
        int curMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, curMonth + 1);
        int curyear = cal.get(Calendar.YEAR);
        curMonth = cal.get(Calendar.MONTH);
        int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
            mArray[1] = 29;
        }
        return mArray[curMonth];
    }
    public static void main(String[] args) {
        System.out.println(DateTools.getCurrentDateTime());
        System.out.println("first=" + changeDateWithSplit("2000.1", ""));
        System.out.println("second=" + changeDateWithSplit("2000.1", "/"));
        String[] t = getArrayDiffDays("2008/02/15", "2008/02/19");
        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i]);
        }
        t = getArrayDiffDays("2008-02-15", "2008-02-19");
        for (int i = 0; i < t.length; i++) {
            System.out.println(t[i]);
        }
        System.out.println(getNextMonthDays("2008/02/15") + "||" + getCurrentMonth() + "||" + DateTools.changeDate("1999"));
        System.out.println(DateTools.changeDate("1999.1"));
        System.out.println(DateTools.changeDate("1999.11"));
        System.out.println(DateTools.changeDate("1999.1.2"));
        System.out.println(DateTools.changeDate("1999.11.12"));
    }
}

