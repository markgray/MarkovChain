package com.example.android.common;

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
public class ClockDataItem {

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
}
