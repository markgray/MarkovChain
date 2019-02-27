package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An {@code AsyncTask} which loads the Html file with the resource ID specified by the parameter
 * passed to the method {@code doInBackground} in the background and returns a {@code Spanned} string
 * object to the caller.
 */
@SuppressWarnings("WeakerAccess")
public class WhatDataTask extends AsyncTask<Integer, String, Spanned> {
    /**
     * TAG used for logging
     */
    static final String TAG = "WhatDataTask";
    /**
     * {@code Context} to use to access resources from our application (in our case this is the
     * "context of the single, global Application object of the current process" obtained from the
     * {@code getApplicationContext} method of the {@code WhatIsMan} activity and then passed to our
     * constructor).
     */
    @SuppressLint("StaticFieldLeak")
    Context mContext;

    /**
     * Our constructor, we just save our parameter {@code Context context} in our field {@code mContext}.
     *
     * @param context {@code Context} to use to access resources of this application
     */
    WhatDataTask(Context context) {
        mContext = context;
    }

    /**
     * Loads a Html file from our resources on a background thread and returns a {@code Spanned} string
     * created from the contents of the file we load. The parameter is the resource ID of the file
     * passed to {@link #execute} by the caller of this task. First we initialize our variable
     * {@code StringBuilder builder} to null, and declare our variable {@code String line}. We initialize
     * {@code InputStream inputStream} by fetching a {@code Resources} instance for the application's
     * package as returned by the {@code getResources} method of our field {@code mContext}, and using
     * that {@code Resources} instance open a data stream for reading the raw resource with resource
     * ID {@code resourceId[0]}. Next we initialize {@code BufferedReader reader} with a buffering
     * character-input stream that uses a default-sized input buffer to read from an {@code InputStreamReader}
     * constructed to read bytes from {@code inputStream} and decode them into characters using  the
     * platform's default charset.
     * <p>
     * Having set everything up, wrapped in a try block intended to catch and log IOException, we set
     * our variable {@code int sizeOfInputStream} to an estimate of the number of bytes that can be
     * read from {@code inputStream} and allocate an initial capacity of 80 more than that value for
     * {@code builder}. We then loop setting {@code line} to the {@code String} returned by the
     * {@code readLine} method of {@code reader} until it returns null appending each {@code line}
     * to {@code builder}. When done reading the entire file into {@code builder} we close {@code reader}.
     * <p>
     * Upon exiting from the try block we return the {@code Spanned} string created by the {@code fromHtml}
     * method of {@code Html} from the string value of {@code builder}.
     *
     * @param resourceId the resource ID of the Html file we are to load.
     * @return A {@code Spanned} string created from the contents of the file we load.
     */
    @Override
    protected Spanned doInBackground(Integer... resourceId) {
        StringBuilder builder = null;
        String line;
        int sizeOfInputStream = 0;
        final InputStream inputStream = mContext
                .getResources()
                .openRawResource(resourceId[0]);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            sizeOfInputStream = inputStream.available(); // Get the size of the stream
            builder = new StringBuilder(sizeOfInputStream + 80);
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "sizeOfInputStream: " + sizeOfInputStream);
        assert builder != null;
        return Html.fromHtml(builder.toString());
    }
}
