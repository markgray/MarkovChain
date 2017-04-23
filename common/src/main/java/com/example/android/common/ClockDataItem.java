package com.example.android.common;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataItem implements Comparable<ClockDataItem> {

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
        angleSecond = 6.0 * second;
        angleMinute = 6.0 * (minute + second/60.0);
        angleHour = 30.0 * (hour + minute/60.0 + second/3600.0);
    }

    public double badness() {
        double diff = (angleHour < angleMinute) ? angleMinute - angleMinute : angleHour - angleMinute;
        if (diff > 180.0) {
            diff -= 120.0;
        }
        return Math.abs(120.0 - diff);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     *
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    @Override
    public int compareTo(@NonNull ClockDataItem o) {
        return  Double.compare(this.badness(), o.badness());
    }
}
