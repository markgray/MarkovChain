package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("WeakerAccess")
public class WhatDataTask extends AsyncTask<Integer, String, Spanned> {
    @SuppressLint("StaticFieldLeak")
    Context mContext;
    WhatDataTask(Context context) {
        mContext = context;
    }
    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param resourceId The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Spanned doInBackground(Integer... resourceId) {
        final StringBuilder builder = new StringBuilder();
        String line;
        final InputStream inputStream = mContext
                .getResources()
                .openRawResource(resourceId[0]);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Html.fromHtml(builder.toString());
    }
}
