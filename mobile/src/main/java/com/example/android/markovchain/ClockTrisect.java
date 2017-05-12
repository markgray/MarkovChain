package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.common.ClockDataAdapter;
import com.example.android.common.ClockDataItem;

import java.util.Random;

public class ClockTrisect extends Activity {
    private int h = 1;
    private int m = 0;
    private double s = 0.0;
    private static Random rand = new Random(); // Random number generator used for random time.
    LinearLayout outputLinearLayout;
    ClockDataAdapter adapter = new ClockDataAdapter();
    ClockDataTask clockDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_trisect);
        outputLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        Button button = (Button) findViewById(R.id.start_the_clock);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockDataItem clockDataItem = new ClockDataItem(h, m, s);
                addText(clockDataItem + "\n", outputLinearLayout);
                h = Math.abs(rand.nextInt()) % 12 + 1;
                m = Math.abs(rand.nextInt()) % 60;
                s = Math.abs(rand.nextInt()) % 60;
                createClockDataTask();
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

    public void createClockDataTask() {
        clockDataTask = new ClockDataTask() {
            @Override
            protected void onPreExecute() {
                outputLinearLayout.removeAllViews();
            }

            @Override
            protected void onPostExecute(ClockDataItem aClockDataItem) {
                addText(aClockDataItem + "\n", outputLinearLayout);
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
