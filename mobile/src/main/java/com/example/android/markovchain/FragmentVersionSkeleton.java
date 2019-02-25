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
    /**
     * TAG used for logging
     */
    final static String TAG = "FragmentVersionSkeleton";

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate. If this is the first time we are called (Bundle savedInstanceState == null) we need
     * to:
     * <ul>
     *     <li>
     *         1. Get the FragmentManager for interacting with fragments associated with this activity.
     *     </li>
     *     <li>
     *         2. Start a series of edit operations on the Fragments associated with this FragmentManager.
     *     </li>
     *     <li>
     *         3. Add a new instance of our UiFragment fragment to the activity state
     *     </li>
     *     <ul>
     *         4. Schedule a commit of this transaction. The commit does not happen immediately; it will
     *         be scheduled as work on the main thread to be done the next time that thread is ready.
     *     </ul>
     * </ul>
     * If we are being recreated after an orientation change or other event, then the fragment api
     * will have saved its state in {@code Bundle savedInstanceState} so it will not be null. (We however do
     * not override {@code onSaveInstanceState}, so there is nothing in the Bundle we need bother with.)
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
     * Create and show a {@code MyDialogFragment DialogFragment}. First we get the {@code FragmentManager}
     * for interacting with fragments associated with this activity and instruct it to create a
     * {@code FragmentTransaction ft} and by beginning a series of fragment transactions on the Fragment's
     * associated with this {@code FragmentManager}. Next we initialize our variable {@code Fragment prev}
     * by having the {@code FragmentManager} for interacting with fragments associated with this activity
     * search for a fragment with the tag "dialog". If {@code prev} is not null we use {@code ft} to remove
     * {@code prev}. We next tell {@code ft} to add the transaction to the back stack. Next we initialize
     * {@code DialogFragment newFragment} with a new instance of {@code MyDialogFragment} using some dummy
     * data for its label and text. Finally we instruct {@code newFragment} to show itself using the tag
     * "dialog".
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
        /**
         * A reference to our example retained Fragment
         */
        RetainedFragment mWorkFragment;
        /**
         * View containing our layout file
         */
        View mView;

        /**
         * Called to have the fragment instantiate its user interface view. First we use the
         * {@code LayoutInflater inflater} passed us to inflate our layout file R.layout.fragment_retain_instance
         * saving a reference to it in our field {@code View mView}. We locate the {@code Button} with
         * id R.id.restart ("RESTART") and set its {@code OnClickListener} to an anonymous class which
         * will instruct our field {@code RetainedFragment mWorkFragment} to restart itself. Then we
         * locate the {@code Button} with id R.id.toast_me ("TOAST VALUE") and set its {@code OnClickListener}
         * to an anonymous class which creates and shows a Toast displaying the value of the static field
         * {@code int mPosition} of {@code RetainedFragment}. Next it locates the view with id
         * R.id.main_view (which has a visibility of GONE until {@code mWorkFragment} flips the visibility
         * of the view containing the {@code ProgressBar} to GONE and it to  VISIBLE btw) and set its
         * {@code OnLongClickListener} to an anonymous class which displays an informative Toast and instructs
         * our {@code FragmentVersionSkeleton} {@code Activity}  to show its dialog. Finally we return
         * our field {@code mView} to our caller.
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
            Button button = mView.findViewById(R.id.restart);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the R.id.restart ("RESTART") {@code Button} is clicked. We simply instruct
                 * our field {@code RetainedFragment mWorkFragment} to restart the count at zero by calling
                 * its {@code restart()} method.
                 *
                 * @param v View of Button that was clicked
                 */
                @Override
                public void onClick(View v) {
                    mWorkFragment.restart();
                }
            });

            button = mView.findViewById(R.id.toast_me);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the R.id.toast_me ("TOAST VALUE") {@code Button} is clicked. We simply
                 * show a Toast displaying the current value of the static {@code int mPosition} field of
                 * {@code RetainedFragment}.
                 *
                 * @param v View of Button that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "The value is:" + RetainedFragment.mPosition, Toast.LENGTH_LONG).show();
                }
            });

            TextView textView = mView.findViewById(R.id.main_view);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Called when the R.id.main_view {@code TextView} has been long clicked. We make a
                 * Toast reporting: "I have been long clicked" and call the {@code showDialog} method
                 * of our Activity to create and show a {@code MyDialogFragment DialogFragment}. Finally
                 * we return true to indicate to our caller that we have consumed the long click.
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
         * <p>
         * First we call through to our super's implementation of {@code onActivityCreated}. Next we
         * set our variable {@code android.app.FragmentManager fm} to the FragmentManager for interacting
         * with fragments associated with this fragment's activity. Then we use {@code fm} to look
         * for a fragment with the tag "work" and use this value to set our field
         * {@code RetainedFragment mWorkFragment}. If null was returned (the {@code Fragment} was not
         * found) we create a new instance of {@code RetainedFragment} for {@code mWorkFragment}, set
         * 'this' to be the target Fragment for {@code mWorkFragment}, and then use {@code fm} to start
         * a series of edit operations on the Fragments associated with this {@code FragmentManager},
         * use the {@code FragmentTransaction} returned to add the Fragment {@code mWorkFragment} to
         * the Activity with the Tag "work", with the method chain terminating with a call to {@code commit()}
         * to schedule a commit of this transaction. Finally we call the {@code setDoneListener} method
         * of {@code mWorkFragment} to set the {@code DoneListener} of {@code mWorkFragment} to the
         * to field {@code DoneListener mIamDone} which swaps the visibility of the {@code TextView}
         * with id R.id.main_view and the {@code LinearLayout} with id R.id.progress_view_linear_layout.
         * (The two share the same space in a {@code FrameLayout}, and R.id.progress_view_linear_layout
         * starts out with a visibility of VISIBLE which is changed to GONE, and R.id.main_view starts
         * out with a visibility of GONE which is changed to VISIBLE.)
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
         * DoneListener for {@code RetainedFragment mWorkFragment}. When {@code mWorkFragment} is done
         * it calls the {@code onDone} method of {@code DoneListener} which in turn calls our override
         * of {@code onDoneDo} which changes the visibility of the {@code LinearLayout} with id
         * R.id.progress_view_linear_layout from VISIBLE to GONE, and the visibility of the {@code TextView}
         * with id R.id.main_view from GONE to VISIBLE.
         */
        DoneListener mIamDone = new DoneListener() {
            /**
             * Called by {@code DoneListener.onDone()}. First we locate the {@code TextView} with id
             * R.id.main_view in our parameter {@code View v} and save a reference in our variable
             * {@code TextView mMainView}, then we locate the {@code LinearLayout} with id
             * R.id.progress_view_linear_layout and save a reference to it in our variable
             * {@code LinearLayout mProgressViewLinearLayout}. Finally we set the visibility of
             * {@code mProgressViewLinearLayout} to GONE, and the visibility of {@code mMainView}
             * to visible.
             *
             * @param v View of our inflated layout file.
             */
            @Override
            public void onDoneDo(View v) {
                TextView mMainView = v.findViewById(R.id.main_view);
                LinearLayout mProgressViewLinearLayout = v.findViewById(R.id.progress_view_linear_layout);
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
        /**
         * TAG used for logging.
         */
        private String TAG = "RetainedFragment";
        /**
         * {@code ProgressBar} in our layout that we update to the latest value of {@code mPosition}
         */
        ProgressBar mProgressBar;
        /**
         * Counter that we increment and use to set the progress of our {@code ProgressBar mProgressBar}
         */
        public static int mPosition;
        /**
         * Flag set by our {@code UIFragment} to start (true) and stop (false) our work thread
         */
        boolean mReady = false;
        /**
         * Flag set in {@code onDestroy} callback to stop our work thread
         */
        boolean mQuiting = false;
        /**
         * {@code LinearLayout} in our layout that contains our {@code ProgressBar}
         */
        LinearLayout mProgressViewLinearLayout;
        /**
         * {@code TextView} in our layout that is used for results
         */
        TextView mMainView;
        /**
         * {@code DoneListener} instance used when our work thread finishes
         */
        private DoneListener doneListener;
        /**
         * {@code View} passed to {@code setDoneListener}, used as the parameter when calling the
         * {@code onDone} method of {@code DoneListener}.
         */
        private View view;
        /**
         * This is the {@code Thread} that will do our work. It sits in a loop incrementing the
         * {@code ProgressBar} until it has reached the top, then stops and waits until killed.
         */
        final Thread mThread = new Thread() {
            /**
             * We override the {@code run()} method of {@code Thread} to provide the code that runs
             * when the background thread is started after we call {@code mThread.start()} in our
             * {@code onCreate} override. First we initialize our variable {@code int max} by retrieving
             * the upper limit of the range of {@code ProgressBar mProgressBar}, (which is set by the
             * attribute android:max="500" in the layout file). Then we loop forever using 'this' to
             * synchronize with the UI Thread. In our first synchronized block we check whether our
             * field {@code mReady} is false (the UI thread is not ready for us to continue) or our
             * counter {@code mPosition} is >= {@code max} (our work is done) and if either condition
             * holds we check whether the flag {@code mQuiting} is true, and if it is we return ending
             * this thread ({@code mQuiting} is set in {@code onDestroy}). If {@code mQuiting} has not
             * been set we have finished our work ({@code mPosition} >= {@code max}) so we call the
             * {@code onDone} method of our field {@code DoneListener doneListener} which swaps the
             * visibility of views from the {@code ProgressBar} we have been moving to the main View
             * (results View if you would). If the UI is ready for us to continue AND we are not yet
             * finished with our "work", we increment our counter {@code mPosition} and set {@code mProgressBar}
             * to this position. In the second synchronized block we simply {@code wait} for 50 milliseconds
             * before continuing our infinite loop.
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
         * Fragment initialization. First we call through to our super's implementation of {@code onCreate},
         * then we call {@code setRetainInstance(true)} to indicate to the system that this fragment instance
         * should be retained across Activity re-creation (such as from a configuration change), and
         * finally we start our thread {@code Thread mThread} running.
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
         * been installed. It is called both after the initial fragment creation and after the
         * fragment is re-attached to a new activity. First we call through to our super's
         * implementation of {@code onActivityCreated}, then we fetch a reference to our UIFragment
         * to initialize our variable {@code Fragment targetFragment} by calling {@code getTargetFragment}.
         * We initialize our variable {@code View gotView} to null, and if {@code targetFragment} is
         * not null we set {@code gotView} to the root view for the layout of {@code targetFragment}
         * (the one returned by {@link #onCreateView}). If we have successfully set {@code gotView}
         * is not null we set our field {@code ProgressBar mProgressBar} to the view in {@code gotView}
         * with id R.id.progress_horizontal, our field {@code LinearLayout mProgressViewLinearLayout}
         * to the view in {@code gotView} with id R.id.progress_view_linear_layout, and our field
         * {@code TextView mMainView} to the view in {@code gotView} with id R.id.main_view. Finally
         * synchronized on {@code Thread mThread} we set our field {@code mReady} to true, and call
         * the {@code notify()} method of {@code mThread} to wake that thread up (it is currently
         * waiting on its 'this') allowing it to run after our synchronized statement completes and
         * releases the lock on mThread.
         *
         * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use
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
                mProgressBar = gotView.findViewById(R.id.progress_horizontal);
                mProgressViewLinearLayout = gotView.findViewById(R.id.progress_view_linear_layout);
                mMainView = gotView.findViewById(R.id.main_view);
            }

            // We are ready for our thread to go.
            synchronized (mThread) {
                mReady = true;
                mThread.notify();
            }
        }

        /**
         * This is called when the fragment is going away. It is NOT called when the fragment is
         * being propagated between activity instances (such as after an orientation change). In
         * a synchronized statement using {@code Thread mThread} as its intrinsic lock, we
         * set the {@code boolean mReady} flag to false (a signal to our worker thread that it
         * should check the flag {@code boolean mQuiting} when it is awoken by {@code notify()}), set
         * the {@code boolean mQuiting} flag to true (a signal to our worker thread that its
         * {@code run()} method should exit by returning to its caller), and then awaken our worker
         * {@code Thread mThread} (which is {@code wait()}'ing on its 'this' which is locked while
         * we are in our synchronized block) by calling the {@code notify()} method of {@code mThread}.
         * Finally we call through to our super's implementation of {@code onDestroy()}.
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
         * In a synchronized block using {@code Thread mThread} as its intrinsic lock, we set the field
         * {@code ProgressBar mProgressBar} to null because the new Activity will need to find its own
         * version of {@code mProgressBar} in onActivityCreated before it can use it. We then set
         * {@code mReady} to false to signal to our "worker" {@code Thread} that it should wait until
         * the new Activity is created before it continues, then awaken our worker {@code Thread mThread}
         * is currently {@code wait()}'ing for its 'this' to be unlocked by our exiting the synchronized
         * block) by calling the {@code notify()} method of {@code mThread}. Finally we call through
         * to our super's implementation of {@code onDetach()}.
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
         * Sets our fields {@code DoneListener doneListener}, and {@code View view} which are used by
         * our work thread when it finishes its task.
         *
         * @param doneListener DoneListener to use when work thread is done
         * @param view View to pass to doneListener.onDone(View) when work thread is done.
         */
        public void setDoneListener(DoneListener doneListener, View view) {
            this.view = view;
            this.doneListener = doneListener;
        }

        /**
         * API for our UI to restart the progress thread when the {@code Button} R.id.restart ("RESTART")
         * is clicked. In a synchronized block using {@code Thread mThread} as the intrinsic lock, we
         * set the field {@code int mPosition} to 0, then awaken the worker {@code Thread mThread}
         * (which is currently waiting for us to release its 'this') by calling the {@code notify()}
         * method of {@code mThread}.
         */
        public void restart() {
            synchronized (mThread) {
                mPosition = 0;
                mThread.notify();
            }
        }
    }

    /**
     * This is a skeletal {@code DialogFragment} which was just stuck into this Activity in order to
     * experiment with using {@code DialogFragment}'s. When {@code show()} is called it just displays
     * the label and text that were passed as parameters to its {@code newInstance} method.
     */
    public static class MyDialogFragment extends DialogFragment {
        /**
         * "label" to display
         */
        String mLabel;
        /**
         * "text" to display
         */
        String mText;

        /**
         * Create a new instance of {@code MyDialogFragment} providing values for "label" and "text"
         * in its argument {@code Bundle}. First we initialize {@code MyDialogFragment f} with a new
         * instance, then we initialize {@code Bundle args} with a new instance. We store our parameter
         * {@code String label} in {@code args} under the key "label" and our parameter {@code String text}
         * under the key "text". We then set the construction arguments for {@code f} to {@code args}
         * and return {@code f} to the caller.
         *
         * @param label Label to use for dialog
         * @param text  Text body for dialog
         * @return A MyDialogFragment instance with the arguments set
         */
        @SuppressWarnings("SameParameterValue")
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
         * implementation of {@code onCreate}, then we set our field {@code mLabel} to the argument
         * stored in our argument {@code Bundle} by {@code newInstance} under the key "label", and
         * our field {@code mText} to the argument it stored under the key "text". Finally we set
         * the style of our {@code DialogFragment} to STYLE_NORMAL.
         *
         * @param savedInstanceState we do not override onSaveInstanceState so do not use this
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mLabel = getArguments().getString("label");
            mText = getArguments().getString("text");

            // Pick a style based on the num.
            int theme = 0;
            setStyle(DialogFragment.STYLE_NORMAL, theme);
        }

        /**
         * Called to have the fragment instantiate its user interface view. First we initialize our
         * variable {@code View v} by using our parameter {@code LayoutInflater inflater} to inflate
         * our layout file R.layout.frag_vers_skel_dialog using our parameter {@code ViewGroup container}
         * for the layout params without attaching to it, then we initialize our variable {@code TextView tv}
         * by finding the view in {@code v} with the id R.id.label and set its text to the contents
         * of our field {@code String mLabel}, then we set {@code tv} to the view in {@code v} we find
         * with the R.id.text and set its text to the contents of our field {@code String mText}. We
         * next initialize our variable {@code Button button} by finding the view in {@code v} with
         * id R.id.dismiss ("DISMISS") and set its {@code OnClickListener} to an anonymous class which
         * simply dismisses this dialog by calling the {@code dismiss()} method. Finally we return
         * {@code v} to the caller.
         *
         * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
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

            TextView tv = v.findViewById(R.id.label);
            tv.setText(mLabel);

            tv = v.findViewById(R.id.text);
            tv.setText(mText);

            // Watch for button clicks.
            Button button = v.findViewById(R.id.dismiss);
            button.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the "DISMISS" {@code Button} is clicked, we just call the {@code dismiss()}
                 * method to dismiss our dialog.
                 *
                 * @param v {@code View} that was clicked
                 */
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return v;
        }
    }


}
