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
    public ClockDataItem trialClock = new ClockDataItem(0, 0, 0);

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

    /**
     * Our zero parameter constructor, we just call our super's constructor.
     */
    public ClockDataTask() {
        super();
    }

    protected ClockDataItem doInBackground(ClockDataItem... params) {
        int indexToMinute = 0;
        for (h = 0; h < 12; h++) {
            for (m = 0; m <60; m++) {
                if (params[indexToMinute] != null) {
                    s = 0.0;
                    while (s < 60.0) {
                        trialClock.set(h, m, s);
                        if (trialClock.badness < params[indexToMinute].badness) {
                            params[indexToMinute].clone(trialClock);
                        }
                        s += increment;
                    }
                    if (params[indexToMinute].badness < 12.0) {
                        publishProgress(params[indexToMinute]);
                    } else {
                        params[indexToMinute] = null;
                    }
                    indexToMinute++;
                } else {
                    indexToMinute++;
                }
            }
        }

        ClockDataItem bestClock = new  ClockDataItem(0, 0, 0);
        for (ClockDataItem clockDataItem : params) {
            if (clockDataItem != null) {
                if (clockDataItem.badness < bestClock.badness) {
                    bestClock.clone(clockDataItem);
                }
            }
        }

        return bestClock;
    }

    /**
     * Initializes our results. First we initialize our variable {@code ClockDataItem[] minuteBestClock}
     * by allocating room for 720 {@code ClockDataItem} objects, and initialize {@code int indexToMinute}
     * to 0. We loop over {@code int i} for all 12 hours, and in an inner loop we then loop over {@code int j}
     * for the sixty minutes in an hour initializing {@code minuteBestClock[indexToMinute]} with a new
     * instance whose hour is {@code i}, whose minute is {@code j} and whose second is 0. We then increment
     * {@code indexToMinute} and loop back for the next minute. When done we return {@code minuteBestClock}
     * to the caller.
     *
     * @return an {@code ClockDataItem[]} array that has an entry for every minute
     */
    public ClockDataItem[] init() {
        ClockDataItem[] minuteBestClock = new ClockDataItem[720];
        int indexToMinute = 0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 60; j++) {
                minuteBestClock[indexToMinute] = new ClockDataItem(i, j, 0);
                indexToMinute++;
            }
        }
        return minuteBestClock;
    }
}
