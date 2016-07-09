package com.example.android.common;

import android.os.AsyncTask;

/**
 * Background task to be benchmarked
*/
public class CalcTask extends AsyncTask<Long, Long, Double> {

    /**
     * Override this method to perform a computation on a background thread.
     * The specified parameters are the parameters passed to execute(Params...)
     * by the caller of this task. This method can call publishProgress(Progress...)
     * to publish updates on the UI thread.
     *
     * @param reps Number of repetitions the for loop should execute
     * @return Result of the computation.
     */
    @Override
    protected Double doInBackground(Long... reps) {
        Long repeats = reps[0];
        double acc = 1.000000001;
        double div = 0.999999999;
        for (int i = 0; i < repeats ; i++) {
            acc = acc / div;
        }
        return acc;
    }

}
