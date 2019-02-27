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
    /**
     * TAG used for logging
     */
    String TAG = "TestBenchMark";
    /**
     * {@code LinearLayout} with id R.id.progress_view_linear_layout that contains our UI widgets:
     * {@code ProgressBar vProgressBar}, the two start Buttons, the {@code Button vAbortButton} "ABORT"
     * button, {@code EditText vProgressSteps}, and {@code EditText vIterationsPerStep}. It shares a
     * {@code FrameLayout} with {@code LinearLayout vResultsLinearLayout} and starts out VISIBLE then
     * switches to GONE when a benchmark finishes so that the results can be seen.
     */
    LinearLayout vProgressLayout;
    /**
     * {@code ProgressBar} in our layout used to show the progress of our benchmark.
     */
    ProgressBar vProgressBar;
    /**
     * {@code Button} used to start version one of code
     */
    Button vStartButtonOne;
    /**
     * {@code Button} used to start version two of code
     */
    Button vStartButtonTwo;
    /**
     * {@code Button} currently used to {@code finish()} this {@code Activity}
     */
    Button vAbortButton;

    /**
     * {@code EditText} in layout used to change {@code mProgressSteps}
     */
    EditText vProgressSteps;
    /**
     * {@code EditText} in layout used to change {@code mIterationsPerStep}
     */
    EditText vIterationsPerStep;

    /**
     * Number of steps in {@code ProgressBar}
     */
    Long mProgressSteps = 100L;
    /**
     * Number of repetitions per {@code ProgressBar} step.
     */
    Long mIterationsPerStep = 10000L;

    /**
     * {@code LinearLayout} with id R.id.results_linear_layout in our layout file that contains
     * {@code TextView vResults}, and {@code Button vTryAgain}. It shares a {@code FrameLayout} with
     * {@code LinearLayout vProgressLayout} and starts out GONE then switches to VISIBLE when a benchmark
     * finishes so that the results displayed in {@code TextView vResults} can be seen.
     */
    LinearLayout vResultsLinearLayout;
    /**
     * {@code TextView} used to display results
     */
    TextView vResults;
    /**
     * {@code Button} in {@code vResultsLinearLayout} that "returns" us to {@code vProgressLayout}
     */
    Button vTryAgain;

    /**
     * Instance of {@code ControlClass} that is currently being used
     */
    ControlClass mControlInstance;

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file activity_test_bench_mark.
     * We initialize our field {@code ProgressBar vProgressBar} by finding the view with id
     * R.id.progress_horizontal, initialize our field {@code Button vStartButtonOne} by finding the
     * view with id R.id.start_one and set its {@code OnClickListener} to an anonymous class that will
     * run the first method's benchmark ({@code ControlClass1}) when the {@code Button} is clicked,
     * initialize our field {@code Button vStartButtonTwo} by finding the view with id R.id.start_two
     * and set its {@code OnClickListener} to an anonymous class that will run the second method's
     * benchmark ({@code ControlClass2}). We then initialize our field {@code Button vAbortButton} by
     * finding the view with id R.id.abort and set its {@code OnClickListener} to an anonymous class
     * which calls {@code Activity.finish()} to stop this {@code Activity} and return to {@code MainActivity}.
     * TODO: change to just stop current benchmark
     * <p>
     * We initialize our field {@code EditText vProgressSteps} (the {@code EditText} used to change
     * the maximum number of steps in the {@code ProgressBar}) by finding the view with the id
     * R.id.progress_steps and set its text to the String value of the current value of our field
     * {@code Long mProgressSteps}, and we initialize our field {@code EditText vIterationsPerStep}
     * (the {@code EditText} used to change the number of iterations per progress step) by finding the
     * view with id R.id.iterations_per_step and set its text to the String value of the current value
     * of our field {@code Long mIterationsPerStep}.
     * <p>
     * We initialize our field {@code LinearLayout vProgressLayout} by finding the view with id
     * R.id.progress_view_linear_layout, and our field {@code LinearLayout vResultsLinearLayout} by
     * finding the view with id R.id.results_linear_layout (we need these to use later to swap the
     * visibility of these two views). We initialize our field {@code Button vTryAgain} by finding
     * the view with id R.id.try_again ("TRY AGAIN") and set its {@code OnClickListener} to an anonymous
     * class that will swap the visibility from the results {@code LinearLayout vResultsLinearLayout}
     * to the progress {@code LinearLayout vProgressLayout}. We then initialize our field
     * {@code TextView vResults} (the {@code TextView} for the results of the benchmark) by finding
     * the view with id R.id.results_view (the current benchmark results will be appended to it).
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bench_mark);

        vProgressBar = findViewById(R.id.progress_horizontal);
        vStartButtonOne = findViewById(R.id.start_one);
        vStartButtonOne.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the "START ONE" {@code Button} is clicked. First we log the fact that we
             * were clicked. Then we call our {@code updateIterationValues()} method to read the
             * value entered in {@code EditText vProgressSteps} to update the number of steps for
             * the {@code ProgressBar} storing it in our field {@code mProgressSteps} and the value
             * entered in {@code EditText vIterationsPerStep} to update {@code mIterationsPerStep}.
             * We then initialize our field {@code ControlClass mControlInstance} with an instance
             * of {@code ControlClass1} and call its {@code execute} method to have it run its
             * {@code testMethod} override {@code mIterationsPerStep} times {@code mProgressSteps}
             * times updating the {@code ProgressBar} by one step every {@code mIterationsPerStep}
             * iterations.
             *
             * @param v {@code View} that was clicked
             */
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                updateIterationValues();
                mControlInstance = new ControlClass1();
                mControlInstance.execute(mIterationsPerStep, mProgressSteps);
            }
        });
        vStartButtonTwo = findViewById(R.id.start_two);
        vStartButtonTwo.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the "START TWO" {@code Button} is clicked. First we log the fact that we
             * were clicked. Then we call our {@code updateIterationValues()} method to read the
             * value entered in {@code EditText vProgressSteps} to update the number of steps for
             * the {@code ProgressBar} storing it in our field {@code mProgressSteps} and the value
             * entered in {@code EditText vIterationsPerStep} to update {@code mIterationsPerStep}.
             * We then initialize our field {@code ControlClass mControlInstance} with an instance
             * of {@code ControlClass1} and call its {@code execute} method to have it run its
             * {@code testMethod} override {@code mIterationsPerStep} times {@code mProgressSteps}
             * times updating the {@code ProgressBar} by one step every {@code mIterationsPerStep}
             * iterations.
             *
             * @param v {@code View} that was clicked
             */
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                updateIterationValues();
                mControlInstance = new ControlClass2();
                mControlInstance.execute(mIterationsPerStep, mProgressSteps);
            }
        });
        vAbortButton = findViewById(R.id.abort);
        vAbortButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the "ABORT" {@code Button} is clicked. First we log the fact that we were
             * clicked, then we call the {@code finish()} method to close this {@code Activity}.
             *
             * @param v {@code View} that was clicked.
             */
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Abort button clicked");
                finish();
            }
        });

        vProgressSteps = findViewById(R.id.progress_steps);
        vProgressSteps.setText(mProgressSteps.toString());
        vIterationsPerStep = findViewById(R.id.iterations_per_step);
        vIterationsPerStep.setText(mIterationsPerStep.toString());

        vProgressLayout = findViewById(R.id.progress_view_linear_layout);
        vResultsLinearLayout = findViewById(R.id.results_linear_layout);

        vTryAgain = findViewById(R.id.try_again);
        vTryAgain.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the "TRY AGAIN" {@code Button} is clicked. We just set the visibility of
             * {@code LinearLayout vResultsLinearLayout} to GONE, and the visibility of
             * {@code LinearLayout vProgressLayout} to VISIBLE.
             *
             * @param v {@code View} that was clicked.
             */
            @Override
            public void onClick(View v) {
                vResultsLinearLayout.setVisibility(View.GONE);
                vProgressLayout.setVisibility(View.VISIBLE);
            }
        });

        vResults = findViewById(R.id.results_view);
    }

    /**
     * This method reads the text in the {@code EditText vProgressSteps}, converts that String to
     * {@code Long} and uses that value to update the contents of our field {@code mProgressSteps},
     * and also uses it to set the max number of steps for our {@code ProgressBar vProgressBar}. It
     * then reads the text in {@code EditText vIterationsPerStep}, converts it to {@code Long} and
     * uses that value to update the contents of our field {@code mIterationsPerStep}. These two
     * values are used as arguments to the {@code execute} method of {@code ControlInstance}.
     */
    private void updateIterationValues() {
        mProgressSteps = Long.parseLong(String.valueOf(vProgressSteps.getText()));
        vProgressBar.setMax(mProgressSteps.intValue());
        mIterationsPerStep = Long.parseLong(String.valueOf(vIterationsPerStep.getText()));
    }

    /**
     * This class should be extended by classes which wish to benchmark their code in the
     * overridden method {@code testMethod()}.
     */
    @SuppressLint("StaticFieldLeak")
    private class ControlClass extends CalcTask {
        /**
         * Runs on the UI thread after {@code doInBackground(Long...)}. The {@code Long result} parameter
         * is the value returned by {@code doInBackground(Long...)}. We override this to make use of
         * the elapsed time value returned.
         * <p>
         * First we call through to our super's implementation of {@code onPostExecute}. Then we format
         * a {@code String formattedIterations} to display the number of iterations performed just done
         * ({@code mProgressSteps*mIterationsPerStep}), and format a {@code String formattedResult} to
         * display our parameter {@code Long result} (total benchmark time in milliseconds). We then
         * append a {@code String} containing those two strings to {@code TextView vResults}. Finally
         * we set the visibility of {@code LinearLayout vProgressLayout} to GONE and the visibility of
         * {@code LinearLayout vResultsLinearLayout} to VISIBLE in order to see the results displayed.
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
         * Runs on the UI thread after {@code publishProgress(Long...)} is invoked. The parameter
         * {@code progress} is the value passed to {@code publishProgress(Long...)}. We override
         * this to advance our progress bar.
         * <p>
         * First we call through to our super's implementation of {@code onProgressUpdate}, then we
         * set the {@code ProgressBar vProgressBar} progress to the integer value of our parameter.
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
    @SuppressLint("StaticFieldLeak")
    private class ControlClass1 extends ControlClass {
        /**
         * Accumulator register for repeated divisions
         */
        double acc = 1.000000001;
        /**
         * Divisor register for repeated divisions
         */
        double div = 0.999999999;

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
    @SuppressLint("StaticFieldLeak")
    private class ControlClass2 extends ControlClass {
        /**
         * Accumulator register for repeated multiplications
         */
        double acc = 1.000000001;
        /**
         * Multiplicand register for repeated multiplications
         */
        double mul = 0.999999999;

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
