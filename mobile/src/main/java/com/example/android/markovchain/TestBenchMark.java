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

public class TestBenchMark extends Activity {
    String TAG = "TestBenchMark";
    Button startButton;
    Button abortButton;
    ProgressBar mProgressBar;
    TextView mResults;
    LinearLayout mProgressLayout;
    ControlClass mControlInstance = new ControlClass();
    final Long PROGRESS_STEPS = 100L;
    final Long LOOP_REPITIONS = 10000000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bench_mark);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start button clicked");
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
        mResults = (TextView) findViewById(R.id.results_view);
    }

    private class ControlClass extends CalcTask {
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(TAG, "Benchmark took " + result + " milliseconds");
            mResults.setText("Executed " + PROGRESS_STEPS*LOOP_REPITIONS + " times in\n" + result + " milliseconds");
            mProgressLayout.setVisibility(View.GONE);
            mResults.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            super.onProgressUpdate(progress);
            mProgressBar.setProgress(progress[0].intValue());
        }
    }


}
