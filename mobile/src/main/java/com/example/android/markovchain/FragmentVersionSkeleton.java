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

        ProgressBar mProgressBar; // ProgressBar in our layout that we update to latest mPosition
        public static int mPosition; // Counter that we increment and use to set mProgressBar
        boolean mReady = false; // Flag set by our UIFragment to stop our work thread
        boolean mQuiting = false; // Flag set in onDestroy callback to stop our work thread
        LinearLayout mProgressViewLinearLayout; // LinearLayout in our layout that contains our ProgressBar
        TextView mMainView; // TextView in our layout that is used for results
        private DoneListener doneListener; // DoneListener instance used when our work thread finishes
        private View view; // View passed to setDoneListener, used in call to DoneListener.onDone

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
         * This is the thread that will do our work.  It sits in a loop running
         * the progress up until it has reached the top, then stops and waits.
         */
        final Thread mThread = new Thread() {
            /**
             * We override Thread.run() to provide the code that runs when mThread.run() is called.
             *
             */
            @Override
            public void run() {
                // We'll figure the real value out later.
                int max = 10000;

                // This thread runs almost forever.
                while (true) {

                    // Update our shared state with the UI.
                    synchronized (this) {
                        // Our thread is stopped if the UI is not ready
                        // or it has completed its work.
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
                        max = mProgressBar.getMax();
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
         * Fragment initialization.  We way we want to be retained and
         * start our thread.
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
         * This is called when the Fragment's Activity is ready to go, after
         * its content view has been installed; it is called both after
         * the initial fragment creation and after the fragment is re-attached
         * to a new activity.
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Retrieve the progress bar from the target's view hierarchy.
            Fragment tarGetFragment = getTargetFragment();
            View gotView = null;
            if (tarGetFragment != null) {
                gotView = tarGetFragment.getView();
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
         * This is called when the fragment is going away.  It is NOT called
         * when the fragment is being propagated between activity instances.
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
         * This is called right before the fragment is detached from its
         * current activity instance.
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
         * API for our UI to restart the progress thread.
         */
        public void restart() {
            synchronized (mThread) {
                mPosition = 0;
                mThread.notify();
            }
        }
    }

    public static class MyDialogFragment extends DialogFragment {

        String mLabel;
        String mText;

        /**
         * Create a new instance of MyDialogFragment providing
         * "label" and "text" as arguments.
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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mLabel = getArguments().getString("label");
            mText = getArguments().getString("text");

            // Pick a style based on the num.
            int style = DialogFragment.STYLE_NORMAL, theme = 0;
            setStyle(style, theme);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.bible_dialog, container, false);
            View tv = v.findViewById(R.id.label);
            String dialogLabel = mLabel;
            ((TextView)tv).setText(dialogLabel);

            tv = v.findViewById(R.id.text);
            ((TextView)tv).setText(mText);

            // Watch for button clicks.
            Button button = (Button)v.findViewById(R.id.dismiss);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // When button is clicked, call up to owning activity.
                    ((FragmentVersionSkeleton)getActivity()).showDialog();
                }
            });

            return v;
        }
    }


}
