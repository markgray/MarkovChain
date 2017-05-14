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
    double s = now.get(Calendar.SECOND);
    public ClockDataItem clock = new ClockDataItem(h, m, s);
    public ClockDataItem bestClock = clock;
    double bestBadness = clock.badness();

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

        for (int i = 0; i<3600*12; i++) {
            s += 1.0;
            if (s >= 60.0) {
                s = 0.0;
                m++;
                if (m >= 60) {
                    m = 0;
                    h++;
                    if (h > 12) {
                        h = 1;
                    }
                }
            }
            clock = new ClockDataItem(h, m, s);
            if (clock.badness() < bestBadness) {
                publishProgress(clock);
                bestBadness = clock.badness();
                bestClock = clock;
            }
        }
        return bestClock;
    }
}
