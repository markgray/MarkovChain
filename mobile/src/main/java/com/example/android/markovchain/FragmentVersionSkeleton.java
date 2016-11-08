package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.common.DoneListener;

/**
 * Just a test Activity for experimenting with retained fragments.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentVersionSkeleton extends Activity {
    final String TAG = "FragmentVersionSkeleton";

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate. If this is the first time we are called (Bundle savedInstanceState == null) we need
     * to:
     *     1. Get the FragmentManager for interacting with fragments associated with this activity.
     *     2. Start a series of edit operations on the Fragments associated with this FragmentManager.
     *     3. Add a new instance of our UiFragment fragment to the activity state
     *     4. Schedule a commit of this transaction. The commit does not happen immediately; it will
     *        be scheduled as work on the main thread to be done the next time that thread is ready.
     *
     * If we are being recreated after an orientation change or other event, then the fragment api
     * will have saved its state in Bundle savedInstanceState so it will not be null. (We however do
     * not override onSaveInstanceState, so there is nothing in the Bundle we need bother with.)
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *        previously being shut down then this Bundle contains the data that was most
     *        recently supplied in {@link #onSaveInstanceState}. In addition FragmentManager
     *        will save information in the Bundle so it will be non-null when we are recreated
     *        as the result of an orientation change iff we contain a retained Fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First time init, create the UI.
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new UiFragment())
                    .commit();
        } else {
            Log.i(TAG, "Bundle savedInstanceState is not null");
        }
    }

    /**
     * Create and show a MyDialogFragment DialogFragment. First we get the FragmentManager and
     * instruct it to create a FragmentTransaction ft and to begin a series of fragment transactions
     * on the Fragment's associated with this FragmentManager. Next we ask the FragmentManager to
     * search for a fragment with the tag "dialog" and save the results in Fragment prev. If prev
     * is not null we use FragmentTransaction ft to remove that Fragment. We next tell ft to add
     * the transaction to the back stack. Next we create a new instance of DialogFragment newFragment
     * using some dummy data for its label and text. Finally we instruct DialogFragment newFragment
     * to show itself using the tag "dialog".
     */
    void showDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance("This is label", "This is the text");
        newFragment.show(ft, "dialog");
    }

    /**
     * This is a fragment showing UI that will be updated from work done
     * in the retained fragment.
     */
    public static class UiFragment extends Fragment {
        RetainedFragment mWorkFragment; // A reference to our example retained Fragment
        View mView; // View containing our layout file

        /**
         * Called to have the fragment instantiate its user interface view. First we use the
         * LayoutInflater passed us to inflate our layout file R.layout.fragment_retain_instance
         * saving a reference to it in our field View v. We locate the Button R.id.restart ("RESTART")
         * and set its OnClickListener to an anonymous class which will instruct RetainedFragment
         * mWorkFragment to restart itself. Then we locate the Button R.id.toastme ("TOAST VALUE")
         * and set its OnClickListener to an anonymous class which creates and show a Toast showing
         * the value of RetainedFragment's static field int mPosition. Next it locates the view
         * R.id.main_view (which has a visibility of GONE until mWorkFragment flips the visibility
         * of the view containing the ProgressBar to GONE and it to  VISIBLE btw) and sets its
         * OnLongClickListener to an anonymous class which displays an informative Toast and instructs
         * our Activity FragmentVersionSkeleton to show its dialog. Finally we return our View mView
         * to our caller.
         *
         * @param inflater The LayoutInflater object that can be used to inflate
         *        any views in the fragment,
         * @param container If non-null, this is the parent view that the fragment's UI
         *        should be attached to.  The fragment should not add the view itself,
         *        but this can be used to generate the LayoutParams of the view.
         * @param savedInstanceState If non-null, this fragment is being re-constructed
         *        from a previous saved state as given here.
         *
         * @return Return the View for the fragment's UI, or null.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.fragment_retain_instance, container, false);

            // Watch for button clicks.
            Button button = (Button) mView.findViewById(R.id.restart);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the R.id.restart Button is clicked. We simply instruct our
                 * RetainedFragment mWorkFragment to restart the count at zero by calling
                 * the method RetainedFragment.restart().
                 *
                 * @param v View of Button that was clicked
                 */
                @Override
                public void onClick(View v) {
                    mWorkFragment.restart();
                }
            });

            button = (Button) mView.findViewById(R.id.toastme);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the R.id.toastme Button is clicked. We simply show a Toast displaying
                 * the current value of the public static int mPosition field of RetainedFragment.
                 *
                 * @param v View of Button that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "The value is:" + RetainedFragment.mPosition, Toast.LENGTH_LONG).show();
                }
            });

            TextView textView = (TextView) mView.findViewById(R.id.main_view);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Called when the R.id.main_view View has been long clicked. We make a Toast
                 * reporting: "I have been long clicked" and call showDialog of our Activity to
                 * create and show a MyDialogFragment DialogFragment. Finally we return true to
                 * indicate to our caller that we have consumed the long click.
                 *
                 * @param view View that was long clicked
                 * @return true if the callback consumed the long click, false otherwise.
                 */
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getActivity(), "I have been long clicked", Toast.LENGTH_LONG).show();
                    ((FragmentVersionSkeleton)getActivity()).showDialog();
                    return true;
                }
            });
            return mView;
        }

        /**
         * Called when the fragment's activity has been created and this
         * fragment's view hierarchy instantiated.  It can be used to do final
         * initialization once these pieces are in place, such as retrieving
         * views or restoring state.  It is also useful for fragments that use
         * {@link #setRetainInstance(boolean)} to retain their instance,
         * as this callback tells the fragment when it is fully associated with
         * the new activity instance.  This is called after {@link #onCreateView}
         * and before {@link #onViewStateRestored(Bundle)}.
         *
         * First we call through to our super's implementation of onActivityCreated. Next we set our
         * variable android.app.FragmentManager fm to the FragmentManager for interacting with
         * fragments associated with this fragment's activity. Then we use <code>fm</code> to look
         * for a fragment with the tag "work" and use it to set our field RetainedFragment
         * mWorkFragment, and if null was returned (the Fragment was not found) we create a new
         * instance of RetainedFragment for RetainedFragment mWorkFragment, set <code>this</code>
         * to be the target Fragment for mWorkFragment, and then use <code>fm</code> to start a
         * series of edit operations on the Fragments associated with this FragmentManager, use
         * the FragmentTransaction returned to add the Fragment <code>mWorkFragment</code> to the
         * Activity with the Tag "work", and the method chain terminates with a call to commit()
         * to schedule a commit of this transaction. Finally we call mWorkFragment.setDoneListener
         * to set the DoneListener of mWorkFragment to DoneListener mIamDone which swaps the
         * visibility of TextView R.id.main_view and the LinearLayout R.id.progress_view_linear_layout.
         * (The two share the same space in a FrameLayout, and R.id.progress_view_linear_layout starts
         * out with a visibility of VISIBLE which is changed to GONE, and R.id.main_view starts out
         * with a visibility of GONE which is changed to VISIBLE.)
         *
         * @param savedInstanceState we do not override onSaveInstanceState so ignore the value
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            android.app.FragmentManager fm = getFragmentManager();

            // Check to see if we have retained the worker fragment.
            mWorkFragment = (RetainedFragment) fm.findFragmentByTag("work");

            // If not retained (or first time running), we need to create it.
            if (mWorkFragment == null) {
                mWorkFragment = new RetainedFragment();
                // Tell it who it is working with.
                mWorkFragment.setTargetFragment(this, 0);
                fm.beginTransaction ().add(mWorkFragment, "work").commit();
            }
            mWorkFragment.setDoneListener(mIamDone, mView);
        }

        /**
         * DoneListener for mWorkFragment. When mWorkFragment is done it calls DoneListener.onDone
         * which in turn calls our override of onDoneDo which changes the visibility of LinearLayout
         * R.id.progress_view_linear_layout from VISIBLE to GONE, and the visibility of TextView
         * R.id.main_view from GONE to VISIBLE.
         */
        DoneListener mIamDone = new DoneListener() {
            /**
             * Called by DoneListener.onDone. First we locate the TextView R.id.main_view in our
             * layout file and save a reference in TextView mMainView, then we locate the
             * LinearLayout R.id.progress_view_linear_layout and save a reference to it in
             * LinearLayout mProgressViewLinearLayout. Finally we set the visibility of
             * mProgressViewLinearLayout to GONE, and the visibility of mMainView to visible.
             *
             * @param v View of our inflated layout file.
             */
            @Override
            public void onDoneDo(View v) {
                TextView mMainView = (TextView) v.findViewById(R.id.main_view);
                LinearLayout mProgressViewLinearLayout = (LinearLayout) v.findViewById(R.id.progress_view_linear_layout);
                mProgressViewLinearLayout.setVisibility(View.GONE);
                mMainView.setVisibility(View.VISIBLE);
            }
        };
    }

    /**
     * This is the Fragment implementation that will be retained across
     * activity instances.  It represents some ongoing work, here a thread
     * we have that sits around incrementing a progress indicator.
     */
    public static class RetainedFragment extends Fragment {

        private String TAG = "RetainedFragment";
        ProgressBar mProgressBar; // ProgressBar in our layout that we update to latest mPosition
        public static int mPosition; // Counter that we increment and use to set mProgressBar
        boolean mReady = false; // Flag set by our UIFragment to start (true) and stop (false) our work thread
        boolean mQuiting = false; // Flag set in onDestroy callback to stop our work thread
        LinearLayout mProgressViewLinearLayout; // LinearLayout in our layout that contains our ProgressBar
        TextView mMainView; // TextView in our layout that is used for results
        private DoneListener doneListener; // DoneListener instance used when our work thread finishes
        private View view; // View passed to setDoneListener, used in call to DoneListener.onDone
        /**
         * This is the thread that will do our work.  It sits in a loop setting the ProgressBar higher
         * until it has reached the top, then stops and waits until killed.
         */
        final Thread mThread = new Thread() {
            /**
             * We override Thread.run() to provide the code that runs when mThread.run() is called
             * by the background thread after we call mThread.start() in onCreate. First we initialize
             * our variable <code>int max</code> by retrieving the upper limit of the progress bar's
             * range which is set by the attribute android:max="500" in the layout file. Then we loop
             * forever, using Thread mThread (this for us) to synchronize with the UI Thread. In our
             * first <code>synchronized</code> block we check whether <code>mReady</code> is false
             * (the UI thread is not ready for us to continue) or our <code>mPosition</code> counter
             * is >= max (our work is done) and if either conditions hold we check whether the flag
             * <code>mQuiting</code> has been set (set in <code>onDestroy</code> and if so we return
             * to the caller ending this thread. If <code>mQuiting</code> has not been set we have
             * finished our work (mPosition >= max) so we call the onDone method of our field
             * DoneListener doneListener which swaps the visibility of views from the ProgressBar
             * we have been moving to the main View (results View if you would). If the UI is ready
             * for us to continue AND we are not finished with our "work", we increment our counter
             * mPosition and set the ProgressBar mProgressBar to this position. In the second
             * <code>synchronized</code> block we simply <code>wait</code> for 50 milliseconds before
             * continuing our infinite loop.
             */
            @Override
            public void run() {
                int max = mProgressBar.getMax(); // Read the ProgressBar upper limit android:max="500" from layout file

                // This thread runs almost forever.
                while (true) {

                    // Update our shared state with the UI.
                    synchronized (this) {
                        // Our thread is stopped if the UI is not ready
                        // or we have completed our work.
                        while (!mReady || mPosition >= max) {
                            if (mQuiting) {
                                return;
                            }
                            doneListener.onDone(view);

                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Now update the progress.  Note it is important that
                        // we touch the progress bar with the lock held, so it
                        // doesn't disappear on us.
                        mPosition++;
                        mProgressBar.setProgress(mPosition);
                    }

                    // Normally we would be doing some work, but put a kludge
                    // here to pretend like we are.
                    synchronized (this) {
                        try {
                            wait(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        /**
         * Fragment initialization. First we call through to our super's implementation of onCreate,
         * then call setRetainInstance(true) to indicate to the system that this fragment instance
         * should be retained across Activity re-creation (such as from a configuration change), and
         * finally we start our thread.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so are not interested
         *        in it, and even if we did override onSaveInstanceState onCreate would not be called
         *        again because we call setRetainInstance(true) and our fragment will not need to be
         *        recreated.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Tell the framework to try to keep this fragment around
            // during a configuration change.
            setRetainInstance(true);
            // Start up the worker thread.
            mThread.start();
        }

        /**
         * This is called when the Fragment's Activity is ready to go, after its content view has
         * been installed; it is called both after the initial fragment creation and after the
         * fragment is re-attached to a new activity. First we call through to our super's
         * implementation of onActivityCreated, then we fetch a reference to our UIFragment
         * (<code>Fragment targetFragment</code>) by calling getTargetFragment and use it to get
         * its root view (the one it returned from onCreateView) which we use to set our variable
         * <code>View gotView</code>. If we have successfully set gotView to our root view, we use
         * it to initialize our field <code>ProgressBar mProgressBar</code> to the ProgressBar
         * (R.id.progress_horizontal), our field <code>LinearLayout mProgressViewLinearLayout</code>
         * to the LinearLayout holding the ProgressBar (R.id.progress_view_linear_layout), and our
         * field <code>TextView mMainView</code> to the TextView that is to be made visible after our
         * "work" is done (R.id.main_view). Finally in a <code>synchronized mThread</code> statement
         * we set the boolean mReady flag to true and notify our worker Thread mThread(which is
         * waiting on "this") allowing it to run after the synchronized statement completes and
         * releases the lock on mThread.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.i(TAG, "onActivityCreated has been called");

            // Retrieve the progress bar from the target's view hierarchy.
            Fragment targetFragment = getTargetFragment();
            View gotView = null;
            if (targetFragment != null) {
                gotView = targetFragment.getView();
            }
            if (gotView != null) {
                mProgressBar = (ProgressBar) gotView.findViewById(R.id.progress_horizontal);
                mProgressViewLinearLayout = (LinearLayout) gotView.findViewById(R.id.progress_view_linear_layout);
                mMainView = (TextView) gotView.findViewById(R.id.main_view);
            }

            // We are ready for our thread to go.
            synchronized (mThread) {
                mReady = true;
                mThread.notify();
            }
        }

        /**
         * This is called when the fragment is going away.  It is NOT called when the fragment is
         * being propagated between activity instances (such as after an orientation change). In
         * a synchronized statement using <code>mThread mThread</code> as its intrinsic lock, we
         * set the <code>boolean mReady</code> flag to false (a signal to our worker thread that it
         * should check the flag <code>boolean mQuiting</code> when it is awoken by notify()), set
         * the <code>boolean mQuiting</code> flag to true (a signal to our worker thread that its
         * run() method should exit by returning to its caller), and then awaken our worker Thread
         * mThread (which is wait()'ing on this) by calling mThread.notify(). Finally we call
         * through to our super's implementation of onDestroy.
         */
        @Override
        public void onDestroy() {
            // Make the thread go away.
            synchronized (mThread) {
                mReady = false;
                mQuiting = true;
                mThread.notify();
            }

            super.onDestroy();
        }

        /**
         * This is called right before the fragment is detached from its current activity instance.
         * In a synchronized statement using <code>mThread mThread</code> as its intrinsic lock, we
         * set the field <code>ProgressBar mProgressBar</code> to null because the new Activity will
         * need to find its version of mProgressBar in onActivityCreated before we can use it. We
         * then set mReady to false to signal to our "work" Thread that it should wait until the
         * new Activity is created before it continues, then awaken our worker Thread mThread (which
         * is currently wait()'ing on this) by calling mThread.notify(). Finally we call through to
         * our super's implementation of onDetach().
         */
        @Override
        public void onDetach() {
            // This fragment is being detached from its activity.  We need
            // to make sure its thread is not going to touch any activity
            // state after returning from this function.
            synchronized (mThread) {
                mProgressBar = null;
                mReady = false;
                mThread.notify();
            }

            super.onDetach();
        }

        /**
         * Sets our fields DoneListener doneListener, and View view which are used by our work thread
         * when it finishes its task.
         *
         * @param doneListener DoneListener to use when work thread is done
         * @param view View to pass to doneListener.onDone(View) when work thread is done.
         */
        public void setDoneListener(DoneListener doneListener, View view) {
            this.view = view;
            this.doneListener = doneListener;
        }

        /**
         * API for our UI to restart the progress thread when the Button R.id.restart ("RESTART") is
         * clicked. In a synchronized statement using <code>Thread mThread</code> as the intrinsic
         * lock, we set the field <code>int mPosition</code> to 0, then awaken the worker Thread
         * mThread (which is currently waiting on "this") by calling mThread,notify()
         */
        public void restart() {
            synchronized (mThread) {
                mPosition = 0;
                mThread.notify();
            }
        }
    }

    /**
     * This is a skeletal DialogFragment which was stuck into this Activity in order to experiment
     * with using DialogFragment's. When show() is called it just displays the label and text that
     * were passed as parameters to newInstance.
     */
    public static class MyDialogFragment extends DialogFragment {

        String mLabel; // "label" to display
        String mText;  // "text" to display

        /**
         * Create a new instance of MyDialogFragment providing "label" and "text" as arguments.
         * First we create an instance of <code>MyDialogFragment f</code>, then we create a
         * <code>Bundle args</code> and add our parameter label under the key "label" and our
         * parameter text under the key "text". We use this Bundle to set the construction arguments
         * for <code>MyDialogFragment f</code>, and finally we return <code>f</code> to the caller.
         *
         * @param label Label to use for dialog
         * @param text  Text body for dialog
         * @return A MyDialogFragment instance with the arguments set
         */
        static MyDialogFragment newInstance(String label, String text) {
            MyDialogFragment f = new MyDialogFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString("label", label);
            args.putString("text", text);

            f.setArguments(args);

            return f;
        }

        /**
         * Called to do initial creation of a fragment. First we call through to our super's
         * implementation of onCreate, then we set our field mLabel to the argument stored by
         * newInstance under the key "label", and our field mText to the argument it stored under
         * the key "text". Finally we set the style of our DialogFragment to STYLE_NORMAL.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mLabel = getArguments().getString("label");
            mText = getArguments().getString("text");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NORMAL, theme = 0;
            setStyle(style, theme);
        }

        /**
         * Called to have the fragment instantiate its user interface view. First we inflate our
         * layout R.layout.frag_vers_skel_dialog into <code>View v</code>, then we locate the
         * <code>TextView</code> for our label (R.id.label) and set its text to the contents of our
         * field <code>String mLabel</code>, locate the <code>TextView</code> for our text (R.id.text)
         * and set its text to the contents of our field <code>String mText</code>, and then we
         * locate the <code>Button</code> R.id.dismiss and set its OnClickListener to an anonymous
         * class which simply dismisses this <code>DialogFragment MyDialogFragment</code>. Finally
         * we return <code>View v</code> to the caller.
         *
         * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
         * @param container If non-null, this is the parent view that the fragment's UI should be
         *        attached to. The fragment should not add the view itself, but this can be used to
         *        generate the LayoutParams of the view.
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         *
         * @return Return the View for the fragment's UI.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.frag_vers_skel_dialog, container, false);

            View tv = v.findViewById(R.id.label);
            ((TextView)tv).setText(mLabel);

            tv = v.findViewById(R.id.text);
            ((TextView)tv).setText(mText);

            // Watch for button clicks.
            Button button = (Button)v.findViewById(R.id.dismiss);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return v;
        }
    }


}
