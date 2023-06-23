package com.daniel.ltc20.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public static boolean isWithin48Hours(Date date) {
        try {
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 48);
            Date futureDate = calendar.getTime();
            return currentDate.before(futureDate);
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isWithin30Days(Date date) {
        try {
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date futureDate = calendar.getTime();
            return currentDate.before(futureDate);
        }catch (Exception e){
            return false;
        }
    }
}
