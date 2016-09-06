package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.example.android.common.CalcTask;
import com.example.android.common.Shakespeare;

/**
 * This activity is useful to benchmark two different implementations of a method
 */
public class TestBenchMark extends Activity {
    String TAG = "TestBenchMark";
    Button startButtonOne;
    Button startButtonTwo;
    Button abortButton;
    ProgressBar mProgressBar;
    TextView mResults;
    Button mTryAgain;
    LinearLayout mProgressLayout;
    LinearLayout mResultsLinearLayout;
    ControlClass mControlInstance;
    final Long PROGRESS_STEPS = 100L;
    final Long LOOP_REPETITIONS = 10000000L;

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
                mControlInstance.execute(LOOP_REPETITIONS, PROGRESS_STEPS);
            }
        });
        startButtonTwo = (Button) findViewById(R.id.start_two);
        startButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                mControlInstance = new ControlClass2();
                mControlInstance.execute(LOOP_REPETITIONS, PROGRESS_STEPS);
            }
        });
        abortButton = (Button) findViewById(R.id.abort);
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Abort button clicked");
            }
        });
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
            String formattedIterations = NumberFormat.getNumberInstance(Locale.US).format(PROGRESS_STEPS* LOOP_REPETITIONS);
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
        public double acc = 1.000000001;
        public double div = 0.999999999;
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
        public double acc = 1.000000001;
        public double mul = 0.999999999;
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            acc = acc * mul;
        }
    }

    private class ControlCall3 extends ControlClass {
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            init();
            findFromCitation1("not here", "not here either");
        }
    }

    private class ControlCall4 extends ControlClass {
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            init();
            findFromCitation2("not here", "not here either");
        }
    }


    static ArrayList<String> bookChapterVerse = null;
    public void init() {
        if (bookChapterVerse != null) {
            return;
        }
        bookChapterVerse = new ArrayList<>();
        for (String s :
                Shakespeare.SONNETS) {
            bookChapterVerse.add(s);
        }
    }

    public static int findFromCitation1(String citation, String fallback) {
        int fallBackIndex = 0;
        for (int i = 0; i < bookChapterVerse.size(); i++) {
            if (citation.equals(bookChapterVerse.get(i))) {
                return i;
            }
            if (fallback.equals(bookChapterVerse.get(i))) {
                fallBackIndex = i;
            }
        }
        return fallBackIndex;
    }

    public static int findFromCitation2(String citation, String fallback) {
        int fallBackIndex = 0;
        for (int i = 0; i < bookChapterVerse.size(); i++) {
            String stringToCheck = bookChapterVerse.get(i);
            if (citation.equals(stringToCheck)) {
                return i;
            }
            if (fallback.equals(stringToCheck)) {
                fallBackIndex = i;
            }
        }
        return fallBackIndex;
    }


}
