package com.example.android.markovchain

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * This activity is useful to benchmark two different implementations of a method
 */
@Suppress("MemberVisibilityCanBePrivate")
class TestSplit : AppCompatActivity() {
    /**
     * [LinearLayout] with id R.id.progress_view_linear_layout that contains our UI widgets:
     * the [ProgressBar] field [vProgressBar], the two start Buttons, the [Button] field [vAbortButton]
     * "ABORT", the [EditText] field [vProgressSteps], and the [EditText] field [vIterationsPerStep].
     * It shares a `FrameLayout` with the [LinearLayout] field [vResultsLinearLayout] and starts out
     * VISIBLE then switches to GONE when a benchmark finishes so that the results can be seen.
     */
    lateinit var vProgressLayout: LinearLayout
    /**
     * [ProgressBar] in our layout used to show the progress of our benchmark.
     */
    lateinit var vProgressBar: ProgressBar
    /**
     * [Button] used to start version one of code
     */
    lateinit var vStartButtonOne: Button
    /**
     * [Button] used to start version two of code
     */
    lateinit var vStartButtonTwo: Button
    /**
     * [Button] currently used to `finish()` this `Activity`
     */
    lateinit var vAbortButton: Button

    /**
     * [EditText] in layout used to change [mProgressSteps]
     */
    lateinit var vProgressSteps: EditText
    /**
     * [EditText] in layout used to change [mIterationsPerStep]
     */
    lateinit var vIterationsPerStep: EditText

    /**
     * Number of steps in the [ProgressBar] field [vProgressBar]
     */
    var mProgressSteps: Long = 100L
    /**
     * Number of repetitions per [ProgressBar] step.
     */
    var mIterationsPerStep: Long = 100L

    /**
     * [LinearLayout] with id R.id.results_linear_layout in our layout file that contains
     * the [TextView] field [vResults], and the [Button] field [vTryAgain]. It shares a `FrameLayout`
     * with the [LinearLayout] field [vProgressLayout] and starts out GONE then switches to VISIBLE
     * when a benchmark finishes so that the results displayed in the [TextView] field [vResults]
     * can be seen.
     */
    lateinit var vResultsLinearLayout: LinearLayout
    /**
     * [TextView] used to display results
     */
    lateinit var vResults: TextView
    /**
     * [Button] in the [vResultsLinearLayout] field that "returns" us to [vProgressLayout]
     */
    lateinit var vTryAgain: Button

    /**
     * Instance of [ControlClass] that is currently being used
     */
    lateinit var mControlInstance: ControlClass

    val testString: String =
            "from fairest creatures we desire increase " +
            "that thereby beauty's rose might never die " +
            "but as the riper should by time decease " +
            "his tender heir might bear his memory " +
            "but thou contracted to thine own bright eyes " +
            "feed'st thy light's flame with self-substantial fuel " +
            "making a famine where abundance lies " +
            "thy self thy foe to thy sweet self too cruel " +
            "thou that art now the world's fresh ornament " +
            "and only herald to the gaudy spring " +
            "within thine own bud buriest thy content " +
            "and tender churl mak'st waste in niggarding " +
            "pity the world or else this glutton be " +
            "to eat the world's due by the grave and thee. "

    var testOutput: MutableList<String> = ArrayList(20)
    var testOutputList: List<String> = ArrayList()

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * `onCreate`, then we set our content view to our layout file activity_test_bench_mark.
     * We initialize our [ProgressBar] field [vProgressBar] by finding the view with id
     * R.id.progress_horizontal, initialize our [Button] field [vStartButtonOne] by finding the
     * view with id R.id.start_one and set its `OnClickListener` to an a lambda that will run the
     * first method's benchmark ([ControlClass1]) when the `Button` is clicked, initialize our [Button]
     * field [vStartButtonTwo] by finding the view with id R.id.start_two and set its `OnClickListener`
     * to an a lambda that will run the second method's benchmark ([ControlClass2]). We then initialize
     * our [Button] field [vAbortButton] by finding the view with id R.id.abort and set its
     * `OnClickListener` to an a lambda which calls [finish] to stop this `Activity` and return to
     * [MainActivity].
     * TODO: change to just stop current benchmark
     *
     * We initialize our [EditText] field [vProgressSteps] (the [EditText] used to change the maximum
     * number of steps in the [ProgressBar]) by finding the view with the id R.id.progress_steps and
     * set its text to the [String] value of the current value of our [Long] field [mProgressSteps],
     * and we initialize our [EditText] field [vIterationsPerStep] (the [EditText] used to change the
     * number of iterations per progress step) by finding the view with id R.id.iterations_per_step
     * and set its text to the [String] value of the current value of our [Long] field
     * [mIterationsPerStep].
     *
     * We initialize our [LinearLayout] field [vProgressLayout] by finding the view with id
     * R.id.progress_view_linear_layout, and our [LinearLayout] field [vResultsLinearLayout] by
     * finding the view with id R.id.results_linear_layout (we need these to use later to swap the
     * visibility of these two views). We initialize our [Button] field [vTryAgain] by finding
     * the view with id R.id.try_again ("TRY AGAIN") and set its `OnClickListener` to an a lambda
     * that will swap the visibility from the results [LinearLayout] field [vResultsLinearLayout]
     * to the progress [LinearLayout] field [vProgressLayout] and set the "progress" of the [ProgressBar]
     * field [vProgressBar] back to 0. We then initialize our [TextView] field [vResults] (the
     * [TextView] for the results of the benchmark) by finding the view with id R.id.results_view
     * (the current benchmark results will be appended to it).
     *
     * @param savedInstanceState always null since [onSaveInstanceState] is not overridden
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bench_mark)

        vProgressBar = findViewById(R.id.progress_of_benchmark)
        vStartButtonOne = findViewById(R.id.start_one)
        /**
         * Called when the "START ONE" `Button` is clicked. First we log the fact that we
         * were clicked. Then we call our `updateIterationValues()` method to read the
         * value entered in `EditText vProgressSteps` to update the number of steps for
         * the `ProgressBar` storing it in our field `mProgressSteps` and the value
         * entered in `EditText vIterationsPerStep` to update `mIterationsPerStep`.
         * We then initialize our field `ControlClass mControlInstance` with an instance
         * of `ControlClass1` and call its `execute` method to have it run its
         * `testMethod` override `mIterationsPerStep` times `mProgressSteps`
         * times updating the `ProgressBar` by one step every `mIterationsPerStep`
         * iterations.
         *
         * Parameter: `View` that was clicked
         */
        vStartButtonOne.setOnClickListener {
            timesCalled = 0
            Log.i(TAG, "Start button clicked")
            updateIterationValues()
            mControlInstance = ControlClass1()
            mControlInstance.execute(mIterationsPerStep, mProgressSteps)
        }
        vStartButtonTwo = findViewById(R.id.start_two)
        /**
         * Called when the "START TWO" `Button` is clicked. First we log the fact that we
         * were clicked. Then we call our `updateIterationValues()` method to read the
         * value entered in `EditText vProgressSteps` to update the number of steps for
         * the `ProgressBar` storing it in our field `mProgressSteps` and the value
         * entered in `EditText vIterationsPerStep` to update `mIterationsPerStep`.
         * We then initialize our field `ControlClass mControlInstance` with an instance
         * of `ControlClass1` and call its `execute` method to have it run its
         * `testMethod` override `mIterationsPerStep` times `mProgressSteps`
         * times updating the `ProgressBar` by one step every `mIterationsPerStep`
         * iterations.
         *
         * Parameter: `View` that was clicked
         */
        vStartButtonTwo.setOnClickListener {
            timesCalled = 0
            Log.i(TAG, "Start button clicked")
            updateIterationValues()
            mControlInstance = ControlClass2()
            mControlInstance.execute(mIterationsPerStep, mProgressSteps)
        }
        vAbortButton = findViewById(R.id.abort)
        /**
         * Called when the "ABORT" `Button` is clicked. First we log the fact that we were
         * clicked, then we call the `finish()` method to close this `Activity`.
         *
         * Parameter: `View` that was clicked.
         */
        vAbortButton.setOnClickListener {
            Log.i(TAG, "Abort button clicked")
            finish()
        }

        vProgressSteps = findViewById(R.id.progress_steps)
        vProgressSteps.setText(mProgressSteps.toString())
        vIterationsPerStep = findViewById(R.id.iterations_per_step)
        vIterationsPerStep.setText(mIterationsPerStep.toString())

        vProgressLayout = findViewById(R.id.progress_view_linear_layout)
        vResultsLinearLayout = findViewById(R.id.results_linear_layout)

        vTryAgain = findViewById(R.id.try_again)
        /**
         * Called when the "TRY AGAIN" `Button` is clicked. We just set the visibility of
         * `LinearLayout vResultsLinearLayout` to GONE, and the visibility of
         * `LinearLayout vProgressLayout` to VISIBLE.
         *
         * Parameter: `View` that was clicked.
         */
        vTryAgain.setOnClickListener {
            vResultsLinearLayout.visibility = View.GONE
            vProgressLayout.visibility = View.VISIBLE
            vProgressBar.progress = 0
        }

        vResults = findViewById(R.id.results_view)
    }

    /**
     * This method reads the text in the [EditText] field [vProgressSteps], converts that [String]
     * to [Long] and uses that value to update the contents of our [ProgressBar] field [mProgressSteps],
     * and also uses it to set the max number of steps for [vProgressBar]. It then reads the text in
     * our [EditText] field [vIterationsPerStep], converts it to [Long] and uses that value to update
     * the contents of our field [mIterationsPerStep]. These two values are used as arguments to the
     * `execute` method of our current [ControlClass] field [mControlInstance].
     */
    private fun updateIterationValues() {
        mProgressSteps = java.lang.Long.parseLong(vProgressSteps.text.toString())
        vProgressBar.max = mProgressSteps.toInt()
        mIterationsPerStep = java.lang.Long.parseLong(vIterationsPerStep.text.toString())
    }

    /**
     * This class should be extended by classes which wish to benchmark their code in the
     * overridden method `testMethod()`.
     */
    @SuppressLint("StaticFieldLeak")
    open inner class ControlClass : CalcTask() {
        /**
         * Runs on the UI thread after `doInBackground(Long...)`. The [Long] parameter [result]
         * is the value returned by `doInBackground(Long...)`. We override this to make use of
         * the elapsed time value it returned.
         *
         * First we call through to our super's implementation of `onPostExecute`. Then we format
         * a [String] variable `val formattedIterations` to display the number of iterations just
         * performed ([mProgressSteps] times [mIterationsPerStep]), and format a [String] variable
         * `val formattedResult` to display our [Long] parameter [result] (total benchmark time in
         * milliseconds). We then append a [String] containing those two strings to our [TextView]
         * field [vResults]. Finally we set the visibility of our [LinearLayout] field [vProgressLayout]
         * to GONE and the visibility of our [LinearLayout] field [vResultsLinearLayout] to VISIBLE
         * in order to see the results displayed.
         *
         * @param result The elapsed time the benchmark took.
         */
        override fun onPostExecute(result: Long?) {
            super.onPostExecute(result)
            Log.i(TAG, "Benchmark took $result milliseconds")
            val formattedIterations = NumberFormat.getNumberInstance(Locale.US).format(mProgressSteps * mIterationsPerStep)
            val formattedResult = NumberFormat.getNumberInstance(Locale.US).format(result)
            vResults.append("Executed $formattedIterations times in\n$formattedResult milliseconds\n")
            vProgressLayout.visibility = View.GONE
            vResultsLinearLayout.visibility = View.VISIBLE
        }

        /**
         * Runs on the UI thread after `publishProgress(Long...)` is invoked. The [Long] parameter
         * [progress] is the value passed to `publishProgress(Long...)`. We override this to advance
         * our progress bar.
         *
         * First we call through to our super's implementation of `onProgressUpdate`, then we
         * set the progress of the [ProgressBar] field [vProgressBar] to the integer value of our
         * parameter.
         *
         * @param progress The values indicating progress.
         */
        override fun onProgressUpdate(vararg progress: Long?) {
            vProgressBar.progress = progress[0]!!.toInt()
        }
    }

    /**
     * This is a simple example use of ControlClass designed to benchmark division.
     */
    @SuppressLint("StaticFieldLeak")
    private inner class ControlClass1 : ControlClass() {
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark. Here we just divide our Accumulator register by our Divisor
         * register.
         */
        override fun testMethod() {
            testOutputList = testString
                    .split(" ".toRegex())
                    .dropLastWhile { it.isEmpty() }
            if (timesCalled % 100000 == 0) {
                Log.i(TAG, "Results of split: $testOutputList")
            }
        }
    }

    /**
     * This is a simple example use of ControlClass designed to benchmark multiplication.
     */
    @SuppressLint("StaticFieldLeak")
    private inner class ControlClass2 : ControlClass() {


        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark. Here we just multiply our Accumulator register by our
         * Multiplicand register
         */
        override fun testMethod() {
            var startIndex = 0
            var endIndex = 0
            for(i in testString.indices) {
                if (testString[i] == ' ') {
                    val word: String = testString.substring(startIndex, endIndex+1)
                    testOutput.add(word)
                    startIndex = i
                }
                endIndex = i
            }
            if (startIndex != endIndex) {
                testOutput.add(testString.substring(startIndex))
            }
            if (timesCalled % 100000 == 0) {
                Log.i(TAG, "Results of split: $testOutput")
            }
            if (testOutput.size > 20) testOutput = ArrayList(20)
        }
    }

    companion object {
        /**
         * TAG used for logging
         */
        internal const val TAG = "TestSplit"

        internal var timesCalled = 0
    }
}
