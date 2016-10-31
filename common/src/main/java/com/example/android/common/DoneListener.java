package com.example.android.common;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * This class allows an application to set a DoneListener on a View, and override the
 * onDone method in an anonymous class to perform some action on the UI thread when a
 * method in a separate thread is done. It is recommended to create a method
 * void setDoneListener(DoneListener doneListener, View view) which will save the arguments
 * in fields for later use when background thread is done, then you can use that DoneListener
 * by calling doneListener.onDone(view)
 */
public class DoneListener {

    /**
     * This is the method a background thread should call to perform the action provided in the
     * possibly overridden method onDoneDo(View view). We simply call runOnUiThread on a new
     * Thread instance created using an anonymous Runnable class whose run() method simply calls
     * onDoneDo(View view).
     *
     * @param view View to provide Context for a Toast or other use that needs a View or Context.
     */
    public void onDone(final View view) {
        ((Activity) view.getContext()).runOnUiThread(new Thread(new Runnable()
        {
            /**
             * When our Thread is run, we simply call onDoneDo(View view), where View view is
             * the View passed to onDone.
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
     * @param view a View for Context for a Toast or other possible uses.
     */
    public void onDoneDo(View view) {
        Toast.makeText(view.getContext(), "I am done.", Toast.LENGTH_LONG).show();
    }
}
