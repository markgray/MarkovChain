package com.example.android.common;

import androidx.annotation.NonNull;

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
     * The sizes of the three clock face pie slices made by the clock hands (120,120,120) is perfect.
     */
    public double[] pieSlices = new double[3];
    /**
     * How bad is our trisection (the sum of the absolute values when compared to perfect trisection)
     */
    public double badness;

    /**
     * Our constructor. We just call our method {@code init} with our parameters.
     *
     * @param hour the hour we represent
     * @param minute minute we represent
     * @param second the second we represent
     */
    public ClockDataItem(int hour, int minute, double second) {
        init(hour, minute, second);
    }

    /**
     * Sets the time of this {@code ClockDataItem} to its parameters. We just call our method
     * {@code init} with our parameters and return 'this'.
     *
     * @param hour   the hour we represent
     * @param minute minute we represent
     * @param second the second we represent
     * @return this {@code ClockDataItem} set to the new time
     */
    public ClockDataItem set(int hour, int minute, double second) {
        init(hour, minute, second);
        return this;
    }

    private void init(int hour, int minute, double second) {
        timeHour = hour;
        timeMinute = minute;
        timeSecond = second;
        angleSecond = 6.0 * second;
        angleMinute = 6.0 * (minute + second/60.0);
        angleHour = 30.0 * (hour + minute/60.0 + second/3600.0);
        badness = doBadness();
    }

    public void clone(ClockDataItem mom) {
        timeHour = mom.timeHour;
        timeMinute = mom.timeMinute;
        timeSecond = mom.timeSecond;
        angleHour = mom.angleHour;
        angleMinute = mom.angleMinute;
        angleSecond = mom.angleSecond;
        pieSlices[0] = mom.pieSlices[0];
        pieSlices[1] = mom.pieSlices[1];
        pieSlices[2] = mom.pieSlices[2];
        badness = mom.badness;
    }

    /**
     * Calculates how far the size of pie slices of the clock face created by the positions of the
     * clock hands misses the perfect trisection: (120,120,120). First we check whether there is a
     * cached result in our doBadness cache field {@code double badnessCache} and if so we return that
     * value. We then check to see if our pie slice cache {@code double[] pieSlices} is null and
     * if it is we call our method {@code doPieSlices} to initialize it. Next we initialize our field
     * {@code badnessCache} to 0.0 then loop through all the {@code double slice} is {@code pieSlices}
     * adding the absolute value of the difference between 120.0 and {@code slice} to {@code badnessCache}.
     * When done we return {@code badnessCache} to the caller.
     *
     * @return a value indicating how far from a perfect trisection this ClockDataItem is
     */
    public double doBadness() {
        doPieSlices();
        double badnessValue = 0.0;
        for (double slice : pieSlices) {
            badnessValue += Math.abs(120.0 - slice);
        }
        return badnessValue;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object. We just return the value returned by the {@code compare} method of {@code double}
     * returns when passed the doBadness value of our {@code ClockDataItem} and the doBadness value of
     * our parameter {@code ClockDataItem o}.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@NonNull ClockDataItem o) {
        return  Double.compare(this.badness, o.badness);
    }

    /**
     * Calculates the angular sizes of the three pie slices of the clock face. If our cache of pie
     * slices in {@code double[] pieSlices} is null we initialize it with a new instance of
     * 3 {@code double} objects, initialize {@code double[] angles} with the ordered angles returned
     * by our method {@code orderedAngles} then set {@code pieSlices[0]} to {@code angles[1]}
     * minus {@code angles[0]}, set {@code pieSlices[1]} to {@code angles[2]} minus {@code angles[1]},
     * and set {@code pieSlices[2]} to 360.0 minus {@code angles[2]} plus {@code angles[0]}.
     * Finally we return {@code pieSlices} to the caller.
     */
    public void doPieSlices() {
        double pie1, pie2, pie3;
        if (angleHour < angleMinute && angleHour < angleSecond) {
            pie1 = angleHour;
            if (angleMinute < angleSecond) {
                pie2 = angleMinute;
                pie3 = angleSecond;
            } else {
                pie3 = angleMinute;
                pie2 = angleSecond;
            }
        } else if (angleMinute < angleSecond && angleMinute < angleHour) {
            pie1 = angleMinute;
            if (angleSecond < angleHour) {
                pie2 = angleSecond;
                pie3 = angleHour;
            } else {
                pie3 = angleSecond;
                pie2 = angleHour;
            }
        } else {
            pie1 = angleSecond;
            if (angleMinute < angleHour) {
                pie2 = angleMinute;
                pie3 = angleHour;
            } else {
                pie3 = angleMinute;
                pie2 = angleHour;
            }
        }
        pieSlices[0] = pie2 - pie1;
        pieSlices[1] = pie3 - pie2;
        pieSlices[2] = (360.0 - pie3) + pie1;
    }

    /**
     * Returns a string representation of our {@code ClockDataItem}. We initialize our variable
     * {@code double[] pie} with the array of pie slices calculated by our method {@code doPieSlices},
     * then initialize {@code String sH} with the string value of {@code timeHour} (adding a leading
     * 0 if it is less than 10), initialize {@code String sM} with the string value of {@code timeMinute}
     * (adding a leading 0 if it is less than 10), and initialize {@code String sS} with the string
     * value of {@code timeSecond} (adding a leading 0 if it is less than 10). Finally we return the
     * string formed by concatenating {@code sH} followed by the ":" character, followed by {@code sM}
     * followed by the ":" character, {@code sS} followed by the newline character, followed by the
     * string value of {@code pie[0]} followed by the newline character, followed by the string value
     * of {@code pie[1]} followed by the newline character, followed by the string value of {@code pie[2]}
     * followed by the newline character, followed by the string value of the doBadness value returned
     * by our {@code doBadness} method followed by the string " Badness".
     *
     * @return a string representation of our {@code ClockDataItem}.
     */
    @NonNull
    @Override
    public String toString() {
        String sH = timeHour < 10 ? "0" + timeHour : "" + timeHour;
        if(sH.equals("00")) sH = "12";
        String sM = timeMinute < 10 ? "0" + timeMinute: "" + timeMinute;
        String sS = timeSecond < 10 ? "0" + timeSecond : "" + timeSecond;
        return sH + ":" + sM + ":" + sS + "\n"
                + pieSlices[0] + "\n" + pieSlices[1] + "\n" + pieSlices[2] + "\n"
                + badness + " Badness";
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
