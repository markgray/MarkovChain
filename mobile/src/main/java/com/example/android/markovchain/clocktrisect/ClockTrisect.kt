package com.example.android.markovchain.clocktrisect

import com.example.android.markovchain.R
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android.markovchain.benchmark.BenchMark
import java.lang.String.format
import java.text.NumberFormat
import java.util.Locale

/**
 * Searches for the time of day when the hands of a clock comes closest to trisecting the face of
 * the clock.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ClockTrisect : AppCompatActivity() {
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
     * [Button] used to display result sorted by "badness"
     */
    lateinit var mSortedButton: Button
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
    @SuppressLint("DefaultLocale")
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
         * as the parameter to be passed to its `doInBackground` method. Finally we initialize our
         * [Button] field [mSortedButton] by finding the view in our layout with the resource ID
         * R.id.sorted_by_badness and set its `OnClickListener` to a lambda which calls our method
         * [displaySortedResults] to have it clear our result [LinearLayout] field [outputLinearLayout]
         * and redisplay the non-null contents of [minuteBestClock] sorted by the [ClockDataItem]
         * field [ClockDataItem.badness].
         *
         * Parameter: The `View` that was clicked.
         */
        button.setOnClickListener {view ->
            mSortedButton.visibility = View.VISIBLE
            if (increment > SMALLEST_INCREMENT) {
                ClockDataItem.secondFormat = "%0" + (incrementPrecision + 2) + "." + incrementPrecision + "f"
                createClockDataTask()
                changeButtonLabel(view,
                        "Increment of ${ format("%6.5g", increment) } seconds, try smaller?"
                )
                increment /= 10.0
                incrementPrecision++
                benchMark = BenchMark()
                clockDataTask.execute(minuteBestClock!!)
            } else {
                changeButtonLabel(view, "That's all folks, $increment is too small")
                Toast.makeText(
                        view.context,
                        "Significant digits of double reached, sorry.",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
        mSortedButton = findViewById(R.id.sorted_by_badness)
        mSortedButton.setOnClickListener {
            displaySortedResults()
        }
    }

    /**
     * Sets the label of the [Button] whose [View] is [view] to the [String] parameter [label].
     *
     * @param view The [View] of the [Button] whose label we are to set
     * @param label The new label for [view].
     */
    fun changeButtonLabel(view: View, label: String) {
        (view as Button).text = label
    }

    /**
     * Clears the results displayed in our result [LinearLayout] field [outputLinearLayout]
     * and redisplays the non-null contents of [minuteBestClock] sorted in descending order on
     * the [ClockDataItem] field [ClockDataItem.badness].
     */
    fun displaySortedResults() {
        outputLinearLayout.removeAllViews()
        val sortedResults = minuteBestClock!!.filterNotNull().sortedDescending()
        for (value in sortedResults) {
            addText(value.toString(), value, outputLinearLayout)
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
        /**
         * Limit of significant digits of a [Double] value.
         */
        private const val SMALLEST_INCREMENT = 1E-14 - 1E-15
    }
}
