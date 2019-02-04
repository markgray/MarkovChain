package com.example.android.markovchain;

import android.os.AsyncTask;

import com.example.android.common.ClockDataItem;

/**
 * This background task will cycle through all the seconds in a day, creating a list of ClockDataItem's
 * sorted by the {@code doBadness()} results of each.
 */
@SuppressWarnings("WeakerAccess")
public class ClockDataTask extends AsyncTask<ClockDataItem, ClockDataItem, ClockDataItem> {
    /**
     * The hour we are currently working on
     */
    int h;
    /**
     * The minute we are currently working on
     */
    int m;
    /**
     * The second we are currently working on
     */
    double s;
    /**
     * The fraction of a second we are to increment by
     */
    double increment = 1.0;
    /**
     * The {@code ClockDataItem} whose badness we are currently considering.
     */
    public ClockDataItem clock = new ClockDataItem(0, 0, 0);
    /**
     * The best trisection of the clock face we have found so far.
     */
    public ClockDataItem bestClock;
    /**
     * The {@code badness} of the best trisection of the clock face we have found so far.
     */
    double bestBadness = clock.badness;
    /**
     * The best trisection of the clock face for each hour that we have found so far.
     */
    public ClockDataItem[] hourlyBestClock = new ClockDataItem[12];
    /**
     * The {@code badness} of the best trisection of the clock face for each hour.
     */
    double[] hourlyBestBadness = new double[12];
    /**
     * The best trisection of the clock face for each minute that we have found so far.
     */
    public ClockDataItem[] minutelyBestClock = new ClockDataItem[720];
    /**
     * The {@code badness} of the best trisection of the clock face for each minute.
     */
    double[] minutelyBestBadness = new double[720];

    /**
     * Our constructor which allows the value of our increment to be specified as its parameter. We
     * call our zero parameter constructor then set our field {@code double increment} to our parameter
     * {@code double inc}.
     *
     * @param inc the time increment in seconds we are to use to search with
     */
    public ClockDataTask(double inc) {
        this();
        increment = inc;
    }

    public ClockDataTask() {
        super();
        init();
    }

    private void init() {
        for (int i = 0; i < 12; i++) {
            hourlyBestClock[i] = new ClockDataItem(i, 0, 0);
            hourlyBestBadness[i] = hourlyBestClock[i].badness;
            for (int j = 0; j < 60; j++) {
                ClockDataItem temp = new ClockDataItem(i, j, 0);
                minutelyBestClock[temp.motd()] = temp;
                minutelyBestBadness[temp.motd()] = temp.badness;
            }
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
        boolean publishedLastHour = false;

        for (double secondsTried = 0.0; secondsTried < secondsToTry; secondsTried += increment) {
            publishedLastHour = false;
            clock.set(h, m, s);
            if (clock.badness < bestBadness) {
                bestBadness = clock.badness;
                if (bestClock == null) {
                    bestClock = new ClockDataItem(h, m, s);
                } else {
                    bestClock.clone(clock);
                }
            }
            if (clock.badness < hourlyBestBadness[h]) {
                hourlyBestBadness[h] = clock.badness;
                if (hourlyBestClock[h] == null) {
                    hourlyBestClock[h] = new ClockDataItem(h, m, s);
                } else {
                    hourlyBestClock[h].clone(clock);
                }
            }
            s += increment;
            if (s >= 60.0) {
                s = 0.0;
                m++;
                if (m >= 60) {
                    publishProgress(hourlyBestClock[h]);
                    publishedLastHour = true;
                    m = 0;
                    h++;
                    if (h == 12) break;
                }
            }
        }
        if(!publishedLastHour) {
            publishProgress(hourlyBestClock[h]);
        }
        return bestClock;
    }
}
