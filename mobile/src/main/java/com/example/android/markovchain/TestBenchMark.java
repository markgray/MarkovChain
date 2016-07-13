package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.android.common.CalcTask;

public class TestBenchMark extends Activity {
    String TAG = "TestBenchMark";
    Button startButton;
    Button abortButton;
    ProgressBar mProgressBar;

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
            }
        });
        abortButton = (Button) findViewById(R.id.abort);
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Abort button clicked");
            }
        });
    }

    private class ControlClass extends CalcTask {
        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.i(TAG, "Benchmark took " + result + " milliseconds");
        }

        @Override
        protected void onProgressUpdate(Long... progress) {
            super.onProgressUpdate(progress);
            mProgressBar.setProgress(progress[0].intValue());
        }
    }


}
