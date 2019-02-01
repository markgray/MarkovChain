package com.example.android.common;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataItem implements Comparable<ClockDataItem> {

    /**
     * The hour of the day we represent
     */
    public int timeHour;
    /**
     * The minute of the hour we represent
     */
    public int timeMinute;
    /**
     * The second of the minute we represent
     */
    public double timeSecond;
    /**
     * Angle of the hour hand on the face of the clock
     */
    public double angleHour;
    /**
     * Angle of the minute hand on the face of the clock
     */
    public double angleMinute;
    /**
     * Angle of the second hand on the face of the clock
     */
    public double angleSecond;

    /**
     * Our constructor. We save our parameters in the fields we use for them, then use our parameters
     * to calculate the angles of the hands of the clock for that time of day which we save in our
     * fields {@code double angleSecond}, {@code double angleMinute}, and {@code double angleHour}.
     *
     * @param hour the hour we represent
     * @param minute minute we represent
     * @param second the second we represent
     */
    public ClockDataItem(int hour, int minute, double second) {
        timeHour = hour;
        timeMinute = minute;
        timeSecond = second;
        angleSecond = 6.0 * second;
        angleMinute = 6.0 * (minute + second/60.0);
        angleHour = 30.0 * (hour + minute/60.0 + second/3600.0);
    }

    /**
     * Cache storage for {@code badness} method
     */
    double badnessCache = -1.0;
    /**
     * Calculates how far the size of pie slices of the clock face created by the positions of the
     * clock hands misses the perfect trisection: (120,120,120). First we check whether there is a
     * cached result in our badness cache field {@code double badnessCache} and if so we return that
     * value. We then check to see if our pie slice cache {@code Double[] pieSlicesCache} is null and
     * if it is we call our method {@code pieSlices} to initialize it. Next we initialize our field
     * {@code badnessCache} to 0.0 then loop through all the {@code Double slice} is {@code pieSlicesCache}
     * adding the absolute value of the difference between 120.0 and {@code slice} to {@code badnessCache}.
     * When done we return {@code badnessCache} to the caller.
     *
     * @return a value indicating how far from a perfect trisection this ClockDataItem is
     */
    public double badness() {
        if (badnessCache >= 0.0) {
            return badnessCache;
        }
        if (pieSlicesCache == null) {
            pieSlices();
        }
        badnessCache = 0.0;
        for (Double slice : pieSlicesCache) {
            badnessCache += Math.abs(120.0 - slice);
        }
        return badnessCache;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object. We just return the value returned by the {@code compare} method of {@code Double}
     * returns when passed the badness value of our {@code ClockDataItem} and the badness value of
     * our parameter {@code ClockDataItem o}.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@NonNull ClockDataItem o) {
        return  Double.compare(this.badness(), o.badness());
    }

    /** Cache storage for our {@code orderedAngles} method */
    Double[] orderedAnglesCache;
    /**
     * Sorts the angles of the clock hands into sorted order. If our cache of order angles in our
     * field {@code Double[] orderedAnglesCache} is null we initialize {@code ArrayList<Double> returnList}
     * with a new instance, add our fields {@code angleHour}, {@code angleMinute}, and {@code angleSecond}
     * to it, use the {@code sort} method of {@code Collections} to sort {@code returnList} then initialize
     * {@code orderedAnglesCache} to the {@code Double[3]} array that the {@code toArray} creates from
     * {@code returnList}. Finally we return {@code orderedAnglesCache} to the caller.
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
            orderedAnglesCache = returnList.toArray(new Double[3]);
        }
        return orderedAnglesCache;
    }

    /** Cache storage for our {@code pieSlices} method */
    Double[] pieSlicesCache;
    /**
     * Calculates the angular sizes of the three pie slices of the clock face. If our cache of pie
     * slices in {@code Double[] pieSlicesCache} is null we initialize it with a new instance of
     * 3 {@code Double} objects, initialize {@code Double[] angles} with the ordered angles returned
     * by our method {@code orderedAngles} then set {@code pieSlicesCache[0]} to {@code angles[1]}
     * minus {@code angles[0]}, set {@code pieSlicesCache[1]} to {@code angles[2]} minus {@code angles[1]},
     * and set {@code pieSlicesCache[2]} to 360.0 minus {@code angles[2]} plus {@code angles[0]}.
     * Finally we return {@code pieSlicesCache} to the caller.
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

    /**
     * Returns a string representation of our {@code ClockDataItem}. We initialize our variable
     * {@code Double[] pie} with the array of pie slices calculated by our method {@code pieSlices},
     * then initialize {@code String sH} with the string value of {@code timeHour} (adding a leading
     * 0 if it is less than 10), initialize {@code String sM} with the string value of {@code timeMinute}
     * (adding a leading 0 if it is less than 10), and initialize {@code String sS} with the string
     * value of {@code timeSecond} (adding a leading 0 if it is less than 10). Finally we return the
     * string formed by concatenating {@code sH} followed by the ":" character, followed by {@code sM}
     * followed by the ":" character, {@code sS} followed by the newline character, followed by the
     * string value of {@code pie[0]} followed by the newline character, followed by the string value
     * of {@code pie[1]} followed by the newline character, followed by the string value of {@code pie[2]}
     * followed by the newline character, followed by the string value of the badness value returned
     * by our {@code badness} method followed by the string " Badness".
     *
     * @return a string representation of our {@code ClockDataItem}.
     */
    @NonNull
    @Override
    public String toString() {
        Double[] pie = pieSlices();
        String sH = timeHour < 10 ? "0" + timeHour : "" + timeHour;
        String sM = timeMinute < 10 ? "0" + timeMinute: "" + timeMinute;
        String sS = timeSecond < 10 ? "0" + timeSecond : "" + timeSecond;
        return sH + ":" + sM + ":" + sS + "\n"
                + pie[0] + "\n" + pie[1] + "\n" + pie[2] + "\n"
                + badness() + " Badness";
    }

    /**
     * Returns the minute of the day that this {@code ClockDataItem} belongs to (0-719).
     *
     * @return the minute of the day that this {@code ClockDataItem} belongs to (0-719).
     */
    public int motd() {
        return timeHour * 60 + timeMinute;
    }
}
