package com.example.android.markovchain;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.common.ClockDataItem;

import java.util.Calendar;

/**
 * This background task will cycle through all the seconds in a day, creating a list of ClockDataItem's
 * sorted by the {@code badness()} results of each.
 */
public class ClockDataTask extends AsyncTask<ClockDataItem, ClockDataItem, ClockDataItem> {
    Calendar now = Calendar.getInstance();
    int h = now.get(Calendar.HOUR_OF_DAY);
    int m = now.get(Calendar.MINUTE);
    double s = now.get(Calendar.SECOND);
    public ClockDataItem clock = new ClockDataItem(h, m, s);
    Context context;
    ViewGroup outputView;

    public ClockDataTask(Context c, ViewGroup viewGroup) {
        context = c;
        outputView = viewGroup;
    }
    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        publishProgress(clock);
        return clock;
    }

    /**
     * Runs on the UI thread after {@link #cancel(boolean)} is invoked and
     * {@link #doInBackground(Object[])} has finished.
     *
     * The default implementation simply invokes {@link #onCancelled()} and
     * ignores the result. If you write your own implementation, do not call
     * {@code super.onCancelled(result)}
     *
     * @param clockDataItem The result, if any, computed in {@link #doInBackground(Object[])}
     *                      , can be null
     *
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    @Override
    protected void onCancelled(ClockDataItem clockDataItem) {
        super.onCancelled(clockDataItem);
    }

    /**
     * Applications should preferably override {@link #onCancelled(Object)}.
     * This method is invoked by the default implementation of
     * {@link #onCancelled(Object)}.
     *
     * Runs on the UI thread after {@link #cancel(boolean)} is invoked and
     * {@link #doInBackground(Object[])} has finished.
     *
     * @see #onCancelled(Object)
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     * Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     *
     * This method won't be invoked if the task was cancelled.
     *
     * @param aClockDataItem The result of the operation computed by {@link #doInBackground}.
     *
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(ClockDataItem aClockDataItem) {
        TextView tv = new TextView(context);
        tv.setText(aClockDataItem.toString());
        outputView.addView(tv, 0);
        super.onPostExecute(aClockDataItem);
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     *
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(ClockDataItem... values) {
        super.onProgressUpdate(values);
    }
}
