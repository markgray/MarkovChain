package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.common.BenchMark;
import com.example.android.common.ClockDataAdapter;
import com.example.android.common.ClockDataItem;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Searches for the time of day when the hands of a clock comes closest to trisecting the face of
 * the clock.
 */
public class ClockTrisect extends Activity {
    /**
     * Current hour of the day.
     */
    private int h = 1;
    private int m = 0;
    private double s = 0.0;
    private double increment = 1.0;
    LinearLayout outputLinearLayout;
    ClockDataAdapter adapter = new ClockDataAdapter();
    ClockDataTask clockDataTask;
    BenchMark benchMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_trisect);
        outputLinearLayout = findViewById(R.id.linear_layout);
        Button button = findViewById(R.id.start_the_clock);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockDataItem clockDataItem = new ClockDataItem(h, m, s);
                addText(clockDataItem + "\n", outputLinearLayout);
                createClockDataTask();
                increment /= 10.0;
                benchMark = new BenchMark();
                clockDataTask.execute(clockDataItem);
            }
        });
    }

    /**
     * Adds a TextView containing the String text to the ViewGroup parent. First we create a TextView
     * text, then we set the text of <b>TextView text</b> to the String text, and finally we
     * add the TextView text to the ViewGroup parent (our vertical LinearLayout).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param parent ViewGroup to add our TextView to
     */
    public void addText(String text, final ViewGroup parent) {
        TextView mText = new TextView(this);
        mText.setText(text);
        parent.addView(mText, 0);
    }

    @SuppressLint("StaticFieldLeak")
    public void createClockDataTask() {
        clockDataTask = new ClockDataTask(increment) {
            @Override
            protected void onPreExecute() {
                outputLinearLayout.removeAllViews();
            }

            @Override
            protected void onPostExecute(ClockDataItem aClockDataItem) {
                String benchResult = NumberFormat.getNumberInstance(Locale.US).format(benchMark.stop());
                addText("Final Result: " + benchResult + " milliseconds\n" + aClockDataItem + "\n", outputLinearLayout);
                adapter.sortList();
            }

            @Override
            protected void onProgressUpdate(ClockDataItem... values) {
                addText(values[0] + "\n", outputLinearLayout);
                adapter.addToDataSet(values[0]);
            }
        };
    }
}
