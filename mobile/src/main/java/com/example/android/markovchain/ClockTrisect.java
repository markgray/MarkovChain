package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.common.BenchMark;
import com.example.android.common.ClockDataItem;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Searches for the time of day when the hands of a clock comes closest to trisecting the face of
 * the clock.
 */
public class ClockTrisect extends Activity {
    /**
     * TAG used for logging.
     */
    static final String TAG = "ClockTrisect";
    /**
     * Amount to increment seconds by for each trial {@code ClockDataItem}
     */
    private double increment = 1.0;
    /**
     * The precision needed to format the current {@code increment}
     */
    private int incrementPrecision = 0;
    /**
     * {@code LinearLayout} we add our results to
     */
    LinearLayout outputLinearLayout;
    /**
     * The {@code AsyncTask} which does all our calculations
     */
    ClockDataTask clockDataTask;
    /**
     * The {@code BenchMark} which times how long it takes to do all our calculations
     */
    BenchMark benchMark;
    /**
     * The array of {@code ClockDataItem} objects for the best trisection for each minute on the clock
     */
    ClockDataItem[] minuteBestClock = null;

    /**
     * Called when the {@code Activity} is starting. First we call our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file R.layout.activity_clock_trisect.
     * We initialize our field {@code LinearLayout outputLinearLayout} by finding the view with id
     * R.id.linear_layout, and our variable {@code Button button} by finding the view with id
     * R.id.start_the_clock ("Start the clock"). Finally we set the {@code OnClickListener} of
     * {@code button} to an anonymous class whose {@code onClick} override calls our method
     * {@code createClockDataTask} to initialize our field {@code ClockDataTask clockDataTask},
     * initializes our field {@code BenchMark benchMark} (starting its clock) then calls the
     * {@code execute} method of {@code clockDataTask} to start it running (its {@code doInBackground}
     * method will be called with the value of {@code clockDataItem} as its parameter).
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
            /**
             * Called when our {@code Button button} has been clicked. We set the static field {@code secondFormat}
             * of the class {@code ClockDataItem} to a string formed by concatenating the string "%0"
             * followed by 2 plus the current value of our field {@code incrementPrecision} followed
             * by the string "." followed by the current value of our field {@code incrementPrecision}
             * followed by the string "f" (this is the string that the {@code toString} method of
             * {@code ClockDataItem} will use to format its {@code timeSecond} field). We then call
             * our method {@code createClockDataTask} to construct and initialize our field
             * {@code ClockDataTask clockDataTask}. We divide our field {@code double increment} by 10,
             * increment our field {@code int incrementPrecision}, initialize our field {@code BenchMark benchMark}
             * with a new instance (starting its clock) then call the {@code execute} method of {@code clockDataTask}
             * with our field {@code ClockDataItem[] minuteBestClock} as the parameter to be passed to its
             * {@code doInBackground} method.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                ClockDataItem.secondFormat = "%0" + (incrementPrecision + 2) + "." + incrementPrecision + "f";
                createClockDataTask();
                increment /= 10.0;     // TODO: change button's label
                incrementPrecision++;
                benchMark = new BenchMark();
                clockDataTask.execute(minuteBestClock);
            }
        });
    }

    /**
     * Adds a TextView containing the {@code String text} to the {@code ViewGroup parent}. First we
     * create a {@code TextView textView}, then we set the text of {@code TextView textView} to the
     * {@code String text}, and finally we add the {@code TextView textView} to the {@code ViewGroup parent}
     * (our vertical {@code LinearLayout} in our case) at index 0 (the top).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param parent ViewGroup to add our TextView to
     */
    public void addText(String text, ClockDataItem clockTime, final ViewGroup parent) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setCompoundDrawables(new ClockFaceView(textView, clockTime).getBitmapDrawable(), null, null, null);
        parent.addView(textView, 0);
    }

    /**
     * Initializes our field {@code ClockDataTask clockDataTask} to an anonymous {@code ClockDataTask}
     * for the current value of our field {@code double increment}, overriding the {@code onPreExecute},
     * {@code onPostExecute}, and {@code onProgressUpdate} methods. If our field {@code ClockDataItem[] minuteBestClock}
     * is null, we initialize it with the array returned by the {@code init} method of {@code clockDataTask}.
     */
    @SuppressLint("StaticFieldLeak")
    public void createClockDataTask() {
        clockDataTask = new ClockDataTask(increment) {
            /**
             * Runs on the UI thread before {@link #doInBackground}. We call the {@code removeAllViews}
             * method of our field {@code LinearLayout outputLinearLayout} to have it remove all of
             * its child views.
             */
            @Override
            protected void onPreExecute() {
                outputLinearLayout.removeAllViews();
            }
            /**
             * Runs on the UI thread after {@link #doInBackground}. {@code ClockDataItem aClockDataItem}
             * is the value returned by {@link #doInBackground}. We initialize our variable {@code String benchResult}
             * with the Locale.US formatted number string of the {@code long} value (elapsed time) returned
             * by the {@code stop} method of {@code benchMark}. We then call our {@code addText} method
             * to have it display a {@code TextView} at the top of {@code outputLinearLayout} whose text
             * consists of the string formed by concatenating the string "Final Result: " followed by {@code benchResult}
             * followed by the string " milliseconds\n", followed by the string value of our parameter {@code aClockDataItem}
             * followed by the newline character.
             *
             * @param aClockDataItem The result of the operation computed by {@link #doInBackground}.
             */
            @Override
            protected void onPostExecute(ClockDataItem aClockDataItem) {
                String benchResult = NumberFormat.getNumberInstance(Locale.US).format(benchMark.stop());
                addText("Final Result: " + benchResult + " milliseconds\n" + aClockDataItem + "\n",
                        aClockDataItem, outputLinearLayout);
            }
            /**
             * Runs on the UI thread after {@link #publishProgress} is invoked. The specified values
             * are the values passed to {@link #publishProgress}. We call our {@code addText} method
             * to have it display a {@code TextView} at the top of {@code outputLinearLayout} whose text
             * consists of the string formed by concatenating the string value of our parameter
             * {@code ClockDataItem values[0]} followed by a newline character.
             *
             * @param values The {@code ClockDataItem} with the best "badness" for the hour just tried.
             */
            @Override
            protected void onProgressUpdate(ClockDataItem... values) {
                addText(values[0] + "\n",
                        values[0], outputLinearLayout);
                Log.i(TAG, "Posting Hourly best for: " + values[0].timeHour);
            }
        };
        if (minuteBestClock == null) {
            minuteBestClock = clockDataTask.init();
        }
    }
}
