package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.common.CalcTask;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This activity is useful to benchmark two different implementations of a method
 */
public class TestBenchMark extends Activity {
    String TAG = "TestBenchMark"; // TAG used for logging

    /**
     * LinearLayout that contains <b>ProgressBar vProgressBar</b>, the two start Buttons, the "ABORT"
     * button, <b>EditText vProgressSteps</b>, and <b>EditText vIterationsPerStep</b>. It shares a
     * FrameLayout with <b>LinearLayout vResultsLinearLayout</b> and starts out VISIBLE then switches
     * to GONE when a benchmark finishes so that the results can be seen.
     */
    LinearLayout vProgressLayout;
    ProgressBar vProgressBar; // ProgressBar in our layout used to show progress
    Button vStartButtonOne; // Button used to start version one of code
    Button vStartButtonTwo; // Button used to start version two of code
    Button vAbortButton; // Button currently used to finish() this Activity
    EditText vProgressSteps; // EditText in layout used to change mProgressSteps
    EditText vIterationsPerStep; // EditText in layout used to change mIterationsPerStep

    Long mProgressSteps = 100L; // Number of steps in ProgressBar
    Long mIterationsPerStep = 10000L; // Number of repetitions per ProgressBar step.

    /**
     * LinearLayout that contains <b>TextView vResults</b>, and <b>Button vTryAgain</b>. It shares
     * a FrameLayout with <b>LinearLayout vProgressLayout</b> and starts out GONE then switches to
     * VISIBLE when a benchmark finishes so that the results displayed in <b>TextView vResults</b>
     * can be seen.
     */
    LinearLayout vResultsLinearLayout;
    TextView vResults; // TextView used to display results
    Button vTryAgain; // Button in vResultsLinearLayout that "returns" us to vProgressLayout

    ControlClass mControlInstance; // Instance of ControlClass that is currently being used

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, then we set the content view to the layout activity_test_bench_mark. Next we locate
     * the ProgressBar R.id.progress_horizontal and squirrel it away for later use, locate the
     * Button R.id.start_one in the layout, and set the OnClickListener of the Button to an anonymous
     * class that will run the first method's benchmark when the Button is clicked, locate the Button
     * R.id.start_two in the layout, and set the OnClickListener of the Button to an anonymous class
     * that will run the second method's benchmark. We then locate the Button R.id.abort and set its
     * OnClickListener to an anonymous class which calls <b>Activity.finish()</b> to stop this
     * Activity and return to <b>MainActivity</b>. TODO: change to just stop current benchmark
     * We locate the EditText used to change the maximum number of steps in the ProgressBar
     * (R.id.progress_steps) in order to initialize our field <b>EditText vProgressSteps</b> and
     * set the text of <b>vProgressSteps</b> to the String value of the current value of our field
     * <b>Long mProgressSteps</b>, and we locate the EditText used to change the number of iterations
     * per progress step (R.id.iterations_per_step) to initialize our field <b>EditText vIterationsPerStep</b>
     * and set the text of <b>vIterationsPerStep</b> to the String value of the current value of our field
     * <b>Long mIterationsPerStep</b>.
     * <p>
     * We locate the LinearLayout containing the ProgressBar (R.id.progress_view_linear_layout)
     * and the LinearLayout containing the result View (R.id.results_linear_layout) and squirrel
     * them away in vProgressLayout, and vResultsLinearLayout to use later to swap visibility of
     * these two views. We locate the "TRY AGAIN" button in the results LinearLayout and set its
     * OnClickListener to an anonymous class that will swap the visibility from the results
     * LinearLayout to the progress LinearLayout. We then locate the TextView for the results of
     * the benchmark (R.id.results_view) and squirrel it away in vResults for use by the benchmarking
     * to append the output of the current benchmark results.
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bench_mark);

        vProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        vStartButtonOne = (Button) findViewById(R.id.start_one);
        vStartButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                updateIterationValues();
                mControlInstance = new ControlClass1();
                mControlInstance.execute(mIterationsPerStep, mProgressSteps);
            }
        });
        vStartButtonTwo = (Button) findViewById(R.id.start_two);
        vStartButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                updateIterationValues();
                mControlInstance = new ControlClass2();
                mControlInstance.execute(mIterationsPerStep, mProgressSteps);
            }
        });
        vAbortButton = (Button) findViewById(R.id.abort);
        vAbortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Abort button clicked");
                finish();
            }
        });

        vProgressSteps = (EditText) findViewById(R.id.progress_steps);
        vProgressSteps.setText(mProgressSteps.toString());
        vIterationsPerStep = (EditText) findViewById(R.id.iterations_per_step);
        vIterationsPerStep.setText(mIterationsPerStep.toString());

        vProgressLayout = (LinearLayout) findViewById(R.id.progress_view_linear_layout);
        vResultsLinearLayout = (LinearLayout) findViewById(R.id.results_linear_layout);

        vTryAgain = (Button) findViewById(R.id.try_again);
        vTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vResultsLinearLayout.setVisibility(View.GONE);
                vProgressLayout.setVisibility(View.VISIBLE);
            }
        });

        vResults = (TextView) findViewById(R.id.results_view);
    }

    /**
     * This method reads the text in the <b>EditText vProgressSteps</b>, converts it to Long in order
     * to update the value of our field <b>Long mProgressSteps</b>, the uses it to set the max number
     * of steps for our <b>ProgressBar vProgressBar</b>, and then reads the text in the
     * <b>EditText vIterationsPerStep</b>, converts it to Long in order to update the value of our
     * field <b>Long mIterationsPerStep</b>. These two Long values are used as arguments to the
     * benchmarking framework (as arguments to ControlInstance.execute).
     */
    private void updateIterationValues() {
        mProgressSteps = Long.parseLong(String.valueOf(vProgressSteps.getText()));
        vProgressBar.setMax(mProgressSteps.intValue());
        mIterationsPerStep = Long.parseLong(String.valueOf(vIterationsPerStep.getText()));
    }

    /**
     * This class should be extended by classes which wish to benchmark their code in the
     * overridden method testMethod().
     */
    private class ControlClass extends CalcTask {

        @SuppressLint("SetTextI18n")
        /**
         * Runs on the UI thread after doInBackground(Long...).
         * The specified result is the value returned by doInBackground(Long...).
         * This method won't be invoked if the task was cancelled.
         * Override this to make use of the elapsed time value returned.
         *
         * First we call through to our super's implementation of onPostExecute. Then we format a
         * String for the number of iterations performed (mProgressSteps * mIterationsPerStep), and
         * format a String of our parameter <b>Long result</b> (total benchmark time in milliseconds).
         * We then append a formatted String containing those two strings to <b>TextView vResults</b>.
         * Finally we set the visibility of <b>LinearLayout vProgressLayout</b> to GONE and the
         * visibility of <b>LinearLayout vResultsLinearLayout</b> to VISIBLE in order to see the
         * results displayed.
         *
         * @param result The elapsed time the benchmark took.
         */
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(TAG, "Benchmark took " + result + " milliseconds");
            String formattedIterations = NumberFormat.getNumberInstance(Locale.US).format(mProgressSteps * mIterationsPerStep);
            String formattedResult = NumberFormat.getNumberInstance(Locale.US).format(result);
            vResults.append("Executed " + formattedIterations + " times in\n" + formattedResult + " milliseconds\n");
            vProgressLayout.setVisibility(View.GONE);
            vResultsLinearLayout.setVisibility(View.VISIBLE);
        }

        /**
         * Runs on the UI thread after publishProgress(Long...) is invoked.
         * The specified values are the values passed to publishProgress(Long...).
         * Override this to advance a progress bar
         * <p>
         * First we call through to our super's implementation of onProgressUpdate, then we set the
         * <b>ProgressBar vProgressBar</b> progress to the integer value of our parameter.
         *
         * @param progress The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Long... progress) {
            super.onProgressUpdate(progress);
            vProgressBar.setProgress(progress[0].intValue());
        }
    }

    /**
     * This is a simple example use of ControlClass designed to benchmark division.
     */
    private class ControlClass1 extends ControlClass {
        double acc = 1.000000001; // Accumulator register for repeated divisions
        double div = 0.999999999; // Divisor register for repeated divisions

        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark. Here we just divide our Accumulator register by our Divisor
         * register.
         */
        @Override
        public void testMethod() {
            acc = acc / div;
        }
    }

    /**
     * This is a simple example use of ControlClass designed to benchmark multiplication.
     */
    private class ControlClass2 extends ControlClass {
        double acc = 1.000000001; // Accumulator register for repeated multiplications
        double mul = 0.999999999; // Multiplicand register for repeated multiplications

        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark. Here we just multiply our Accumulator register by our
         * Multiplicand register
         */
        @Override
        public void testMethod() {
            acc = acc * mul;
        }
    }
}
