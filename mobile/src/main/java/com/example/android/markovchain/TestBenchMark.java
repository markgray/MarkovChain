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

import com.example.android.common.CalcTask;
import com.example.android.common.Shakespeare;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

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
    final Long LOOP_REPITIONS = 100L;

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
                mControlInstance = new ControlClass3();
                mControlInstance.execute(LOOP_REPITIONS, PROGRESS_STEPS);
            }
        });
        startButtonTwo = (Button) findViewById(R.id.start_two);
        startButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
                mControlInstance = new ControlClass4();
                mControlInstance.execute(LOOP_REPITIONS, PROGRESS_STEPS);
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
        mTryAgain = (Button) findViewById(R.id.try_again);
        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultsLinearLayout.setVisibility(View.GONE);
                mProgressLayout.setVisibility(View.VISIBLE);
            }
        });
        mResultsLinearLayout = (LinearLayout) findViewById(R.id.results_linear_layout);

        mResults = (TextView) findViewById(R.id.results_view);
    }

    private class ControlClass extends CalcTask {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(TAG, "Benchmark took " + result + " milliseconds");
            String formatedIterations = NumberFormat.getNumberInstance(Locale.US).format(PROGRESS_STEPS*LOOP_REPITIONS);
            String formatedResult = NumberFormat.getNumberInstance(Locale.US).format(result);
            mResults.append("Executed " + formatedIterations + " times in\n" + formatedResult + " milliseconds\n");
            mProgressLayout.setVisibility(View.GONE);
            mResultsLinearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            super.onProgressUpdate(progress);
            mProgressBar.setProgress(progress[0].intValue());
        }
    }

//    private class ControlClass1 extends ControlClass {
//        public double acc = 1.000000001;
//        public double div = 0.999999999;
//        /**
//         * This method should be overridden by a method which performs whatever computation
//         * you wish to benchmark.
//         */
//        @Override
//        public void testMethod() {
//            acc = acc / div;
//        }
//    }
//
//    private class ControlClass2 extends ControlClass {
//        public double acc = 1.000000001;
//        public double mul = 0.999999999;
//        /**
//         * This method should be overridden by a method which performs whatever computation
//         * you wish to benchmark.
//         */
//        @Override
//        public void testMethod() {
//            acc = acc * mul;
//        }
//    }

    private class ControlClass3 extends ControlClass {

        public String[] testArray;

        public ControlClass3() {
            testArray = Shakespeare.SONNETS[0].split(" ");
        }
        /**
         * Given an array of strings which might contain duplicate strings remove all
         * duplicates
         *
         * @param strings String array with possible duplicate string members
         * @return Same array with only single occurrences of strings
         */
        private String[] uniq(String[] strings) {
            HashSet<String> setOfStrings = new HashSet<>(strings.length);
            Collections.addAll(setOfStrings, strings);
            String[] returnStringArray = new String[setOfStrings.size()];
            for (int i = 0; i < setOfStrings.size(); i++) {
                returnStringArray[i] = (String) setOfStrings.toArray()[i];
            }
            return returnStringArray;
        }
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            testArray = uniq(testArray);
        }

    }
    private class ControlClass4 extends ControlClass {

        public String[] testArray;

        public ControlClass4() {
            testArray = Shakespeare.SONNETS[0].split(" ");
        }
        /**
         * Given an array of strings which might contain duplicate strings remove all
         * duplicates
         *
         * @param strings String array with possible duplicate string members
         * @return Same array with only single occurrences of strings
         */
        private String[] uniq(String[] strings) {
            HashSet<String> setOfStrings = new HashSet<>(strings.length);
            Collections.addAll(setOfStrings, strings);
            String[] returnStringArray = new String[setOfStrings.size()];
            Object[] tempObjectArray = setOfStrings.toArray();
            for (int i = 0; i < setOfStrings.size() ; i++) {
                returnStringArray[i] = (String) tempObjectArray[i];
            }
            return returnStringArray;
        }
        /**
         * This method should be overridden by a method which performs whatever computation
         * you wish to benchmark.
         */
        @Override
        public void testMethod() {
            testArray = uniq(testArray);
        }

    }

}
