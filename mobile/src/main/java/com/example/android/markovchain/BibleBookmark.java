package com.example.android.markovchain;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * This {@code DialogFragment} will (eventually) save a bookmark to a particular verse with the date
 * it was bookmarked and an optional comment to an SQL database.
 */
@SuppressWarnings("WeakerAccess")
public class BibleBookmark extends DialogFragment {
    /**
     * TAG for logging
     */
    public final static String TAG = "BibleBookmark";
    /**
     * Canonical Bible citation for current verse
     */
    public String mLabel;
    /**
     * Text of the current verse
     */
    public String mText;

    /**
     * Create a new {@code BibleBookmark DialogFragment}. We initialize {@code BibleBookmark f}, with
     * a new instance, then initialize {@code Bundle args} with a new instance, add our parameter
     * {@code String label} to {@code args} using the index "label", add our parameter {@code String text}
     * to {@code args} using the index "text", and then set the arguments for {@code f} to {@code args}.
     * Finally we return {@code f} to our caller.
     *
     * @param label Canonical Bible citation for the verse being bookmarked
     * @param text  Text of the verse being bookmarked
     * @return A {@code BibleBookmark} instance with the arguments bundle containing the parameters
     * {@code label} and {@code text} passed to us.
     */
    public static BibleBookmark newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleBookmark f = new BibleBookmark();
        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    /**
     * Called to do initial creation of a {@code DialogFragment}. This is called after {@code onAttach(Activity)}
     * and before {@code onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * Note that this can be called while the fragment's activity is still in the process of being
     * created. As such, you can not rely on things like the activity's content view hierarchy being
     * initialized at this point.  If you want to do work once the activity itself is created, see
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * First we call through to our super's implementation of {@code onCreate}, then we initialize our
     * field {@code String mLabel} set to the {@code String} stored under the key "label" in our argument
     * {@code Bundle}, and {@code String mText} set to the {@code String} stored under the key "text"
     * in that {@code Bundle}. Finally we set the style of our {@code DialogFragment} to be STYLE_NORMAL
     * (for a reason which escapes me right now).
     *
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * If you return a View from here, you will later be called in {@link #onDestroyView} when the
     * view is being released.
     * <p>
     * First we initialize {@code View v} by using our parameter {@code LayoutInflater inflater} to
     * inflate our layout file R.layout.bible_bookmark using our parameter {@code ViewGroup container}
     * for layout params without attaching to is, then we initialize {@code View tv} by finding the
     * view in {@code v} with id R.id.label and set its text to our field {@code String mLabel}, and
     * then we set {@code tv} to the view with id R.id.text and set its text to our field {@code String mText}.
     * Finally we locate the "DISMISS" Button (view with id R.id.dismiss) and set its {@code OnClickListener}
     * to an anonymous class which will dismiss this {@code BibleBookmark DialogFragment} when clicked,
     * while updating the label and text used by the {@code BibleDialog} which launched us and to which
     * we will be returning to.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate
     *                  the {@code LayoutParams} of the view.
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_bookmark, container, false);
        TextView tv = v.findViewById(R.id.label);
        tv.setText(mLabel);
        tv = v.findViewById(R.id.text);
        tv.setText(mText);

        v.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            /**
             * Dismiss this {@code DialogFragment}, and make sure that the {@code BibleDialog bibleDialog}
             * which we are returning to uses the correct {@code dialogTitle} for its {@code mLabel}
             * field and correct {@code dialogText} for its {@code mText} field (in case we changed the
             * verse being shown).
             *
             * @param v Button which was clicked
             */
            @Override
            public void onClick(View v) {
                BibleBookmark.this.dismiss();
                //noinspection ConstantConditions
                BibleMain.Companion.getBibleDialog().mLabel = BibleMain.Companion.getDialogTitle();
                BibleMain.Companion.getBibleDialog().mText = BibleMain.Companion.getDialogText();
            }
        });

        return v;
    }
}
