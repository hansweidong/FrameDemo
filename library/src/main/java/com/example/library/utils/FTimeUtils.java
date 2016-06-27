package com.example.library.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by bjhl on 16/6/27.
 *
 * 时间相关方法
 */
public class FTimeUtils {
    private static long MSEC_OF_ONE_DAY = 1000 * 24 * 60 * 60;// 一天的毫秒数
    private static long nMSEC_OF_ONE_HOUR = 1000 * 60 * 60;// 一小时的毫秒数
    private static long MSEC_OF_ONE_MINUTE = 1000 * 60;// 一分钟的毫秒数
    private static long MSEC_OF_ONE_SEC = 1000;// 一秒钟的毫秒数
    /**
     * 8小时的毫秒数.用于时区计算.中国是+8时区.
     */
    public static final long HOUR8 = TimeUnit.HOURS.toMillis(8);
    /**
     * 16小时的毫秒数.用于时区计算.中国是+8时区,对24的补数是16.
     */
    public static final long HOUR16 = TimeUnit.HOURS.toMillis(16);
    public static final long DATE = TimeUnit.DAYS.toMillis(1);
    public static final long DAY = DATE;
    public static final long WEEK = TimeUnit.DAYS.toMillis(7);
    public static final long DAY7 = WEEK;
    public static final long DAY28 = TimeUnit.DAYS.toMillis(28);
    public static final long DAY29 = TimeUnit.DAYS.toMillis(29);
    public static final long DAY30 = TimeUnit.DAYS.toMillis(30);
    public static final long DAY31 = TimeUnit.DAYS.toMillis(31);
    public static final long DAY365 = TimeUnit.DAYS.toMillis(365);
    public static final long DAY366 = TimeUnit.DAYS.toMillis(366);
    public static final long YEAR_COMMON = DAY365;
    public static final long YEAR2 = YEAR_COMMON * 2;
    public static final long YEAR_LEAP = DAY366;

    public static final long YEAR4 = YEAR_COMMON * 3 + YEAR_LEAP;

    public static final long LEAP_DAY_START_IN_4Y = YEAR_COMMON * 2 + DAY31 + DAY28;

    public static final long LEAP_DAY_END_IN_4Y = YEAR_COMMON * 2 + DAY31 + DAY29;

    /**
     * 判断两个时间是否在同一天内.如果escapeYear为true,则不计较年份.可用于节日,生日等每年都有的日子
     *
     * @param escapeYear 如果escapeYear为true,则不计较年份
     */
    public static boolean isTheSameDay(final Date day1, final Date day2, final boolean escapeYear) {
        return isTheSameDay(day1, day2, escapeYear, true);
    }

    public static boolean isTheSameDay(final Date day1, final Date day2, final boolean escapeYear,
        final boolean checkSummerTime) {
        return checkSummerTime ? isTheSameDayCheckSummerTime(day1, day2, escapeYear) : isTheSameDayNoSummerTimeCheck(
            day1, day2, escapeYear);
    }

    private static boolean isTheSameDayCheckSummerTime(final Date day1, final Date day2, final boolean escapeYear) {
        final Calendar c = Calendar.getInstance();
        c.setTime(day1);
        final int y = c.get(Calendar.YEAR);
        final int m = c.get(Calendar.MONTH);
        final int d = c.get(Calendar.DATE);
        c.setTime(day2);
        return (escapeYear || (y == c.get(Calendar.YEAR))) && (m == c.get(Calendar.MONTH))
            && (d == c.get(Calendar.DATE));
    }

    /**
     * 已验证从0L毫秒起的4万天正确.1970年以前,2089年以后未验证
     */
    private static boolean isTheSameDayNoSummerTimeCheck(final Date day1, final Date day2, final boolean escapeYear) {
        final long t1 = day1.getTime(), t2 = day2.getTime();
        if (escapeYear) {
            final long offset4Y1 = (t1 + HOUR8) % YEAR4, offset4Y2 = (t2 + HOUR8) % YEAR4;
            if (offset4Y1 < LEAP_DAY_START_IN_4Y)// d1在闰日前
            {
                if (offset4Y2 < LEAP_DAY_START_IN_4Y) {
                    return offset4Y1 % YEAR_COMMON / DAY == offset4Y2 % YEAR_COMMON / DAY;
                } else if (offset4Y2 >= LEAP_DAY_END_IN_4Y) {
                    return offset4Y1 % YEAR_COMMON / DAY == (offset4Y2 - DAY) % YEAR_COMMON / DAY;
                } else {
                    return false;// d2是闰日
                }
            } else if (offset4Y1 >= LEAP_DAY_END_IN_4Y) {
                if (offset4Y2 < LEAP_DAY_START_IN_4Y) {
                    return (offset4Y1 - DAY) % YEAR_COMMON / DAY == offset4Y2 % YEAR_COMMON / DAY;
                } else if (offset4Y2 >= LEAP_DAY_END_IN_4Y) {
                    return offset4Y1 % YEAR_COMMON / DAY == offset4Y2 % YEAR_COMMON / DAY;
                } else {
                    return false;
                }
            } else {
                // d1是闰日
                return (offset4Y2 >= LEAP_DAY_START_IN_4Y) && (offset4Y2 < LEAP_DAY_END_IN_4Y);
            }
        } else {
            final long t81 = t1 / HOUR8, t82 = t2 / HOUR8;
            if (t81 == t82) {
                return true;
            }
            return (t81 + 1) / 3 == (t82 + 1) / 3;
        }
    }

    /**
     * 判断给定的时间是不是今天.忽略时分秒
     */
    public static boolean isToday(final Date thatDay) {
        return isTheSameDay(new Date(), thatDay, false);
    }

    /**
     * 返回年月日，如果是今年就不显示年份
     */
    public static String getFormatDay(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        Calendar curr = Calendar.getInstance();
        int currYear = curr.get(Calendar.YEAR);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        String formatStr = "yyyy-MM-dd";
        if (currYear == year) {
            formatStr = "MM-dd";
        }
        DateFormat f = new SimpleDateFormat(formatStr, Locale.getDefault());
        return f.format(d);
    }

    public static String getWeekChineseName(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    /**
     * yyyy-MM-dd hh:mm:ss
     */
    public static String formatTime(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return f.format(d);
    }

    /**
     * HH:mm
     */
    public static String formatTime1(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        // 大写H是24小时制
        DateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return f.format(d);
    }

    /**
     * 几天前，几小时前等类似格式 如果不是今年显示年月日 如果是今年 超过3天前则显示月日 没超过3天前就显示几天前等格式
     */
    public static String formatTime2(long cmsec) {
        try {
            long now = System.currentTimeMillis();
            long diff = now - cmsec;
            if (diff < 0) {
                return "刚刚";
            }
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(cmsec);
            Calendar nowC = Calendar.getInstance();
            boolean isThisYear = c.get(Calendar.YEAR) == nowC.get(Calendar.YEAR);

            long hour = diff % MSEC_OF_ONE_DAY / nMSEC_OF_ONE_HOUR;// 计算差多少小时
            long min = diff % MSEC_OF_ONE_DAY % nMSEC_OF_ONE_HOUR / MSEC_OF_ONE_MINUTE;// 计算差多少分钟
            long sec = diff % MSEC_OF_ONE_DAY % nMSEC_OF_ONE_HOUR % MSEC_OF_ONE_MINUTE / MSEC_OF_ONE_SEC;// 计算差多少秒

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            Calendar yes = Calendar.getInstance();
            yes.add(Calendar.DATE, -1);
            yes.set(Calendar.HOUR_OF_DAY, 0);
            yes.set(Calendar.MINUTE, 0);
            yes.set(Calendar.SECOND, 0);
            yes.set(Calendar.MILLISECOND, 0);
            Calendar beforeYes = Calendar.getInstance();
            beforeYes.add(Calendar.DATE, -2);
            beforeYes.set(Calendar.HOUR_OF_DAY, 0);
            beforeYes.set(Calendar.MINUTE, 0);
            beforeYes.set(Calendar.SECOND, 0);
            beforeYes.set(Calendar.MILLISECOND, 0);
            StringBuilder sb = new StringBuilder();
            if (!isThisYear) {
                Date d = c.getTime();
                DateFormat f = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
                return f.format(d);
            }
            if (c.before(beforeYes)) {
                Date d = c.getTime();
                DateFormat f = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
                return f.format(d);
            } else if (c.before(yes)) {
                Date d = c.getTime();
                DateFormat f = new SimpleDateFormat("前天 HH:mm", Locale.getDefault());
                return f.format(d);
            } else if (c.before(today)) {
                Date d = c.getTime();
                DateFormat f = new SimpleDateFormat("昨天 HH:mm", Locale.getDefault());
                return f.format(d);
            } else if (hour > 0 || min > 0) {
                Date d = c.getTime();
                DateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return f.format(d);
            } else if (sec > 0) {
                return "刚刚";
            }
            return sb.toString();
        } catch (Exception e) {
            return String.valueOf(cmsec);
        }
    }

    /**
     * yyyy-MM-dd
     */
    public static String formatTime3(long d) {
        Date date = new Date(d);
        return formatTime3(date);
    }

    /**
     * yyyy-MM-dd
     */
    public static String formatTime3(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return f.format(d);
    }

    /**
     * MM-dd
     */
    public static String formatTime4(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        DateFormat f = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return f.format(d);
    }

    /**
     * 8月5日 周三
     */
    public static String getFormatTime3(Date date) {
        if (date == null) {
            return "";
        }
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(date);
        String str = "";
        str +=
            String.format("%02d", startCal.get(Calendar.MONTH) + 1) + "月"
                + String.format("%02d", startCal.get(Calendar.DAY_OF_MONTH)) + "日" + " "
                + getWeekChineseName(startCal.get(Calendar.DAY_OF_WEEK));
        return str;
    }

    /**
     * yyyy-MM-dd hh:mm
     */
    public static String formatTime5(Date d) {
        if (d == null) {
            d = new Date(0);
        }
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return f.format(d);
    }
}
