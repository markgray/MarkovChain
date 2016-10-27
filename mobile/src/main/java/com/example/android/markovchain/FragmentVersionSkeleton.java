package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
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
public class FragmentVersionSkeleton extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First time init, create the UI.
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content,
                    new UiFragment()).commit();
        }
    }

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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class UiFragment extends Fragment {
        RetainedFragment mWorkFragment;
        View v;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.fragment_retain_instance, container, false);

            // Watch for button clicks.
            Button button = (Button) v.findViewById(R.id.restart);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mWorkFragment.restart();
                }
            });

            button = (Button) v.findViewById(R.id.toastme);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "The value is:" + RetainedFragment.mPosition, Toast.LENGTH_LONG).show();
                }
            });

            TextView textView = (TextView) v.findViewById(R.id.main_view);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getActivity(), "I have been long clicked", Toast.LENGTH_LONG).show();
                    ((FragmentVersionSkeleton)getActivity()).showDialog();
                    return true;
                }
            });
            return v;
        }

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
            mWorkFragment.setDoneListener(mIamDone, v);
        }

        DoneListener mIamDone = new DoneListener() {
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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class RetainedFragment extends Fragment {

        ProgressBar mProgressBar;
        public static int mPosition;
        boolean mReady = false;
        boolean mQuiting = false;
        LinearLayout mProgressViewLinearLayout;
        TextView mMainView;
//        Handler mHandler = new Handler();
        private DoneListener doneListener;
        private View view;

        public void setDoneListener(DoneListener doneListener, View view) {
            this.view = view;
            this.doneListener = doneListener;
        }

        /**
         * This is the thread that will do our work.  It sits in a loop running
         * the progress up until it has reached the top, then stops and waits.
         */
        final Thread mThread = new Thread() {
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
//                            mHandler.post(new Runnable() {
//                                public void run() {
//                                    mProgressViewLinearLayout.setVisibility(View.GONE);
//                                    mMainView.setVisibility(View.VISIBLE);
//                                }
//                            });
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
            Button button = (Button)v.findViewById(R.id.show);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // When button is clicked, call up to owning activity.
                    ((FragmentVersionSkeleton)getActivity()).showDialog();
                }
            });

            return v;
        }
    }


}
