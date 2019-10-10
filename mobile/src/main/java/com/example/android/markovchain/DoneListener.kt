package com.example.android.markovchain

import android.app.Activity
import android.view.View
import android.widget.Toast

/**
 * This class allows an application to set a [DoneListener] for a background task so that that
 * background task can call our `onDoneDo` method to perform some action on the UI thread when
 * a background task is done. It is recommended to have the background task create a method
 * `void setDoneListener(doneListener: DoneListener, view: View)` which will save the arguments
 * in fields for later use when the background thread is done, then it can use that DoneListener
 * by calling `doneListener.onDone(view)` (`onDone` just creates a [Runnable] whose `run` override
 * calls our `onDoneDo(view: View)` with the `View` passed it on the UI thread).
 */
open class DoneListener {

    /**
     * This is the method a background thread should call to perform the action provided by the
     * possibly overridden method `onDoneDo(view: View)` on the UI thread. We simply call
     * `runOnUiThread` method of the `Context` that the [View] parameter [view] is running in,
     * passing it a new [Thread] instance created using a lambda [Runnable] whose `run()` override
     * simply calls `onDoneDo(view: View)` with the [View] passed it.
     *
     * @param view [View] to provide `Context` for a Toast or other use that needs a [View] or `Context`.
     */
    fun onDone(view: View) {
        (view.context as Activity).runOnUiThread(Thread(Runnable
        /**
         * When our Thread is run, we simply call `onDoneDo(View view)`, where
         * `View view` is the `View` passed to `onDone`.
         */
        {
            onDoneDo(view)
        }))

    }

    /**
     * This method can be overridden to perform most any action that needs to be performed on the UI
     * Thread when a background thread is done. The default implementation just displays a Toast
     * saying "I am done."
     *
     * @param view a [View] to provide `Context` for a Toast or other possible uses.
     */
    open fun onDoneDo(view: View) {
        Toast.makeText(view.context, "I am done.", Toast.LENGTH_LONG).show()
    }
}
