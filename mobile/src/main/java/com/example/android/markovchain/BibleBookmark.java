package com.example.android.markovchain;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
` * This DialogFragment will (eventually) save a bookmark with the date it was bookmarked
 * and an optional comment to an SQL database.
 */
public class BibleBookmark extends DialogFragment {
    public final static String TAG = "BibleBookmark"; // TAG for logging
    public String mLabel; // Canonical Bible citation for current verse
    public String mText;  // Current verse

    /**
     * Create a new BibleBookmark DialogFragment. We create a new instance of BibleBookmark f,
     * create a Bundle args, add our parameter String label to args using the index "label", add
     * our parameter String text to args using the index "text", and then set the arguments for
     * f to our Bundle args. Finally we return BibleBook f to our caller.
     *
     * @param label Canonical Bible citation for the verse being bookmarked
     * @param text  Text of the verse being bookmarked
     *
     * @return A BibleBookmark instance with the arguments bundle set with the
     *         label and text passed to the method
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
     * Called to do initial creation of a DialogFragment.  This is called after
     * onAttach(Activity) and before
     * onCreateView(LayoutInflater, ViewGroup, Bundle).
     *
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * First we call through to our super's implementation of onCreate, then we read the construction
     * arguments used when creating this DialogFragment with our field mLabel set to the String
     * stored under the key "label", and mText set to the String stored under the key "text". Finally
     * we set the style of the DialogFragment to be STYLE_NORMAL (for a reason which escapes me right
     * now. TODO: figure out better style usage here.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state. (Always null since onSaveInstanceState
     * is not overridden.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * First we create View v by inflating our layout file R.layout.bible_bookmark, then we locate
     * the TextView that we will use to display our field mLabel (R.id.label) and set its text to
     * that String, and we the locate the TextView that we will use to display our field mText
     * (R.id.text) and set its text to that String. Finally we locate the "DISMISS" Button
     * (R.id.dismiss) and set its OnClickListener to an anonymous class which will dismiss this
     * BibleBookmark DialogFragment when clicked, while updating the label and text used by the
     * BibleDialog which launched us and to which we will be returning to.
     * TODO: is this really necessary?
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here. (Always null since onSaveInstanceState
     * is not overridden.)
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_bookmark, container, false);
        View tv = v.findViewById(R.id.label);
        String dialogLabel = mLabel;
        ((TextView) tv).setText(dialogLabel);

        tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(mText);

        Button dismiss = (Button) v.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            /**
             * Dismiss this DialogFragment, and make sure that the BibleDialog which we are
             * returning to uses the correct dialogTitle and dialogText
             *
             * @param v Button which was clicked
             */
            @Override
            public void onClick(View v) {
                BibleBookmark.this.dismiss();
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
            }
        });

        return v;
    }
}
