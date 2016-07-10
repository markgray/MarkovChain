package com.example.android.common;

import android.os.AsyncTask;

/**
 * Background task to be benchmarked
*/
public class CalcTask extends AsyncTask<Long, Long, Long> {

    public BenchMark benchMark = new BenchMark();
    double acc = 1.000000001;
    double div = 0.999999999;

    /**
     * This method performs a computation contained in the method testMethod
     * reps times on a background thread.
     * The specified parameters are the parameters passed to execute(Params...)
     * by the caller of this task. This method can call publishProgress(Progress...)
     * to publish updates on the UI thread.
     *
     * @param reps Number of repetitions the for loop should execute
     * @return Time in milliseconds that the benchmark took
     */
    @Override
    protected Long doInBackground(Long... reps) {
        Long repeats = reps[0];

        benchMark.Start();
        for (int i = 0; i < repeats ; i++) {
            testMethod();
        }
        return benchMark.Stop();
    }

    /**
     * This method should be overridden by a method which performs whatever computation
     * you wish to benchmark.
     */
    public void testMethod() {
        acc = acc / div;
    }

}
