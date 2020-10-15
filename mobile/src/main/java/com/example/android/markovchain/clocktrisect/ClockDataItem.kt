package com.example.android.markovchain.clocktrisect

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable

import java.util.Formatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.min

/**
 * Contains data and methods for holding a time of day, and the corresponding clock hand angles.
 */
class ClockDataItem
/**
 * Our constructor. We just call our method [initClockDataItem] with our parameters.
 *
 * @param hour the hour we represent
 * @param minute minute we represent
 * @param second the second we represent
 */
(hour: Int, minute: Int, second: Double) : Comparable<ClockDataItem> {
    /**
     * The hour of the day we represent
     */
    private var timeHour: Int = 0
    /**
     * The minute of the hour we represent
     */
    private var timeMinute: Int = 0
    /**
     * The second of the minute we represent
     */
    var timeSecond: Double = 0.0
    /**
     * Angle of the hour hand on the face of the clock
     */
    private var angleHour: Double = 0.0
    /**
     * Angle of the minute hand on the face of the clock
     */
    private var angleMinute: Double = 0.0
    /**
     * Angle of the second hand on the face of the clock
     */
    private var angleSecond: Double = 0.0
    /**
     * The sizes of the three clock face pie slices made by the clock hands (120,120,120) is perfect.
     */
    private var pieSlices = DoubleArray(3)
    /**
     * How bad is our trisection (the sum of the absolute values when each `Double[]` `pieSlice`
     * is compared to a perfect trisection of 120 degrees)
     */
    var badness: Double = 0.0

    init {
        initClockDataItem(hour, minute, second)
    }

    /**
     * Sets the time of *this* [ClockDataItem] to its parameters. We just call our method
     * [initClockDataItem] with our parameters and return *this*.
     *
     * @param hour   the hour we represent
     * @param minute minute we represent
     * @param second the second we represent
     * @return this [ClockDataItem] set to the new time
     */
    operator fun set(hour: Int, minute: Int, second: Double): ClockDataItem {
        initClockDataItem(hour, minute, second)
        return this
    }

    /**
     * Initializes the time of day fields of this instance to its parameters, and calculates the
     * derived fields from the values of the these fields. We save our [Int] parameter [hour] in
     * our [Int] field [timeHour], our [Int] parameter [minute] in our [Int] field [timeMinute],
     * and our [Double] parameter [second] in our [Double] field [timeSecond]. Then we calculate
     * the angle of the second hand, our [Double] field [angleSecond] to be 6.0 times [second], the
     * angle of the minute hand our [Double] field [angleMinute] to be 6.0 times the quantity [minute]
     * plus 1 sixtieth of [second], and the angle of the hour hand our [Double] field [angleHour] to
     * be 30.0 times the quantity [hour] plus 1 sixtieth of [minute] plus 1 thirty-six hundredth
     * of [second]. We then set our [Double] field [badness] to the value returned by our method
     * [doBadness] ([doBadness] calls our method [doPieSlices] which initializes our field [Double]
     * array field [pieSlices] with the angles subtended by the three pie slices of the clock face).
     *
     * @param hour the hour we are to represent
     * @param minute the minute we are to represent
     * @param second the second we are to represent
     */
    private fun initClockDataItem(hour: Int, minute: Int, second: Double) {
        timeHour = hour
        timeMinute = minute
        timeSecond = second
        angleSecond = 6.0 * second
        angleMinute = 6.0 * (minute + second / 60.0)
        angleHour = 30.0 * (hour.toDouble() + minute / 60.0 + second / 3600.0)
        badness = doBadness()
    }

    /**
     * Copies the values of all the fields of its [ClockDataItem] parameter [mom] to our fields
     * (deep copy).
     *
     * @param mom the [ClockDataItem] whose fields we are to copy to ours.
     */
    fun clone(mom: ClockDataItem) {
        timeHour = mom.timeHour
        timeMinute = mom.timeMinute
        timeSecond = mom.timeSecond
        angleHour = mom.angleHour
        angleMinute = mom.angleMinute
        angleSecond = mom.angleSecond
        pieSlices[0] = mom.pieSlices[0]
        pieSlices[1] = mom.pieSlices[1]
        pieSlices[2] = mom.pieSlices[2]
        badness = mom.badness
    }

    /**
     * Calculates how far the size of pie slices of the clock face created by the positions of the
     * clock hands misses the perfect trisection: (120,120,120). First we call our method [doPieSlices]
     * to initialize our [Double] array field [pieSlices] to the size of the pie slices created by
     * the angles of the clock hands. Then we initialize our [Double] variable `var badnessValue` to
     * 0.0 and loop through all the [Double] `slice` values in [pieSlices] adding the absolute value
     * of the difference between 120.0 and `slice` to `badnessValue`. When done we return
     * `badnessValue` to the caller.
     *
     * @return a value indicating how far from a perfect trisection this [ClockDataItem] is
     */
    private fun doBadness(): Double {
        doPieSlices()
        var badnessValue = 0.0
        for (slice in pieSlices) {
            badnessValue += abs(120.0 - slice)
        }
        return badnessValue
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object. We just return the value that the `compareTo` method of [Double] returns when passed
     * the [badness] field of our [ClockDataItem] and the [badness] field of our [ClockDataItem]
     * parameter [other].
     *
     * @param   other the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    override fun compareTo(other: ClockDataItem): Int {
        return this.badness.compareTo(other.badness)
    }

    /**
     * Calculates the angular sizes of the three pie slices of the clock face. First we declare our
     * variables `Double angle1`, `Double angle2`, and `Double angle3` (these will be the clockwise
     * ordered angles of the three clock hands). Then we fall through a complex when gauntlet which
     * determines which of the clock hand angles [angleSecond], [angleMinute] and [angleHour] to
     * assign to `angle1`, `angle2`, and `angle3` based on their clockwise order. We then set
     * `pieSlices[0]` to `angle2` minus `angle1`, set `pieSlices[1]` to `angle3` minus `angle2`,
     * and set `pieSlices[2]` to 360.0 minus `angle3` plus `angle1`.
     */
    private fun doPieSlices() {
        val angle1: Double
        val angle2: Double
        val angle3: Double
        when {
            angleHour < angleMinute && angleHour < angleSecond -> {
                angle1 = angleHour
                if (angleMinute < angleSecond) {
                    angle2 = angleMinute
                    angle3 = angleSecond
                } else {
                    angle3 = angleMinute
                    angle2 = angleSecond
                }
            }
            angleMinute < angleSecond && angleMinute < angleHour -> {
                angle1 = angleMinute
                if (angleSecond < angleHour) {
                    angle2 = angleSecond
                    angle3 = angleHour
                } else {
                    angle3 = angleSecond
                    angle2 = angleHour
                }
            }
            else -> {
                angle1 = angleSecond
                if (angleMinute < angleHour) {
                    angle2 = angleMinute
                    angle3 = angleHour
                } else {
                    angle3 = angleMinute
                    angle2 = angleHour
                }
            }
        }
        pieSlices[0] = angle2 - angle1
        pieSlices[1] = angle3 - angle2
        pieSlices[2] = 360.0 - angle3 + angle1
    }

    /**
     * Creates a [BitmapDrawable] pie chart representing the clock face for the time that we
     * hold that is suitable to use in a call to the `setCompoundDrawables` method of a
     * `TextView`. First we construct a [Bitmap] variable `val bitmap` that is [sizeOfBitmap] by
     * [sizeOfBitmap] pixels, a [RectF] variable `val oval` that is that size as well, and an
     * [Canvas] variable `val canvas` that will draw into `bitmap`. We initialize the [Float]
     * variable `var startAngle` to be the minimum angle of the angles of our three clock hands,
     * and subtract 90 degrees from that since the `drawArc` method of [Canvas] considers 0 degrees
     * to be 3 o'clock on a watch. We then proceed to draw the three wedges created by our clock
     * hands, setting the color for the first wedge to RED, and drawing a `pieSlices[0]` degree
     * wedge from `startAngle`, then adding `pieSlices[0]` to `startAngle` to draw a GREEN wedge
     * that is `pieSlices[1]` degrees, and finally adding `pieSlices[0]` to `startAngle` to draw
     * a BLUE wedge that is `pieSlices[2]` in size. We then create the [BitmapDrawable] variable
     * `val bmd` from `bitmap`, set its bounds to be [sizeOfBitmap] by [sizeOfBitmap] and return
     * `bmd` to the caller.
     *
     * @param resources    Application `Resources` instance to allow `BitmapDrawable` to
     * set the initial target density based on the display metrics of the screen.
     * @param sizeOfBitmap Diameter of the clock face pie chart we are to generate.
     * @return a `BitmapDrawable` pie chart clock face for the time value we hold
     */
    fun clockFace(resources: Resources, sizeOfBitmap: Int): BitmapDrawable {
        val bitmap = Bitmap.createBitmap(sizeOfBitmap, sizeOfBitmap, Bitmap.Config.ARGB_8888)
        val oval = RectF(0f, 0f, sizeOfBitmap.toFloat(), sizeOfBitmap.toFloat())
        val canvas = Canvas(bitmap)
        var startAngle = min(angleHour, min(angleMinute, angleSecond)).toFloat()
        startAngle -= 90f

        mPaint.color = Color.RED
        canvas.drawArc(oval, startAngle, pieSlices[0].toFloat(), true, mPaint)
        startAngle += pieSlices[0].toFloat()
        mPaint.color = Color.GREEN
        canvas.drawArc(oval, startAngle, pieSlices[1].toFloat(), true, mPaint)
        startAngle += pieSlices[1].toFloat()
        mPaint.color = Color.BLUE
        canvas.drawArc(oval, startAngle, pieSlices[2].toFloat(), true, mPaint)

        val bmd = BitmapDrawable(resources, bitmap)
        bmd.setBounds(0, 0, sizeOfBitmap, sizeOfBitmap)
        return bmd
    }

    /**
     * Returns a string representation of our [ClockDataItem]. First we initialize our [StringBuilder]
     * variable `val sb` with a new instance, and initialize our [Formatter] variable `var formatter`
     * with an instance which will use `sb` as the destination `StringBuilder` and apply the US
     * `Locale` for the localization. If [timeHour] is 0 we initialize our [Int] variable `val tempHour`
     * to 12, otherwise initializing it to `timeHour`. We then have `formatter` append to `sb` the
     * value of `tempHour` formatting using the format string "%02d:", append the value of [timeMinute]
     * formatting using the format string "%02d:", and append the value of [timeSecond] formatting
     * using the format string in our static public field [secondFormat]. We then append to `sb` a
     * newline character followed by the string value of the three values in our [Double] array field
     * [pieSlices] each terminated by a newline character, followed by the string value of our [badness]
     * field and ending by appending the string " Badness". Finally we return the string value of `sb`
     * to the caller.
     *
     * @return a string representation of our `ClockDataItem`.
     */
    override fun toString(): String {
        val sb = StringBuilder()
        val formatter = Formatter(sb, Locale.US)

        val tempHour = if (timeHour == 0) 12 else timeHour
        formatter.format("%02d:", tempHour)
        formatter.format("%02d:", timeMinute)
        formatter.format(secondFormat, timeSecond)
        sb.append("\n")
        sb.append(pieSlices[0]).append("\n")
        sb.append(pieSlices[1]).append("\n")
        sb.append(pieSlices[2]).append("\n")
        sb.append(badness).append(" Badness")
        return sb.toString()
    }

    /**
     * Our static fields.
     */
    companion object {
        /**
         * Format string for [toString] to use for the formatting of our [Double] field [timeSecond].
         */
        var secondFormat = "%06.4f"

        /**
         * [Paint] that our [clockFace] method uses to draw the [BitmapDrawable] pie
         * chart of the clock face for the time value that we hold
         */
        var mPaint = Paint()
    }
}
