package com.example.android.markovchain

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.text.NumberFormat
import java.util.*

/**
 * Searches for the time of day when the hands of a clock comes closest to trisecting the face of
 * the clock.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ClockTrisect : Activity() {
    /**
     * Amount to increment seconds by for each trial [ClockDataItem]
     */
    var increment = 1.0
    /**
     * The precision needed to format the current [increment]
     */
    var incrementPrecision = 0
    /**
     * [LinearLayout] we add our results to
     */
    lateinit var outputLinearLayout: LinearLayout
    /**
     * The `AsyncTask` which does all our calculations
     */
    lateinit var clockDataTask: ClockDataTask
    /**
     * The [BenchMark] which times how long it takes to do all our calculations
     */
    lateinit var benchMark: BenchMark
    /**
     * The array of [ClockDataItem] objects with the best trisection for each minute on the clock
     */
    var minuteBestClock: Array<ClockDataItem?>? = null
    /**
     * The size of the clock face pie chart in pixels we are to display as part of our output.
     */
    var mClockSize: Int = 0


    /**
     * Called when the [Activity] is starting. First we call our super's implementation of
     * `onCreate`, then we set our content view to our layout file R.layout.activity_clock_trisect.
     * We initialize our [Int] field [mClockSize] by using the logical density of the display to
     * scale our constant CLOCK_SIZE_DIP to pixels. We initialize our [LinearLayout] field
     * [outputLinearLayout] by finding the view with id R.id.linear_layout, and our [Button]
     * variable `val button` by finding the view with id R.id.start_the_clock ("Start the clock").
     * Finally we set the `OnClickListener` of `button` to an a lambda whose `onClick` override
     * calls our method [createClockDataTask] to initialize our [ClockDataTask] field [clockDataTask],
     * initializes our [BenchMark] field [benchMark] (starting its clock) then calls the `execute`
     * method of `clockDataTask` to start it running (its `doInBackground` method will be called
     * with the value of the [minuteBestClock] array of [ClockDataItem]'s as its parameter).
     *
     * @param savedInstanceState we do not override [onSaveInstanceState] so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_trisect)

        mClockSize = (CLOCK_SIZE_DIP * resources.displayMetrics.density + 0.5f).toInt()

        outputLinearLayout = findViewById(R.id.linear_layout)
        val button = findViewById<Button>(R.id.start_the_clock)
        /**
         * Called when our [Button] `val button` has been clicked. We set the static field
         * `secondFormat` of the class [ClockDataItem] to a string formed by concatenating the
         * string "%0" followed by 2 plus the current value of our field [incrementPrecision]
         * followed by the string "." followed by the current value of our field [incrementPrecision]
         * followed by the string "f" (this is the string that the `toString` method of
         * [ClockDataItem] will use to format its `timeSecond` field). We then call our method
         * [createClockDataTask] to construct and initialize our [ClockDataTask] field [clockDataTask].
         * We divide our [Double] field [increment] by 10, increment our [Int] field [incrementPrecision],
         * initialize our [BenchMark] field [benchMark] with a new instance (starting its clock) then
         * call the `execute` method of [clockDataTask] with our field `ClockDataItem[] minuteBestClock`
         * as the parameter to be passed to its `doInBackground` method.
         *
         * Parameter: The `View` that was clicked.
         */
        button.setOnClickListener {
            ClockDataItem.secondFormat = "%0" + (incrementPrecision + 2) + "." + incrementPrecision + "f"
            createClockDataTask()
            increment /= 10.0     // TODO: change button's label
            incrementPrecision++
            benchMark = BenchMark()
            clockDataTask.execute(minuteBestClock!!)
        }
    }

    /**
     * Adds a [TextView] containing the [String] parameter [text] to the [ViewGroup] parameter [parent].
     * First we initialize our [TextView] variable `val textView` with a new instance, then we set
     * the text of `textView` to our [String] parameter [text], and set the left drawable of `textView`
     * to the [mClockSize] by [mClockSize] pie chart created by the `clockFace` method of [clockTime],
     * and finally we add `textView` to our [ViewGroup] parameter [parent] (the vertical [LinearLayout]
     * in our UI) at index 0 (the top).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param clockTime the `ClockDataItem` that the `TextView` represents.
     * @param parent ViewGroup to add our TextView to
     */
    fun addText(text: String, clockTime: ClockDataItem, parent: ViewGroup) {
        val textView = TextView(this)
        textView.text = text
        textView.setCompoundDrawables(clockTime.clockFace(resources, mClockSize), null, null, null)
        parent.addView(textView, 0)
    }

    /**
     * Initializes our [ClockDataTask] field [clockDataTask] to an anonymous [ClockDataTask] for the
     * current value of our [Double] field [increment], overriding its `onPreExecute`, `onPostExecute`,
     * and `onProgressUpdate` methods. If our [ClockDataItem] array field [minuteBestClock] is null,
     * we initialize it with the array returned by the `init` method of [clockDataTask].
     */
    @SuppressLint("StaticFieldLeak")
    fun createClockDataTask() {
        Log.i(TAG, "Trying an increment of $increment")
        clockDataTask = object : ClockDataTask(increment) {
            /**
             * Runs on the UI thread before [doInBackground]. We call the `removeAllViews` method
             * of our [LinearLayout] field [outputLinearLayout] to have it remove all of its child
             * views.
             */
            override fun onPreExecute() {
                outputLinearLayout.removeAllViews()
            }

            /**
             * Runs on the UI thread after [doInBackground]. Our [ClockDataItem] parameter
             * [aClockDataItem] is the value returned by [doInBackground]. We initialize our [String]
             * variable `val benchResult` with the Locale.US formatted number string of the `long`
             * value (elapsed time) returned by the `stop` method of our [BenchMark] field [benchMark].
             * We then call our [addText] method to have it display a [TextView] at the top of our
             * [TextView] field [outputLinearLayout] whose text consists of the string formed by
             * concatenating the string "Final Result: " followed by `benchResult` followed by the
             * string " milliseconds\n", followed by the string value of our parameter [aClockDataItem]
             * followed by the newline character.
             *
             * @param aClockDataItem The result of the operation computed by [.doInBackground].
             */
            override fun onPostExecute(aClockDataItem: ClockDataItem) {
                val benchResult = NumberFormat.getNumberInstance(Locale.US).format(benchMark.stop())
                addText("Final Result: $benchResult milliseconds\n$aClockDataItem\n",
                        aClockDataItem, outputLinearLayout)
            }

            /**
             * Runs on the UI thread after [publishProgress] is invoked. The specified values
             * are the values passed to [publishProgress]. We call our [addText] method to have
             * it display a [TextView] at the top of our [TextView] field [outputLinearLayout]
             * whose text consists of the string formed by concatenating the string value of our
             * [ClockDataItem] parameter `values[0]` followed by a newline character.
             *
             * @param values The [ClockDataItem] with the best "badness" for the hour just tried.
             */
            override fun onProgressUpdate(vararg values: ClockDataItem) {
                addText(values[0].toString() + "\n",
                        values[0], outputLinearLayout)
//                Log.i(TAG, "Posting Hourly best for: " + values[0].timeHour)
            }
        }
        if (minuteBestClock == null) {
            minuteBestClock = clockDataTask.init()
        }
    }

    /**
     * Our static constants.
     */
    companion object {
        /**
         * TAG used for logging.
         */
        internal const val TAG = "ClockTrisect"

        /**
         * The size of the clock face pie chart in DIPs we are to display as part of our output.
         */
        private const val CLOCK_SIZE_DIP = 100
    }
}
