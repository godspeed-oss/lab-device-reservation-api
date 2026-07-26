package com.lab.reservation.util;

import java.time.LocalTime;

public class TimeUtil {
    private TimeUtil() {
    }

    public static boolean isTimeRangeInvalid(LocalTime startTime, LocalTime endTime) {
        return startTime == null || endTime == null || !startTime.isBefore(endTime);
    }

    public static boolean isTimeConflict(LocalTime newStartTime, LocalTime newEndTime,
                                         LocalTime existingStartTime, LocalTime existingEndTime) {
        return newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime);
    }
}