package com.example.android.common;

import java.util.Comparator;

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataItem implements Comparator {

    public int timeHour;
    public int timeMinute;
    public double timeSecond;
    public double angleHour;
    public double angleMinute;
    public double angleSecond;

    public ClockDataItem(int hour, int minute, double second) {
        timeHour = hour;
        timeMinute = minute;
        timeSecond = second;
    }

    @Override
    public int compare(Object o1, Object o2) {
        ClockDataItem clock1 = (ClockDataItem) o1;
        ClockDataItem clock2 = (ClockDataItem) o2;
        Double pieSlice1 = Math.abs(clock1.angleHour - clock1.angleMinute);
        Double pieSlice2 = Math.abs(clock2.angleHour - clock2.angleMinute);
        return Double.compare(pieSlice1 - 120.0, pieSlice2 - 120.0);
    }
}
