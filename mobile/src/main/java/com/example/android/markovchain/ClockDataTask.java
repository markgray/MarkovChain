package com.example.android.markovchain;

import android.os.AsyncTask;

/**
 * The first time this background task is run it will cycle through all the seconds in a day, and if
 * the {@code badness} field of the best {@code ClockDataItem} for a particular minute is less than
 * 12 it will publish this {@code ClockDataItem} to the UI thread, and if not it will remove it from
 * future consideration for fractional seconds. When done it will return the {@code ClockDataItem}
 * that has the best {@code badness} field. In subsequent runs with fractional increments for the
 * seconds it only looks at minutes which survived the previous pass.
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

    /**
     * We override this method to perform a computation on a background thread. The parameters is a
     * {@code ClockDataItem} array containing the best trisection of the clock for every minute of
     * the half day so far passed to {@link #execute} by the caller of this task, and entries are
     * updated here or deleted if they are too bad to be improved by another iteration (a badness
     * greater than 12 degrees cannot be improved by further fine adjustment of the second hand).
     * We call {@link #publishProgress} to publish updates on the UI thread when a minute is found
     * to have a badness that is less than 12.
     * <p>
     * We initialize our variable {@code int indexToMinute} to 0 (it will point to the {@code ClockDataItem}
     * of the minute whose seconds we are searching for a better trisection). Then we loop over {@code h}
     * for the 12 hours of our clock, and in an inner loop loop over {@code m} for the 60 minutes in each
     * hour:
     * <ul>
     *     <li>
     *         If the next minute's {@code ClockDataItem} in {@code params[indexToMinute]} is null, we
     *         skip it just incrementing {@code indexToMinute}.
     *     </li>
     *     <li>
     *         Otherwise we set our field {@code double s} to 0.0, then loop while {@code s} is less than
     *         60.0, setting the time in {@code ClockDataItem trialClock} to {@code h} hour, {@code m}
     *         minute and {@code s} second and if the {@code badness} field of {@code trialClock} is
     *         less than the {@code badness} field of {@code params[indexToMinute]} we clone {@code trialClock}
     *         into {@code params[indexToMinute]}, in either case we then add {@code increment} to {@code s}
     *         and loop around for the next value of {@code s}.
     *     </li>
     *     <li>
     *         When done considering all of the seconds in {@code params[indexToMinute]} if the {@code badness}
     *         field of {@code params[indexToMinute]} is less than 12.0 we call {@code publishProgress} to
     *         have the {@code onProgressUpdate} override output the string value of {@code params[indexToMinute]}.
     *         If on the other hand the {@code badness} is greater than or equal to 12.0 we set {@code params[indexToMinute]}
     *         to null so it will no longer be considered (a second spans a 6 degree arc so no further fine
     *         adjustment of the second can possibly correct for this).
     *     </li>
     *     <li>
     *         We then increment {@code indexToMinute} and loop around to explore the next minute in
     *         {@code ClockDataItem[] params}.
     *     </li>
     * </ul>
     * When done with all the minutes in {@code params} we initialize {@code ClockDataItem bestClock}
     * with a new instance for the time 12:00:00 (a very bad {@code badness} needless to say) then
     * loop through all the {@code ClockDataItem clockDataItem} in {@code params} and if the {@code badness}
     * of the current {@code ClockDataItem clockDataItem} is not null we check whether its {@code badness}
     * field is less than the {@code badness} field of {@code bestClock} and if so we clone {@code clockDataItem}
     * into {@code bestClock}. When done searching {@code params} we return {@code bestClock} to the
     * caller to have our {@code onPostExecute} override output it to the user.
     *
     * @param params a {@code ClockDataItem[]} array containing the best trisection for every minute
     *               as calculated by previous running of {@code ClockDataTask}
     * @return a {@code ClockDataItem} representing the best trisection of the clock
     */
    protected ClockDataItem doInBackground(ClockDataItem... params) {
        int indexToMinute = 0;
        for (h = 0; h < 12; h++) {
            for (m = 0; m <60; m++) {
                if (params[indexToMinute] != null) {
                    s = 0.0;
                    while (s < 60.0) {
                        trialClock.set(h, m, s);
                        if (trialClock.getBadness() < params[indexToMinute].getBadness()) {
                            params[indexToMinute].clone(trialClock);
                        }
                        s += increment;
                    }
                    if (params[indexToMinute].getBadness() < 12.0) {
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
                if (clockDataItem.getBadness() < bestClock.getBadness()) {
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
