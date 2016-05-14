package com.example.android.common;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * This class allows an application to set a DoneListener on a View, and override the
 * onDone method in an anonymous class to perform some action when a method in a seperate
 * thread is done
 */
public class DoneListener {

    public void onDone(final View view) {
        ((Activity) view.getContext()).runOnUiThread(new Thread(new Runnable()
        {
            @Override
            public void run() {
                Toast.makeText(view.getContext(), "I am done.", Toast.LENGTH_LONG).show();
            }
        }));

    }
}
