package com.example.android.markovchain

import android.annotation.TargetApi
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.DialogFragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

/**
 * Just a test Activity for experimenting with retained fragments.
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "MemberVisibilityCanBePrivate")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class FragmentVersionSkeleton : FragmentActivity() {
    /**
     * Handle to the [FragmentManager] for interacting with fragments associated with this activity
     */
    lateinit var mFragmentManager: FragmentManager

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * `onCreate`. If this is the first time we are called (our [Bundle] parameter [savedInstanceState]
     * is *null*) we need to:
     *  1. Get the [FragmentManager] for interacting with fragments associated with this activity
     *  to initialize our [FragmentManager] field [mFragmentManager]
     *  2. Start a series of edit operations on the Fragments associated with this [FragmentManager].
     *  3. Add a new instance of our [UiFragment] fragment to the activity state
     *  4. Schedule a commit of this transaction. The commit does not happen immediately; it will
     *  be scheduled as work on the main thread to be done the next time that thread is ready.
     *
     * If we are being recreated after an orientation change or other event, then the fragment api
     * will have saved its state in [savedInstanceState] so it will not be null. (We however do
     * not override [onSaveInstanceState], so there is nothing in the Bundle we need bother with.)
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data that was most
     * recently supplied in [.onSaveInstanceState]. In addition FragmentManager
     * will save information in the Bundle so it will be non-null when we are recreated
     * as the result of an orientation change iff we contain a retained Fragment.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFragmentManager = supportFragmentManager

        // First time init, create the UI.
        if (savedInstanceState == null) {
            mFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, UiFragment(mFragmentManager))
                    .commit()
        } else {
            Log.i(TAG, "Bundle savedInstanceState is not null")
        }
    }

    /**
     * Create and show a [MyDialogFragment] DialogFragment. First we use our [FragmentManager] field
     * [mFragmentManager] to create a `FragmentTransaction` to initialize our variable `val ft` and
     * then use `ft` to begin a series of fragment transactions on the Fragment's associated with our
     * [FragmentManager]. Next we initialize our [Fragment] variable `val prev` by having our field
     * [mFragmentManager] search for a fragment with the tag "dialog". If `prev` is not null after
     * this we use `ft` to remove `prev`. We next tell `ft` to add the transaction to the back stack.
     * Next we initialize our [DialogFragment] variable `val newFragment` with a new instance of
     * [MyDialogFragment] using some dummy data for its label and text. Finally we instruct
     * `newFragment` to show itself using the tag "dialog".
     */
    internal fun showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        val ft = mFragmentManager.beginTransaction()
        val prev = mFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = MyDialogFragment.newInstance(
                "This is label",
                "This is the text"
        )
        newFragment.show(ft, "dialog")
    }

    /**
     * This is a fragment showing the UI that will be updated from work done
     * in the retained fragment.
     */
    class UiFragment internal constructor(internal var mFM: FragmentManager) : Fragment() {
        /**
         * A reference to our example retained [Fragment]
         */
        internal var mWorkFragment: RetainedFragment? = null
        /**
         * [View] containing our layout file
         */
        internal lateinit var mView: View

        /**
         * [DoneListener] for our [RetainedFragment] field [mWorkFragment]. When [mWorkFragment] is
         * done it calls the `onDone` method of `DoneListener` which in turn calls our override
         * of `onDoneDo` which changes the visibility of the `LinearLayout` with id
         * R.id.progress_view_linear_layout from VISIBLE to GONE, and the visibility of the [TextView]
         * with id R.id.main_view from GONE to VISIBLE.
         */
        internal var mIamDone: DoneListener = object : DoneListener() {
            /**
             * Called by [DoneListener.onDone]. First we locate the `TextView` with id
             * R.id.main_view in our parameter `View v` and save a reference in our variable
             * `TextView mMainView`, then we locate the `LinearLayout` with id
             * R.id.progress_view_linear_layout and save a reference to it in our variable
             * `LinearLayout mProgressViewLinearLayout`. Finally we set the visibility of
             * `mProgressViewLinearLayout` to GONE, and the visibility of `mMainView`
             * to visible.
             *
             * @param view View of our inflated layout file.
             */
            override fun onDoneDo(view: View) {
                val mMainView = view.findViewById<TextView>(R.id.main_view)
                val mProgressViewLinearLayout = view.findViewById<LinearLayout>(R.id.progress_view_linear_layout)
                mProgressViewLinearLayout.visibility = View.GONE
                mMainView.visibility = View.VISIBLE
            }
        }

        /**
         * Called to have the fragment instantiate its user interface view. First we use the
         * `LayoutInflater inflater` passed us to inflate our layout file R.layout.fragment_retain_instance
         * saving a reference to it in our field `View mView`. We locate the `Button` with
         * id R.id.restart ("RESTART") and set its `OnClickListener` to an anonymous class which
         * will instruct our field `RetainedFragment mWorkFragment` to restart itself. Then we
         * locate the `Button` with id R.id.toast_me ("TOAST VALUE") and set its `OnClickListener`
         * to an anonymous class which creates and shows a Toast displaying the value of the static field
         * `int mPosition` of `RetainedFragment`. Next it locates the view with id
         * R.id.main_view (which has a visibility of GONE until `mWorkFragment` flips the visibility
         * of the view containing the `ProgressBar` to GONE and it to  VISIBLE btw) and set its
         * `OnLongClickListener` to an anonymous class which displays an informative Toast and instructs
         * our `FragmentVersionSkeleton` `Activity`  to show its dialog. Finally we return
         * our field `mView` to our caller.
         *
         * @param inflater The LayoutInflater object that can be used to inflate
         * any views in the fragment,
         * @param container If non-null, this is the parent view that the fragment's UI
         * should be attached to.  The fragment should not add the view itself,
         * but this can be used to generate the LayoutParams of the view.
         * @param savedInstanceState If non-null, this fragment is being re-constructed
         * from a previous saved state as given here.
         *
         * @return Return the View for the fragment's UI, or null.
         */
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            mView = inflater.inflate(R.layout.fragment_retain_instance, container, false)
            return mView
        }

        /**
         * Called immediately after [.onCreateView]
         * has returned, but before any saved state has been restored in to the view.
         * This gives subclasses a chance to initialize themselves once
         * they know their view hierarchy has been completely created.  The fragment's
         * view hierarchy is not however attached to its parent at this point.
         *
         * @param view               The View returned by [.onCreateView].
         * @param savedInstanceState If non-null, this fragment is being re-constructed
         */
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            // Watch for button clicks.
            var button = mView.findViewById<Button>(R.id.restart)
            /**
             * Called when the R.id.restart ("RESTART") `Button` is clicked. We simply instruct
             * our field `RetainedFragment mWorkFragment` to restart the count at zero by calling
             * its `restart()` method.
             *
             * Parameter: View of Button that was clicked
             */
            button.setOnClickListener {
                mWorkFragment!!.restart()
            }

            button = mView.findViewById(R.id.toast_me)
            /**
             * Called when the R.id.toast_me ("TOAST VALUE") `Button` is clicked. We simply
             * show a Toast displaying the current value of the static `int mPosition` field of
             * `RetainedFragment`.
             *
             * Parameter: View of Button that was clicked.
             */
            button.setOnClickListener {
                Toast.makeText(activity, "The value is:" + RetainedFragment.mPosition, Toast.LENGTH_LONG).show()
            }

            val textView = mView.findViewById<TextView>(R.id.main_view)
            textView.setOnLongClickListener {
                Toast.makeText(activity, "I have been long clicked", Toast.LENGTH_LONG).show()

                (activity as FragmentVersionSkeleton).showDialog()
                true
            }
        }

        /**
         * Called when the fragment's activity has been created and this
         * fragment's view hierarchy instantiated.  It can be used to do final
         * initialization once these pieces are in place, such as retrieving
         * views or restoring state.  It is also useful for fragments that use
         * [.setRetainInstance] to retain their instance,
         * as this callback tells the fragment when it is fully associated with
         * the new activity instance.  This is called after [.onCreateView]
         * and before [.onViewStateRestored].
         *
         *
         * First we call through to our super's implementation of `onActivityCreated`. Next we
         * set our variable `android.app.FragmentManager fm` to the FragmentManager for interacting
         * with fragments associated with this fragment's activity. Then we use `fm` to look
         * for a fragment with the tag "work" and use this value to set our field
         * `RetainedFragment mWorkFragment`. If null was returned (the `Fragment` was not
         * found) we create a new instance of `RetainedFragment` for `mWorkFragment`, set
         * 'this' to be the target Fragment for `mWorkFragment`, and then use `fm` to start
         * a series of edit operations on the Fragments associated with this `FragmentManager`,
         * use the `FragmentTransaction` returned to add the Fragment `mWorkFragment` to
         * the Activity with the Tag "work", with the method chain terminating with a call to `commit()`
         * to schedule a commit of this transaction. Finally we call the `setDoneListener` method
         * of `mWorkFragment` to set the `DoneListener` of `mWorkFragment` to the
         * to field `DoneListener mIamDone` which swaps the visibility of the `TextView`
         * with id R.id.main_view and the `LinearLayout` with id R.id.progress_view_linear_layout.
         * (The two share the same space in a `FrameLayout`, and R.id.progress_view_linear_layout
         * starts out with a visibility of VISIBLE which is changed to GONE, and R.id.main_view starts
         * out with a visibility of GONE which is changed to VISIBLE.)
         *
         * @param savedInstanceState we do not override onSaveInstanceState so ignore the value
         */
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            // Check to see if we have retained the worker fragment.
            mWorkFragment = mFM.findFragmentByTag("work") as RetainedFragment?

            // If not retained (or first time running), we need to create it.
            if (mWorkFragment == null) {
                mWorkFragment = RetainedFragment()
                // Tell it who it is working with.
                mWorkFragment!!.setTargetFragment(this, 0)
                mFM.beginTransaction().add(mWorkFragment!!, "work").commit()
            }
            mWorkFragment!!.setDoneListener(mIamDone, mView)
        }
    }

    /**
     * This is the Fragment implementation that will be retained across
     * activity instances.  It represents some ongoing work, here a thread
     * we have that sits around incrementing a progress indicator.
     */
    class RetainedFragment : Fragment() {
        /**
         * `ProgressBar` in our layout that we update to the latest value of `mPosition`
         */
        internal var mProgressBar: ProgressBar? = null
        /**
         * Flag set by our `UIFragment` to start (true) and stop (false) our work thread
         */
        internal var mReady = false
        /**
         * Flag set in `onDestroy` callback to stop our work thread
         */
        internal var mQuiting = false
        /**
         * `LinearLayout` in our layout that contains our `ProgressBar`
         */
        internal lateinit var mProgressViewLinearLayout: LinearLayout
        /**
         * `TextView` in our layout that is used for results
         */
        internal lateinit var mMainView: TextView
        /**
         * `DoneListener` instance used when our work thread finishes
         */
        private var doneListener: DoneListener? = null
        /**
         * `View` passed to `setDoneListener`, used as the parameter when calling the
         * `onDone` method of `DoneListener`.
         */
        private var mView: View? = null
        /**
         * This is the `Thread` that will do our work. It sits in a loop incrementing the
         * `ProgressBar` until it has reached the top, then stops and waits until killed.
         */
        internal val mThread: Thread = object : Thread() {
            /**
             * We override the `run()` method of `Thread` to provide the code that runs
             * when the background thread is started after we call `mThread.start()` in our
             * `onCreate` override. First we initialize our variable `int max` by retrieving
             * the upper limit of the range of `ProgressBar mProgressBar`, (which is set by the
             * attribute android:max="500" in the layout file). Then we loop forever using 'this' to
             * synchronize with the UI Thread. In our first synchronized block we check whether our
             * field `mReady` is false (the UI thread is not ready for us to continue) or our
             * counter `mPosition` is >= `max` (our work is done) and if either condition
             * holds we check whether the flag `mQuiting` is true, and if it is we return ending
             * this thread (`mQuiting` is set in `onDestroy`). If `mQuiting` has not
             * been set we have finished our work (`mPosition` >= `max`) so we call the
             * `onDone` method of our field `DoneListener doneListener` which swaps the
             * visibility of views from the `ProgressBar` we have been moving to the main View
             * (results View if you would). If the UI is ready for us to continue AND we are not yet
             * finished with our "work", we increment our counter `mPosition` and set `mProgressBar`
             * to this position. In the second synchronized block we simply `wait` for 50 milliseconds
             * before continuing our infinite loop.
             */
            override fun run() {
                val max = 500
                Log.i(TAG, "mPosition value:$mPosition")
                Log.i(TAG, "mReady value:$mReady")

                // This thread runs almost forever.
                while (true) {

                    // Update our shared state with the UI.
                    synchronized(this) {
                        // Our thread is stopped if the UI is not ready
                        // or we have completed our work.
                        while (!mReady || mPosition >= max) {
                            if (mQuiting) {
                                return
                            }
                            doneListener!!.onDone(mView!!)

                            try {
                                (this as Object).wait()
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }

                        }
                        // Now update the progress.  Note it is important that
                        // we touch the progress bar with the lock held, so it
                        // doesn't disappear on us.
                        mPosition++
                        mProgressBar!!.progress = mPosition
                    }

                    // Normally we would be doing some work, but put a kludge
                    // here to pretend like we are.
                    synchronized(this) {
                        try {
                            (this as Object).wait(50)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }

        /**
         * Fragment initialization. First we call through to our super's implementation of `onCreate`,
         * then we call `setRetainInstance(true)` to indicate to the system that this fragment instance
         * should be retained across Activity re-creation (such as from a configuration change), and
         * finally we start our thread `Thread mThread` running.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so are not interested
         * in it, and even if we did override onSaveInstanceState onCreate would not be called
         * again because we call setRetainInstance(true) and our fragment will not need to be
         * recreated.
         */
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Tell the framework to try to keep this fragment around
            // during a configuration change.
            retainInstance = true
            // Start up the worker thread.
            mThread.start()
        }

        /**
         * This is called when the Fragment's Activity is ready to go, after its content view has
         * been installed. It is called both after the initial fragment creation and after the
         * fragment is re-attached to a new activity. First we call through to our super's
         * implementation of `onActivityCreated`, then we fetch a reference to our UIFragment
         * to initialize our variable `Fragment targetFragment` by calling `getTargetFragment`.
         * We initialize our variable `View gotView` to null, and if `targetFragment` is
         * not null we set `gotView` to the root view for the layout of `targetFragment`
         * (the one returned by [.onCreateView]). If we have successfully set `gotView`
         * is not null we set our field `ProgressBar mProgressBar` to the view in `gotView`
         * with id R.id.progress_horizontal, our field `LinearLayout mProgressViewLinearLayout`
         * to the view in `gotView` with id R.id.progress_view_linear_layout, and our field
         * `TextView mMainView` to the view in `gotView` with id R.id.main_view. Finally
         * synchronized on `Thread mThread` we set our field `mReady` to true, and call
         * the `notify()` method of `mThread` to wake that thread up (it is currently
         * waiting on its 'this') allowing it to run after our synchronized statement completes and
         * releases the lock on mThread.
         *
         * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
         */
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            Log.i(TAG, "onActivityCreated has been called")

            // Retrieve the progress bar from the target's view hierarchy.
            val targetFragment = targetFragment
            val gotView: View?
            if (targetFragment != null) {
                gotView = targetFragment.view
            } else {
                Log.e(TAG, "targetFragment is null")
                throw RuntimeException("targetFragment is null")
            }
            if (gotView == null) {
                Log.e(TAG, "gotView is null")
                throw RuntimeException("gotView is null")
            }

            mProgressBar = gotView.findViewById(R.id.progress_horizontal)
            if (mProgressBar == null) {
                Log.e(TAG, "mProgressBar is null")
                throw RuntimeException("mProgressBar is null")
            }
            mProgressViewLinearLayout = gotView.findViewById(R.id.progress_view_linear_layout)
            mMainView = gotView.findViewById(R.id.main_view)

            // We are ready for our thread to go.
            synchronized(mThread) {
                mReady = true
                (mThread as Object).notify()
            }
        }

        /**
         * This is called when the fragment is going away. It is NOT called when the fragment is
         * being propagated between activity instances (such as after an orientation change). In
         * a synchronized statement using `Thread mThread` as its intrinsic lock, we
         * set the `boolean mReady` flag to false (a signal to our worker thread that it
         * should check the flag `boolean mQuiting` when it is awoken by `notify()`), set
         * the `boolean mQuiting` flag to true (a signal to our worker thread that its
         * `run()` method should exit by returning to its caller), and then awaken our worker
         * `Thread mThread` (which is `wait()`'ing on its 'this' which is locked while
         * we are in our synchronized block) by calling the `notify()` method of `mThread`.
         * Finally we call through to our super's implementation of `onDestroy()`.
         */
        override fun onDestroy() {
            // Make the thread go away.
            synchronized(mThread) {
                mReady = false
                mQuiting = true
                (mThread as Object).notify()
            }

            super.onDestroy()
        }

        /**
         * This is called right before the fragment is detached from its current activity instance.
         * In a synchronized block using `Thread mThread` as its intrinsic lock, we set the field
         * `ProgressBar mProgressBar` to null because the new Activity will need to find its own
         * version of `mProgressBar` in onActivityCreated before it can use it. We then set
         * `mReady` to false to signal to our "worker" `Thread` that it should wait until
         * the new Activity is created before it continues, then awaken our worker `Thread mThread`
         * is currently `wait()`'ing for its 'this' to be unlocked by our exiting the synchronized
         * block) by calling the `notify()` method of `mThread`. Finally we call through
         * to our super's implementation of `onDetach()`.
         */
        override fun onDetach() {
            // This fragment is being detached from its activity.  We need
            // to make sure its thread is not going to touch any activity
            // state after returning from this function.
            synchronized(mThread) {
                mProgressBar = null
                mReady = false
                (mThread as Object).notify()
            }

            super.onDetach()
        }

        /**
         * Sets our fields `DoneListener doneListener`, and `View view` which are used by
         * our work thread when it finishes its task.
         *
         * @param doneListener DoneListener to use when work thread is done
         * @param view View to pass to doneListener.onDone(View) when work thread is done.
         */
        fun setDoneListener(doneListener: DoneListener, view: View) {
            this.mView = view
            this.doneListener = doneListener
        }

        /**
         * API for our UI to restart the progress thread when the `Button` R.id.restart ("RESTART")
         * is clicked. In a synchronized block using `Thread mThread` as the intrinsic lock, we
         * set the field `int mPosition` to 0, then awaken the worker `Thread mThread`
         * (which is currently waiting for us to release its 'this') by calling the `notify()`
         * method of `mThread`.
         */
        fun restart() {
            synchronized(mThread) {
                mPosition = 0
                (mThread as Object).notify()
            }
        }

        companion object {
            /**
             * TAG used for logging.
             */
            private const val TAG = "RetainedFragment"
            /**
             * Counter that we increment and use to set the progress of our `ProgressBar mProgressBar`
             */
            var mPosition: Int = 0
        }
    }

    /**
     * This is a skeletal `DialogFragment` which was just stuck into this Activity in order to
     * experiment with using `DialogFragment`'s. When `show()` is called it just displays
     * the label and text that were passed as parameters to its `newInstance` method.
     */
    class MyDialogFragment : DialogFragment() {
        /**
         * "label" to display
         */
        internal var mLabel: String? = null
        /**
         * "text" to display
         */
        internal var mText: String? = null

        /**
         * Called to do initial creation of a fragment. First we call through to our super's
         * implementation of `onCreate`, then we set our field `mLabel` to the argument
         * stored in our argument `Bundle` by `newInstance` under the key "label", and
         * our field `mText` to the argument it stored under the key "text". Finally we set
         * the style of our `DialogFragment` to STYLE_NORMAL.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         */
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            mLabel = arguments!!.getString("label")
            mText = arguments!!.getString("text")

            // Pick a style based on the num.
            val theme = 0
            setStyle(STYLE_NORMAL, theme)
        }

        /**
         * Called to have the fragment instantiate its user interface view. First we initialize our
         * variable `View v` by using our parameter `LayoutInflater inflater` to inflate
         * our layout file R.layout.frag_vers_skel_dialog using our parameter `ViewGroup container`
         * for the layout params without attaching to it, then we initialize our variable `TextView tv`
         * by finding the view in `v` with the id R.id.label and set its text to the contents
         * of our field `String mLabel`, then we set `tv` to the view in `v` we find
         * with the R.id.text and set its text to the contents of our field `String mText`. We
         * next initialize our variable `Button button` by finding the view in `v` with
         * id R.id.dismiss ("DISMISS") and set its `OnClickListener` to an anonymous class which
         * simply dismisses this dialog by calling the `dismiss()` method. Finally we return
         * `v` to the caller.
         *
         * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
         * @param container If non-null, this is the parent view that the fragment's UI should be
         * attached to. The fragment should not add the view itself, but this can be used to
         * generate the LayoutParams of the view.
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         *
         * @return Return the View for the fragment's UI.
         */
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater.inflate(R.layout.frag_vers_skel_dialog, container, false)

            var tv = v.findViewById<TextView>(R.id.label)
            tv.text = mLabel

            tv = v.findViewById(R.id.text)
            tv.text = mText

            // Watch for button clicks.
            val button = v.findViewById<Button>(R.id.dismiss)
            /**
             * Called when the "DISMISS" `Button` is clicked, we just call the `dismiss()`
             * method to dismiss our dialog.
             *
             * Parameter: `View` that was clicked
             */
            button.setOnClickListener {
                dismiss()
            }

            return v
        }

        companion object {

            /**
             * Create a new instance of `MyDialogFragment` providing values for "label" and "text"
             * in its argument `Bundle`. First we initialize `MyDialogFragment f` with a new
             * instance, then we initialize `Bundle args` with a new instance. We store our parameter
             * `String label` in `args` under the key "label" and our parameter `String text`
             * under the key "text". We then set the construction arguments for `f` to `args`
             * and return `f` to the caller.
             *
             * @param label Label to use for dialog
             * @param text  Text body for dialog
             * @return A MyDialogFragment instance with the arguments set
             */
            internal fun newInstance(label: String, text: String): MyDialogFragment {
                val f = MyDialogFragment()

                // Supply num input as an argument.
                val args = Bundle()
                args.putString("label", label)
                args.putString("text", text)

                f.arguments = args

                return f
            }
        }
    }

    companion object {
        /**
         * TAG used for logging
         */
        internal const val TAG = "FragmentVersionSkeleton"
    }


}
