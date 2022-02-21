package com.jdk8;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @Description DateTimeTest
 * @author fangxilin
 * @date 2020-10-11
 * @Copyright: 深圳市宁远科技股份有限公司版权所有(C)2020
 */

/**
 * 新的 日期/时间API
 */
public class DateTimeTest {

    static void clockTest() {
        Clock clock = Clock.systemUTC();
        System.out.println("clock.instant() = " + clock.instant());
        //可代替 System.currentTimeMillis()
        System.out.println("clock.millis() = " + clock.millis());
    }

    /**
     * LocaleDate只包含ISO-8601日历系统的中日期部分，不含时区信息
     */
    static void localDateTest() {
        LocalDate now = LocalDate.now();
        System.out.println("now = " + now); //输出格式为 2020-10-11
        Clock clock = Clock.systemUTC();
        LocalDate nowFromClock = LocalDate.now(clock);
        System.out.println("nowFromClock = " + nowFromClock);
    }

    /**
     * LocaleTime只包含ISO-8601日历系统中的时间部分，不含时区信息
     */
    static void localTimeTest() {
        LocalTime now = LocalTime.now();
        System.out.println("now = " + now); //输出格式为 14:22:06.436
        Clock clock = Clock.systemUTC();
        LocalTime nowFromClock = LocalTime.now(clock);
        System.out.println("nowFromClock = " + nowFromClock); //输出格式为 06:22:06.438
    }

    /**
     * LocalDateTime 含有ISO-8601日历系统下中的日期和时间部分，但是不含时区信息
     */
    static void localDateTimeTest() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now); //输出格式为 2020-10-11T14:29:42.731
        String formatTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("formatTime = " + formatTime);
    }

    /**
     * ZonedDateTime 包含ISO-8601日历系统中的日期和时间，同时含有时区信息
     */
    static void zonedDateTimeTest() {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("now = " + now); //输出格式为 2020-10-11T14:35:39.377+08:00[Asia/Shanghai]
        ZonedDateTime nowFromZoneId = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        System.out.println("nowFromZoneId = " + nowFromZoneId);
    }

    /**
     * Duration类：基于秒和纳秒的时间计数。使用它可以非常容易的计算两个日期间的差异
     */
    static void DurationTest() {
        final LocalDateTime from = LocalDateTime.of( 2021, Month.SEPTEMBER, 13, 0, 0, 0 );
        final LocalDateTime to = LocalDateTime.of( 2021, Month.DECEMBER, 30, 23, 59, 59 );

        final Duration duration = Duration.between( from, to );
        System.out.println( "Duration in days: " + duration.toDays() );
        System.out.println( "Duration in hours: " + duration.toHours() );
        System.out.println( "Duration in minutes: " + duration.toMinutes());
        System.out.println( "Duration in millis: " + duration.toMillis());
        System.out.println( "Duration in seconds: " + duration.getSeconds());
    }

    public static void main(String[] args) {
//        clockTest();
//        localDateTest();
//        localTimeTest();
//        localDateTimeTest();
//        zonedDateTimeTest();
        DurationTest();
    }
}
