package com.example.android.markovchain;

import android.os.AsyncTask;

import com.example.android.common.ClockDataItem;

import java.util.Calendar;

/**
 * This background task will cycle through all the seconds in a day, creating a list of ClockDataItem's
 * sorted by the {@code badness()} results of each.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataTask extends AsyncTask<ClockDataItem, ClockDataItem, ClockDataItem> {
    Calendar now = Calendar.getInstance();
    int h = now.get(Calendar.HOUR_OF_DAY);
    int m = now.get(Calendar.MINUTE);
    double increment = 1.0;
    double s = now.get(Calendar.SECOND);
    public ClockDataItem clock = new ClockDataItem(h, m, s);
    public ClockDataItem bestClock = clock;
    double bestBadness = clock.badness();
    public ClockDataItem[] hourlyBestClock = new ClockDataItem[13];
    double[] hourlyBestBadness = new double[13];

    public ClockDataTask(double inc) {
        this();
        increment = inc;
    }

    public ClockDataTask() {
        super();
        for (int i = 0; i < 13; i++) {
            hourlyBestClock[i] = new ClockDataItem(i, 0, 0);
            hourlyBestBadness[i] = hourlyBestClock[i].badness();
        }
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     *
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     *
     * @return A result, defined by the subclass of this task.
     *
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected ClockDataItem doInBackground(ClockDataItem... params) {
        h = params[0].timeHour;
        m = params[0].timeMinute;
        s = params[0].timeSecond;
        double secondsToTry = 3600.0 * 12.0;

        for (double secondsTried = 0.0; secondsTried < secondsToTry; secondsTried += increment) {
            clock = new ClockDataItem(h, m, s);
            if (clock.badness() < bestBadness) {
                bestBadness = clock.badness();
                bestClock = clock;
            }
            if (clock.badness() < hourlyBestBadness[h]) {
                hourlyBestBadness[h] = clock.badness();
                hourlyBestClock[h] = clock;
            }
            s += increment;
            if (s >= 60.0) {
                s = 0.0;
                m++;
                if (m >= 60) {
                    publishProgress(hourlyBestClock[h]);
                    m = 0;
                    h++;
                    if (h > 12) {
                        h = 1;
                    }
                }
            }
        }
        return bestClock;
    }
}
