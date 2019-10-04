package com.example.android.markovchain;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * This class allows an application to set a {@code DoneListener} for a background task so that that
 * background task can call our {@code onDoneDo} method to perform some action on the UI thread when
 * a background task is done. It is recommended to have the background task create a method
 * {@code void setDoneListener(DoneListener doneListener, View view)} which will save the arguments
 * in fields for later use when the background thread is done, then it can use that DoneListener
 * by calling {@code doneListener.onDone(view)} ({@code onDone} just creates a {@code Runnable} whose
 * {@code run} override calls our {@code onDoneDo(View view)} with the {@code View} passed it on the
 * UI thread).
 */
public class DoneListener {

    /**
     * This is the method a background thread should call to perform the action provided by the
     * possibly overridden method {@code onDoneDo(View view)} on the UI thread. We simply call
     * {@code runOnUiThread} method of the {@code Context} that the parameter {@code View view} is
     * running in passing it a new {@code Thread} instance created using an anonymous {@code Runnable}
     * class whose {@code run()} override simply calls {@code onDoneDo(View view)} with the {@code View}
     * passed it.
     *
     * @param view View to provide Context for a Toast or other use that needs a View or Context.
     */
    @SuppressWarnings("WeakerAccess")
    public void onDone(final View view) {
        ((Activity) view.getContext()).runOnUiThread(new Thread(new Runnable() {
            /**
             * When our Thread is run, we simply call {@code onDoneDo(View view)}, where
             * {@code View view} is the {@code View} passed to {@code onDone}.
             */
            @Override
            public void run() {
                onDoneDo(view);
            }
        }));

    }

    /**
     * This method can be overridden to perform most any action that needs to be performed on the UI
     * Thread when a background thread is done. The default implementation just displays a Toast saying
     * "I am done."
     *
     * @param view a {@code View} to provide {@code Context} for a Toast or other possible uses.
     */
    public void onDoneDo(View view) {
        Toast.makeText(view.getContext(), "I am done.", Toast.LENGTH_LONG).show();
    }
}
