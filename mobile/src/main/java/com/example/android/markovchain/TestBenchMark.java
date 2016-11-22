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
     * LinearLayout that contains <b>ProgressBar mProgressBar</b>, the two start Buttons, and
     * the "ABORT" button. It shares a FrameLayout with <b>LinearLayout mResultsLinearLayout</b>
     * and starts out VISIBLE then switches to GONE when a benchmark finishes so that the results
     * can be seen.
     */
    LinearLayout mProgressLayout;
    Button startButtonOne; // Button used to start version one of code
    Button startButtonTwo; // Button used to start version two of code
    Button abortButton; // Button currently used to finish() this Activity
    ProgressBar mProgressBar; // ProgressBar in our layout used to show progress

    /**
     * LinearLayout that contains <b>TextView mResults</b>, and <b>Button mTryAgain</b>. It shares
     * a FrameLayout with <b>LinearLayout mProgressLayout</b> and starts out GONE then switches to
     * VISIBLE when a benchmark finishes so that the results displayed in <b>TextView mResults</b>
     * can be seen.
     */
    LinearLayout mResultsLinearLayout;
    TextView mResults; // TextView used to display results
    Button mTryAgain; // Button in mResultsLinearLayout that "returns" us to mProgressLayout

    ControlClass mControlInstance; // Instance of ControlClass that is currently being benchmarked

    /**
     * TODO: make these two settable in LinearLayout mProgressLayout
     */
    Long PROGRESS_STEPS = 100L; // Number of steps in ProgressBar
    Long ITERATIONS_PER_STEP = 1000000L; // Number of repetitions per ProgressBar step.
    EditText progressSteps; // EditText in layout used to change PROGRESS_STEPS
    EditText iterationsPerStep; // EditText in layout used to change ITERATIONS_PER_STEP

    /**
     * Called when the activity is starting, it sets the content view to the layout
     * activity_test_bench_mark, locates the ProgressBar View R.id.progress_horizontal
     * and squirrels it away for later use, locates the Button R.id.start_one in the layout,
     * and sets the OnClickListener of the Button to run the first method's benchmark, locates
     * the Button R.id.start_two in the layout, and sets the OnClickListener of the Button to
     * run the second method's benchmark. It then locates the Button R.id.abort and sets its
     * OnClickListener to just Log a message TODO: Implement an "abort" function
     * It locates the LinearLayout containing the ProgressBar (R.id.progress_view_linear_layout)
     * and the LinearLayout containing the result View (R.id.results_linear_layout) and squirrels
     * them away in mProgressLayout, and mResultsLinearLayout to use later to swap visibility of
     * these two views. It locates the "TRY AGAIN" button in the results LinearLayout and set its
     * OnClickListener to swap the visibility from the results LinearLayout to the progress
     * LinearLayout. It then locates the TextView for the results of the benchmark (R.id.results_view)
     * and squirrels it away in mResults for use by the benchmarking to append the output of the
     * results.
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bench_mark);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        startButtonOne = (Button) findViewById(R.id.start_one);
        startButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                mControlInstance = new ControlClass1();
                mControlInstance.execute(ITERATIONS_PER_STEP, PROGRESS_STEPS);
            }
        });
        startButtonTwo = (Button) findViewById(R.id.start_two);
        startButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                mControlInstance = new ControlClass2();
                mControlInstance.execute(ITERATIONS_PER_STEP, PROGRESS_STEPS);
            }
        });
        abortButton = (Button) findViewById(R.id.abort);
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Abort button clicked");
                finish();
            }
        });

        progressSteps = (EditText) findViewById(R.id.progress_steps);
        progressSteps.setText(PROGRESS_STEPS.toString());
        iterationsPerStep = (EditText) findViewById(R.id.iterations_per_step);
        iterationsPerStep.setText(ITERATIONS_PER_STEP.toString());

        mProgressLayout = (LinearLayout) findViewById(R.id.progress_view_linear_layout);
        mResultsLinearLayout = (LinearLayout) findViewById(R.id.results_linear_layout);

        mTryAgain = (Button) findViewById(R.id.try_again);
        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultsLinearLayout.setVisibility(View.GONE);
                mProgressLayout.setVisibility(View.VISIBLE);
            }
        });

        mResults = (TextView) findViewById(R.id.results_view);
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
         * @param result The elapsed time the benchmark took.
         */
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(TAG, "Benchmark took " + result + " milliseconds");
            String formattedIterations = NumberFormat.getNumberInstance(Locale.US).format(PROGRESS_STEPS* ITERATIONS_PER_STEP);
            String formattedResult = NumberFormat.getNumberInstance(Locale.US).format(result);
            mResults.append("Executed " + formattedIterations + " times in\n" + formattedResult + " milliseconds\n");
            mProgressLayout.setVisibility(View.GONE);
            mResultsLinearLayout.setVisibility(View.VISIBLE);
        }

        /**
         * Runs on the UI thread after publishProgress(Long...) is invoked.
         * The specified values are the values passed to publishProgress(Long...).
         * Override this to advance a progress bar
         *
         * @param progress The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Long... progress) {
            super.onProgressUpdate(progress);
            mProgressBar.setProgress(progress[0].intValue());
        }
    }

    /**
     * This is a simple example use of ControlClass designed to benchmark division.
     */
    private class ControlClass1 extends ControlClass {
        double acc = 1.000000001;
        double div = 0.999999999;
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
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
        double acc = 1.000000001;
        double mul = 0.999999999;
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            acc = acc * mul;
        }
    }


}
