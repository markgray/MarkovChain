package com.example.android.markovchain;

import android.os.AsyncTask;

/**
 * This background task will cycle through all the seconds in a day, creating a list of ClockDataItem's
 * sorted by the {@code badness()} results of each.
 */
public class ClockDataTask extends AsyncTask<Double, Double, Double> {
    @Override
    protected Double doInBackground(Double... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Double aDouble) {
        super.onPostExecute(aDouble);
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
    }
}
