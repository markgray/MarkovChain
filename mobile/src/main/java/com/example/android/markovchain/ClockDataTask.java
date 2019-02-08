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
     * The best trisection of the clock face we have found so far.
     */
    public ClockDataItem bestClock;
    /**
     * The {@code badness} of the best trisection of the clock face we have found so far.
     */
    double bestBadness = trialClock.badness;
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

    /**
     * Our zero parameter constructor, we just call our super's constructor then call our method
     * {@code init} to initialize our result arrays.
     */
    public ClockDataTask() {
        super();
        init();  // TODO: move this call to doInBackground() and save values for next call.
    }

    /**
     * Initializes our results. We loop over {@code int i} for all 12 hours, initializing the contents
     * of {@code ClockDataItem hourlyBestClock[i]} to a new instance whose hour is {@code i} and
     * whose minute and second are 0, and we set {@code double hourlyBestBadness[i]} to the {@code badness}
     * field of {@code hourlyBestClock[i]}. In an inner loop we then loop over {@code int j} for the
     * sixty minutes in an hour initializing {@code ClockDataItem temp} with a new instance whose hour
     * is {@code i}, whose minute is {@code j} and whose second is 0. We store {@code temp} in the
     * slot reserved for the hour and minute in {@code ClockDataItem[] minutelyBestClock} and the
     * badness field of {@code temp} in the slot reserved for the hour and minute in {@code double[] minutelyBestBadness},
     * then continue looping until all our result objects are populated.
     */
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
     * We override this method to perform a computation on a background thread. The specified parameters
     * are the parameters passed to {@link #execute} by the caller of this task. We initialize our field
     * {@code int h} with the {@code timeHour} field of our parameter {@code params[0]}, initialize our
     * field {@code int m} with the {@code timeMinute} field of our parameter {@code params[0]}, and
     * initialize our field {@code double s} with the {@code timeSecond} field of our parameter {@code params[0]}.
     * We initialize our variable {@code double secondsToTry} to the number of seconds in 12 hours, and
     * initialize our variable {@code boolean publishedLastHour} to false. Then we loop over {@code double secondsTried}
     * for the {@code secondsToTry} we want to check, incrementing {@code secondsTried} by our field {@code increment}:
     * <ul>
     *     <li>
     *         We set {@code publishedLastHour} to false (this is necessary because we want to make sure
     *         that the last hour we check has its best results published after we finish our loop as
     *         some increments finish looping before we have a chance to publish that last hour's best).
     *     </li>
     *     <li>
     *         We set the time held by our field {@code ClockDataItem trialClock} to {@code h} hours, {@code m}
     *         minutes and {@code s} seconds.
     *     </li>
     *     <li>
     *         If the {@code badness} field of {@code trialClock} is less than our {@code bestBadness} we have
     *         found a new best trisection, so we set {@code bestBadness} to the {@code badness} field of
     *         {@code trialClock} and if {@code bestClock} is null we initialize it with a new instance whose
     *         time is set to {@code h} hours, {@code m} minutes and {@code s} seconds, and if {@code bestClock}
     *         is not null we clone {@code temp} into it by calling its {@code clone} method.
     *     </li>
     *     <li>
     *         If the {@code badness} field of {@code trialClock} is less than our the value for the best badness
     *         of hour {@code h} contained in {@code hourlyBestBadness[h]} we have found a new best trisection
     *         for hour {@code h}, so we set {@code hourlyBestBadness[h]} to the {@code badness} field of
     *         {@code temp} and if {@code hourlyBestClock[h]} is null we initialize it with a new instance whose
     *         time is set to {@code h} hours, {@code m} minutes and {@code s} seconds, and if it is not null
     *         we clone {@code temp} into it by calling its {@code clone} method.
     *     </li>
     *     <li>
     *         We now increment {@code s} by {@code increment} and if it is now greater than or equal to
     *         60 we set it to 0 and increment {@code m}. If {@code m} is now greater than or equal to
     *         60 we have reached the end of another hour so we call the {@code publishProgress} method
     *         to have the {@code onProgressUpdate} method running on the UI thread output the value
     *         of {@code hourlyBestClock[h]}. We then set {@code publishedLastHour} to true (just to make
     *         sure if we exit the loop now we won't republish {@code hourlyBestClock[h]}), set {@code m}
     *         to 0, and increment {@code h}. If {@code h} is now 12 we break out of the loop (rounding
     *         errors using some values of {@code increment} can cause an extra iteration and hour 13
     *         will crash us!)
     *     </li>
     *     <li>
     *         We then loop around for the next {@code increment} of time.
     *     </li>
     * </ul>
     * On exiting we check to see if {@code publishedLastHour} is false and if so we call the {@code publishProgress}
     * method to have the {@code onProgressUpdate} method running on the UI thread output the value of
     * {@code hourlyBestClock[h]} (rounding errors for some values of {@code increment} can skip the last
     * iteration need to do this in the above loop). Finally we return {@code bestClock} to the caller
     * (who will pass it on to the {@code onPostExecute} method running on the UI thread output).
     *
     * @param params The {@code ClockDataItem} we are to start searching from.
     * @return The {@code ClockDataItem} with the best (lowest) "badness".
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
            trialClock.set(h, m, s);
            if (trialClock.badness < bestBadness) {
                bestBadness = trialClock.badness;
                if (bestClock == null) {
                    bestClock = new ClockDataItem(h, m, s);
                } else {
                    bestClock.clone(trialClock);
                }
            }
            if (trialClock.badness < hourlyBestBadness[h]) {
                hourlyBestBadness[h] = trialClock.badness;
                if (hourlyBestClock[h] == null) {
                    hourlyBestClock[h] = new ClockDataItem(h, m, s);
                } else {
                    hourlyBestClock[h].clone(trialClock);
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
