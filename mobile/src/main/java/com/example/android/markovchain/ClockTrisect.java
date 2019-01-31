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
import java.util.Calendar;
import java.util.Locale;

/**
 * Searches for the time of day when the hands of a clock comes closest to trisecting the face of
 * the clock.
 */
public class ClockTrisect extends Activity {
    /**
     * Hour of the day to start search at.
     */
    private int h = 1;
    /**
     * Minute of the hour to start search at.
     */
    private int m = 0;
    /**
     * Second of the minute to start search at.
     */
    private double s = 0.0;
    /**
     * Amount to increment seconds by for each trial {@code ClockDataItem}
     */
    private double increment = 1.0;
    /**
     * {@code LinearLayout} we add our results to TODO: Change to RecyclerView you lazy bum.
     */
    LinearLayout outputLinearLayout;
    /**
     * The {@code RecyclerView.Adapter} we will eventually use instead of {@code LinearLayout outputLinearLayout}
     */
    ClockDataAdapter adapter = new ClockDataAdapter();
    /**
     * The {@code AsyncTask} which does all our calculations
     */
    ClockDataTask clockDataTask;
    /**
     * The {@code BenchMark} which times how long it takes to do all our calculations
     */
    BenchMark benchMark;

    /**
     * Called when the {@code Activity} is starting. First we call our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file R.layout.activity_clock_trisect.
     * We initialize our field {@code LinearLayout outputLinearLayout} by finding the view with id
     * R.id.linear_layout, and our variable {@code Button button} by finding the view with id
     * R.id.start_the_clock ("Start the clock"). Finally we set the {@code OnClickListener} of
     * {@code button} to an anonymous class whose {@code onClick} override
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_trisect);

        outputLinearLayout = findViewById(R.id.linear_layout);
        Button button = findViewById(R.id.start_the_clock);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h = Calendar.getInstance().get(Calendar.HOUR);
                if(h == 0) h =12;
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
