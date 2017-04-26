package com.example.android.common;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

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

    /** Cache storage for {@code badness} method */
    double badnessCache;
    /**
     *
     * @return a value indicating how far from a perfect trisection this ClockDataItem is
     */
    public double badness() {
        badnessCache = (angleHour < angleMinute) ? angleMinute - angleMinute : angleHour - angleMinute;
        if (badnessCache > 180.0) {
            badnessCache -= 120.0;
        }
        badnessCache = Math.abs(120.0 - badnessCache);
        return badnessCache;
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

    /** Cache storage for our {@code orderedAngles} method */
    Double[] orderedAnglesCache;
    /**
     * Sorts the angles of the clock hands into sorted order.
     *
     * @return {@code angleHour, angleMinute, angleSecond} in sorted order
     */
    public Double[] orderedAngles() {
        if (orderedAnglesCache == null) {
            ArrayList<Double> returnList = new ArrayList<>();
            returnList.add(angleHour);
            returnList.add(angleMinute);
            returnList.add(angleSecond);
            Collections.sort(returnList);
            orderedAnglesCache = new Double[3];
            for (int i = 0; i < 3; i++) {
                orderedAnglesCache[i] = returnList.get(i);
            }
        }
        return orderedAnglesCache;
    }

    /** Cache storage fo our {@code pieSlices} method */
    Double[] pieSlicesCache;
    /**
     * Calculates the angular sizes of the three pie slices of the clock face.
     *
     * @return The pie slice arc angles of the clock face
     */
    public Double[] pieSlices() {
        if (pieSlicesCache == null) {
            pieSlicesCache = new Double[3];
            Double[] angles = orderedAngles();
            pieSlicesCache[0] = angles[1] - angles[0];
            pieSlicesCache[1] = angles[2] - angles[1];
            pieSlicesCache[2] = (360.0 - angles[2]) + angles[0];
        }
        return pieSlicesCache;
    }

    @Override
    public String toString() {
        Double[] pie = pieSlices();
        return timeHour + ":" + timeMinute + ":" + timeSecond + "\n"
                + pie[0] + "\n" + pie[1] + "\n" + pie[2] + "\n"
                + badness();
    }
}
