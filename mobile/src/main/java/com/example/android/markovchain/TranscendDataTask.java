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
import java.util.ArrayList;
import java.util.List;

/**
 * An {@code AsyncTask} which loads the utf8 text file with the resource ID specified by the parameter
 * passed to the method {@code doInBackground} in the background and returns a list of strings that
 * correspond to each of the paragraphs of the text file to the caller.
 */
@SuppressWarnings("WeakerAccess")
public class TranscendDataTask extends AsyncTask<Integer, String, List<String>> {
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
    TranscendDataTask(Context context) {
        mContext = context;
    }

    /**
     *
     */
    @Override
    protected List<String> doInBackground(Integer... resourceId) {
        StringBuilder builder = new StringBuilder();
        String line;
        List<String> results = new ArrayList<>();
        final InputStream inputStream = mContext
                .getResources()
                .openRawResource(resourceId[0]);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {

            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    if (builder.length() != 0){
                        builder.append("\n");
                        results.add(builder.toString());
                        builder.setLength(0);
                    } else {
                        results.add("\n");
                    }
                } else {
                    builder.append(line + " ");
                    if (line.startsWith(" ")){
                        builder.append("\n");
                    }
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
