package com.example.android.markovchain;

import android.os.AsyncTask;

import com.example.android.common.ClockDataItem;

/**
 * This background task will cycle through all the seconds in a day, creating a list of ClockDataItem's
 * sorted by the {@code badness()} results of each.
 */
public class ClockDataTask extends AsyncTask<ClockDataItem, ClockDataItem, ClockDataItem> {
    @Override
    protected ClockDataItem doInBackground(ClockDataItem... params) {
        return null;
    }

    @Override
    protected void onPostExecute(ClockDataItem aClockDataItem) {
        super.onPostExecute(aClockDataItem);
    }

    @Override
    protected void onProgressUpdate(ClockDataItem... values) {
        super.onProgressUpdate(values);
    }
}
