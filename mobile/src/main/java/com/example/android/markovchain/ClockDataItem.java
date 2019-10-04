package com.example.android.markovchain;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.util.Formatter;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataItem implements Comparable<ClockDataItem> {

    /**
     * Format string to use for the formatting of our {@code double timeSecond} field.
     */
    public static String secondFormat = "%06.4f";

    /**
     * {@code Paint} that our {@code clockFace} method uses to draw the {@code BitmapDrawable} pie
     * chart of the clock face for the time value that we hold
     */
    public static Paint mPaint = new Paint();
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
     * How bad is our trisection (the sum of the absolute values when each {@code double[] pieSlice}
     * is compared to a perfect trisection of 120 degrees)
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

    /**
     * Initializes the time of day fields of this instance to its parameters, and calculates the
     * derived fields from the values of the these fields. We save our parameter {@code int hour} in
     * our field {@code int timeHour}, our parameter {@code int minute} in our field {@code int timeMinute},
     * and our parameter {@code double second} in our field {@code double timeSecond}. Then we calculate
     * the angle of the second hand {@code double angleSecond} to be 6.0 times {@code second}, the angle
     * of the minute hand {@code double angleMinute} to be 6.0 times the quantity {@code minute} plus
     * 1 sixtieth of {@code second}, and the angle of the hour hand {@code double angleHour} to be
     * 30.0 times the quantity {@code hour} plus 1 sixtieth of {@code minute} plus 1 thirty-six hundredth
     * of {@code second}. We then set our field {@code double badness} to the value returned by our
     * method {@code doBadness} ({@code doBadness} calls our method {@code doPieSlices} which initializes
     * our field {@code double[] pieSlices}).
     *
     * @param hour the hour we are to represent
     * @param minute the minute we are to represent
     * @param second the second we are to represent
     */
    private void init(int hour, int minute, double second) {
        timeHour = hour;
        timeMinute = minute;
        timeSecond = second;
        angleSecond = 6.0 * second;
        angleMinute = 6.0 * (minute + second/60.0);
        angleHour = 30.0 * (hour + minute/60.0 + second/3600.0);
        badness = doBadness();
    }

    /**
     * Copies the values of all the fields of its parameter {@code ClockDataItem mom} to our fields
     * (deep copy).
     *
     * @param mom the {@code ClockDataItem} whose fields we are to copy to ours.
     */
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
     * clock hands misses the perfect trisection: (120,120,120). First we call our method {@code doPieSlices}
     * to initialize our field {@code double[] pieSlices} to the size of the pie slices created by the
     * angles of the clock hands. Then we initialize our variable {@code badnessValue} to 0.0 and loop
     * through all the {@code double slice} values in {@code pieSlices} adding the absolute value of
     * the difference between 120.0 and {@code slice} to {@code badnessValue}. When done we return
     * {@code badnessValue} to the caller.
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
     * object. We just return the value that the {@code compare} method of {@code Double} returns
     * when passed the {@code badness} field of our {@code ClockDataItem} and the {@code badness}
     * field of our parameter {@code ClockDataItem o}.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *          is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@NonNull ClockDataItem o) {
        return Double.compare(this.badness, o.badness);
    }

    /**
     * Calculates the angular sizes of the three pie slices of the clock face. First we declare our
     * variables {@code double angle1}, {@code double angle2}, and {@code double angle3} (these will
     * be the clockwise-ordered angles of the three clock hands). Then we fall through a complex if
     * else gauntlet which determines which of the clock hand angles {@code angleSecond}, {@code angleMinute}
     * and {@code angleHour} to assign to {@code angle1}, {@code angle2}, and {@code angle3}. We then
     * set {@code pieSlices[0]} to {@code angle2} minus {@code angle1}, set {@code pieSlices[1]} to
     * {@code angle3} minus {@code angle2}, and set {@code pieSlices[2]} to 360.0 minus {@code angle3}
     * plus {@code angle1}. TODO: benchmark the gauntlet versus a library sort of the angles
     */
    public void doPieSlices() {
        double angle1, angle2, angle3;
        if (angleHour < angleMinute && angleHour < angleSecond) {
            angle1 = angleHour;
            if (angleMinute < angleSecond) {
                angle2 = angleMinute;
                angle3 = angleSecond;
            } else {
                angle3 = angleMinute;
                angle2 = angleSecond;
            }
        } else if (angleMinute < angleSecond && angleMinute < angleHour) {
            angle1 = angleMinute;
            if (angleSecond < angleHour) {
                angle2 = angleSecond;
                angle3 = angleHour;
            } else {
                angle3 = angleSecond;
                angle2 = angleHour;
            }
        } else {
            angle1 = angleSecond;
            if (angleMinute < angleHour) {
                angle2 = angleMinute;
                angle3 = angleHour;
            } else {
                angle3 = angleMinute;
                angle2 = angleHour;
            }
        }
        pieSlices[0] = angle2 - angle1;
        pieSlices[1] = angle3 - angle2;
        pieSlices[2] = (360.0 - angle3) + angle1;
    }

    /**
     * Creates a {@code BitmapDrawable} pie chart representing the clock face for the time that we
     * hold that is suitable to use in a call to the {@code setCompoundDrawables} method of a
     * {@code TextView}. First we create a {@code Bitmap bitmap} that is {@code sizeOfBitmap} by
     * {@code sizeOfBitmap} pixels, a {@code RectF oval} that is that size as well, and an
     * {@code Canvas canvas} that will draw into {@code bitmap}. We initialize {@code float startAngle}
     * to be the minimum angle of the angles of our three clock hands, and subtract 90 degrees from
     * that since the {@code drawArc} method of {@code Canvas} considers 0 degrees to be 3 o'clock on
     * a watch. We then proceed to draw the three wedges created by our clock hands, setting the color
     * for the first wedge to RED, and drawing a {@code pieSlices[0]} degree wedge from {@code startAngle},
     * then adding {@code pieSlices[0]} to {@code startAngle} to draw a GREEN wedge that is {@code pieSlices[1]}
     * degrees, and finally adding {@code pieSlices[0]} to {@code startAngle} to draw a BLUE wedge that
     * is {@code pieSlices[2]} in size. We then create {@code BitmapDrawable bmd} from {@code bitmap},
     * set its bounds to be {@code sizeOfBitmap} by {@code sizeOfBitmap} before returning {@code bmd}
     * to the caller.
     *
     * @param resources    Application {@code Resources} instance to allow {@code BitmapDrawable} to
     *                     set the initial target density based on the display metrics of the screen.
     * @param sizeOfBitmap Diameter of the clock face pie chart we are to generate.
     * @return a {@code BitmapDrawable} pie chart clock face for the time value we hold
     */
    public BitmapDrawable clockFace(Resources resources, int sizeOfBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(sizeOfBitmap, sizeOfBitmap, Bitmap.Config.ARGB_8888);
        RectF oval = new RectF(0, 0, sizeOfBitmap, sizeOfBitmap);
        Canvas canvas = new Canvas(bitmap);
        float startAngle = (float) Math.min(angleHour, Math.min(angleMinute, angleSecond));
        startAngle -= 90;

        mPaint.setColor(Color.RED);
        canvas.drawArc(oval, startAngle, (float) pieSlices[0], true, mPaint);
        startAngle += pieSlices[0];
        mPaint.setColor(Color.GREEN);
        canvas.drawArc(oval, startAngle, (float) pieSlices[1], true, mPaint);
        startAngle += pieSlices[1];
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(oval, startAngle, (float) pieSlices[2], true, mPaint);

        BitmapDrawable bmd = new BitmapDrawable(resources, bitmap);
        bmd.setBounds(0, 0, sizeOfBitmap, sizeOfBitmap);
        return bmd;
    }

    /**
     * Returns a string representation of our {@code ClockDataItem}. First we initialize our variable
     * {@code StringBuilder sb} with a new instance, and initialize {@code Formatter formatter} with
     * an instance which will use {@code sb} as the destination {@code StringBuilder} and apply the
     * US {@code Locale} for the localization. If {@code timeHour} is 0 we initialize our variable
     * {@code int tempHour} to 12, otherwise initializing it to {@code timeHour}. We then have
     * {@code formatter} append to {@code sb} the value of {@code tempHour} formatting using the
     * format string "%02d:", append the value of {@code timeMinute} formatting using the format string
     * "%02d:", and append the value of {@code timeSecond} formatting using the format string in our
     * static public field {@code secondFormat}. We then append to {@code sb} a newline character
     * followed by the string value of the three values in our field {@code double[] pieSlices} each
     * terminated by a newline character, followed by the string value of our {@code badness} field
     * and ending by appending the string " Badness". Finally we return the string value of {@code sb}
     * to the caller.
     *
     * @return a string representation of our {@code ClockDataItem}.
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);

        int tempHour = timeHour == 0 ? 12 : timeHour;
        formatter.format("%02d:", tempHour);
        formatter.format("%02d:", timeMinute);
        formatter.format(secondFormat, timeSecond);
        sb.append("\n");
        sb.append(pieSlices[0]).append("\n");
        sb.append(pieSlices[1]).append("\n");
        sb.append(pieSlices[2]).append("\n");
        sb.append(badness).append(" Badness");
        return sb.toString();
    }
}
