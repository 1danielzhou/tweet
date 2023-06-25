package com.daniel.ltc20.utils;

import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class TimeUtil {
    public static Date convertToShanghaiTime(String utcTimeString) {
        try {
            // 创建SimpleDateFormat对象，指定输入时间字符串的格式和时区为UTC
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // 将输入时间字符串解析为Date对象
            Date utcDate = utcFormat.parse(utcTimeString);

            // 创建SimpleDateFormat对象，指定输出时间的格式和时区为上海
            SimpleDateFormat shanghaiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            shanghaiFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            // 格式化Date对象为上海时间的字符串
            String shanghaiTimeString = shanghaiFormat.format(utcDate);

            // 将上海时间的字符串解析为Date对象
            return shanghaiFormat.parse(shanghaiTimeString);
        } catch (Exception e) {
            log.error("{}，时间转换失败", utcTimeString);
        }
        return null;
    }

    public static Date getCurrentDateInShanghai() {
        try {
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            // 获取上海时区
            ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
            // 将当前时间转换为上海时区的时间
            LocalDateTime shanghaiDateTime = now.atZone(shanghaiZoneId).toLocalDateTime();
            // 创建 Calendar 对象并设置上海时区
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(shanghaiDateTime.atZone(shanghaiZoneId).toInstant()));
            // 返回上海时间的 Date 对象
            return calendar.getTime();
        } catch (Exception e) {
            log.debug("获取当前时间出错，{}", e);
        }
        return new Date();
    }

    public static boolean isWithinIntervalHours(Date date, int interval) {
        try {
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, interval);
            Date futureDate = calendar.getTime();
            return currentDate.before(futureDate);
        } catch (Exception e) {
            return false;
        }
    }

    public static Pair<Date, Date> getYesterdayTimeRange() {
        // 获取当前北京时间
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        // 获取昨天的开始时间和结束时间
        LocalDate yesterday = now.toLocalDate().minusDays(1);
        LocalDateTime yesterdayStart = yesterday.atStartOfDay();
        LocalDateTime yesterdayEnd = yesterday.atTime(LocalTime.MAX);

        // 转换为Date类型
        Instant startInstant = yesterdayStart.atZone(zoneId).toInstant();
        Instant endInstant = yesterdayEnd.atZone(zoneId).toInstant();
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        // 返回时间范围
        return Pair.of(startDate, endDate);
    }

    public static Pair<Date, Date> getTodayTimeRange() {
        // 获取当前北京时间
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        // 获取今天的开始时间和结束时间
        LocalDate today = now.toLocalDate();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(LocalTime.MAX);

        // 转换为Date类型
        Instant startInstant = todayStart.atZone(zoneId).toInstant();
        Instant endInstant = todayEnd.atZone(zoneId).toInstant();
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        // 返回时间范围
        return Pair.of(startDate, endDate);
    }

    public static Pair<Date, Date> getSevenDaysAgoToYesterdayTimeRange() {
        // 获取当前北京时间
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        // 获取昨天的开始时间和结束时间
        LocalDate yesterday = now.toLocalDate().minusDays(1);
        LocalDateTime yesterdayStart = yesterday.atStartOfDay();

        // 获取7天前的开始时间
        LocalDate sevenDaysAgo = yesterday.minusDays(7);
        LocalDateTime sevenDaysAgoStart = sevenDaysAgo.atStartOfDay();

        // 转换为Date类型
        Instant yesterdayStartInstant = yesterdayStart.atZone(zoneId).toInstant();
        Instant sevenDaysAgoStartInstant = sevenDaysAgoStart.atZone(zoneId).toInstant();
        Date yesterdayStartDate = Date.from(yesterdayStartInstant);
        Date sevenDaysAgoStartDate = Date.from(sevenDaysAgoStartInstant);

        // 返回时间范围
        return Pair.of(sevenDaysAgoStartDate, yesterdayStartDate);
    }

    public static Date extractDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 将时间部分设为零时
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
