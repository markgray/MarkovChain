package com.example.android.common;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Background task to benchmark
*/
public class CalcTask extends AsyncTask<Long, Long, Long> {
    private final static String TAG = "CalcTask"; // TAG for logging

    @SuppressWarnings("WeakerAccess")
    public BenchMark benchMark = new BenchMark(); // Create a BenchMark timer instance
    private double acc = 1.000000001; // Start value for example use
    @SuppressWarnings("FieldCanBeLocal")
    private double div = 0.999999999; // Dividend for example use

    /**
     * This method performs a computation contained in the method testMethod
     * reps[0]*reps[1] times on a background thread.
     * The specified parameters are the parameters passed to execute(Long...)
     * by the caller of this task. This method calls publishProgress(Long...)
     * to publish updates on the UI thread.
     *
     * @param reps [0] Number of repetitions the for loop should run for each publish period
     *             [1] Number of publish progress periods
     *
     * @return Time in milliseconds that the benchmark took
     */
    @Override
    protected Long doInBackground(Long... reps) {
        Long repeats = reps[0];
        Long publish = reps[1];
        int totalNumber = 0;

        benchMark.start();
        for (int j=0; j < publish; j++) {
            for (int i = 0; i < repeats ; i++) {
                totalNumber++;
                testMethod();
            }
            publishProgress((long) j);
        }
        publishProgress(publish);
        Log.i(TAG, "Total number of iterations: " + totalNumber);
        return benchMark.stop();
    }

    /**
     * Just an example calculation to run a benchmark on. This method should be overridden by a
     * method which performs whatever computation you wish to benchmark.
     */
    public void testMethod() {
        acc = acc / div;
    }

    /**
     * Runs on the UI thread after publishProgress(Long...) is invoked.
     * The specified values are the values passed to publishProgress(Long...).
     * Override this to advance a progress bar
     *
     * @param progress The values indicating progress.
     */
    @Override
    protected void onProgressUpdate(Long... progress) {
    }

    /**
     * Runs on the UI thread after doInBackground(Long...).
     * The specified result is the value returned by doInBackground(Long...).
     * This method won't be invoked if the task was cancelled.
     * Override this to make use of the elapsed time value returned.
     *
     * @param result The elapsed time the benchmark took.
     */
    @Override
    protected void onPostExecute(Long result) {
    }

}
