package com.example.android.markovchain;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Background task to benchmark. This {@code AsyncTask} allows you time run a benchmark of a code
 * fragment when you extend it in a subclass that overrides the {@code testMethod()} method.
 */
public class CalcTask extends AsyncTask<Long, Long, Long> {
    /**
     * TAG for logging
     */
    private final static String TAG = "CalcTask";

    /**
     * {@code BenchMark} timer instance we use to time our {@code doInBackground} method.
     */
    private BenchMark benchMark = new BenchMark();
    /**
     * Constant start value for the use of our example {@code testMethod()}
     */
    private double acc = 1.000000001;
    /**
     * Constant dividend value for the use of our example {@code testMethod()}.
     */
    private final static double div = 0.999999999;

    /**
     * This method performs a computation contained in the method {@code testMethod()} {@code reps[0]}
     * times {@code reps[1]} times on a background thread. The parameters {@code Long... reps} are
     * the parameters passed to {@code execute(Long...)} by the user of this task. This method calls
     * {@code publishProgress(Long...)} every {@code reps[0]} iterations to publish updates on the
     * UI thread.
     * <p>
     * First we retrieve a local copy of our parameter {@code reps[0]} into {@code Long repeats} and
     * our parameter {@code reps[1]} into {@code Long publish} in order to make the code more readable.
     * We then initialize our count of repetitions performed {@code int totalNumber} to 0. Then we
     * call the {@code start()} method of {@code BenchMark benchMark} to have it set itself to the
     * current system time in milliseconds.
     * <p>
     * We now loop over {@code int j} for the {@code publish} times we are to publish progress, and
     * in an inner loop we loop over {@code int j} for the {@code repeats} times we are to loop between
     * each call to {@code publishProgress} incrementing {@code totalNumber} each time before calling
     * the {@code testMethod()} method. Every time we complete the inner loop we call {@code publishProgress}
     * with the current value of {@code j} and loop around for the next {@code j}.
     * <p>
     * When done looping we call {@code publishProgress} once more with the value of {@code publish}
     * then return the elapsed time of our run that the {@code stop()} method of {@code benchMark}
     * returns to the caller.
     *
     * @param reps {@code reps[0]} Number of repetitions the for loop should run between each call
     *             to {@code publishProgress} {@code reps[1]} Number of times to publish progress.
     * @return Time in milliseconds that the benchmark took
     */
    @Override
    protected Long doInBackground(Long... reps) {
        Long repeats = reps[0];
        Long publish = reps[1];
        int totalNumber = 0;

        benchMark.start();
        for (int j = 0; j < publish; j++) {
            for (int i = 0; i < repeats; i++) {
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
     * Runs on the UI thread after {@code publishProgress(Long...)} is invoked. The specified values
     * are the values passed to {@code publishProgress(Long...)}. Override this to advance a progress
     * bar.
     *
     * @param progress The values indicating progress.
     */
    @Override
    protected void onProgressUpdate(Long... progress) {
    }

    /**
     * Runs on the UI thread after {@code doInBackground(Long...)}. The specified result is the value
     * returned by {@code doInBackground(Long...)}. This method won't be invoked if the task was
     * cancelled. Override this to make use of the elapsed time value we return.
     *
     * @param result The elapsed time the benchmark took.
     */
    @Override
    protected void onPostExecute(Long result) {
    }

}
