package com.example.android.markovchain;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class BibleDialog extends DialogFragment {
    private static final String TAG = "BibleDialog";
    public String mLabel;
    public String mText;
    public Button repeatButton;

    /**
     * Create and initialize a BibleDialog DialogFragment. It does this by first creating a new
     * instance of BibleDialog f, then creates an instance of Bundle args, putString's the label
     * parameter under the index "label", and putStrings's the text parameter under the index
     * "text' in this Bundle args, and finally using this Bundle to set the arguments of the
     * BibleDialog using the method Fragment.setArguments.
     *
     * @param label Label to use
     * @param text  Text to use
     * @return Initialized BibleDialog
     */
    static BibleDialog newInstance(String label, String text) {
        BibleDialog f = new BibleDialog();

        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text", text);

        f.setArguments(args);

        return f;
    }

    TextView lastLabelView;
    TextView lastTextView;
    /**
     * Update the text displayed in the BibleDialog for a new verse. This is necessary only after
     * moving to a random verse at the moment because the same BibleDialog DialogFragment is
     * used. Other actions create DialogFragments which when dismissed need to have the BibleDialog
     * recreated so onCreateView is called, therefore setting the fields BibleMain.bibleDialog.mLabel
     * and BibleMain.bibleDialog.mText is done instead.
     *
     * @param label Canonical Bible citation for the verse
     * @param text Text of the verse
     */
    public void refresh(String label, String text) {
        lastLabelView.setText(label);
        lastTextView.setText(text);
    }

    /**
     * Called to do initial creation of a DialogFragment.  This is called after
     * onAttach(Activity) and before onCreateView(LayoutInflater, ViewGroup, Bundle).
     * First we call our super's onCreate, then we fetch from our arguments the String
     * stored with the index "label" and save it in the field String mLabel, and then
     * we fetch the String stored with the index "text" and save it it the field String
     * mText. We set the style of our BibleDialog DialogFragment to DialogFragment.STYLE_NORMAL
     * TODO: SEE if this is necessary
     *
     * @param savedInstanceState Always null since onSaveInstanceState is not overridden.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    public String[] spinChoices = {"Choose Action", "Random verse", "Google",
                                   "Bookmark", "Go to verse", "Read aloud"};
    public String spinChosen = "";
    public int spinIndex = 0;
    public int lastIndex = 0;
    public static final int CHOICE_NONE = 0;
    public static final int CHOICE_RANDOM_VERSE = 1;
    public static final int CHOICE_GOOGLE = 2;
    public static final int CHOICE_BOOKMARK = 3;
    public static final int CHOICE_GO_TO_VERSE = 4;
    public static final int CHOICE_READ_ALOUD = 5;

    /**
     * AdapterView.OnItemSelectedListener used for the function chooser spinner
     */
    public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener() {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         *
         * Implementers can call getItemAtPosition(position) if they need to access the data
         * associated with the selected item.
         *
         * First we save the position selected and the string associated with that position in
         * the fields spinIndex and spinChosen respectively. Then if it is not the same as the
         * as the lastIndex chosen we call handleAction to perform whatever action is required
         * when that spinIndex is chosen, and on return we save this position as the new lastIndex.
         * If the index was not 0 (our "Choose Action" prompt) we set the visibility of our "REPEAT"
         * Button to VISIBLE (It starts out with a visibility of GONE.)
         *
         * @param parent The AdapterView where the selection happened
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinIndex = position;
            spinChosen = spinChoices[position];

            if (spinIndex != lastIndex) {
                ((BibleMain) getActivity()).handleAction(view, spinIndex);
                lastIndex = spinIndex;
            }
            if (spinIndex != 0) {
                repeatButton.setVisibility(View.VISIBLE);
            }
        }

        /**
         * Callback method to be invoked when the selection disappears from this view.
         * The selection can disappear for instance when touch is activated or when
         * the adapter becomes empty.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * onCreate(Bundle) and onActivityCreated(Bundle). Here we locate and inflate our layout
     * (R.layout.bible_dialog) into View v. Find the TextView for our label (R.id.label), save it
     * in lastLabelView and set the text of that TextView to the contents of the String mLabel.
     * Find the TextView for our verses text (R.id.text), save it in lastTextView and set the text
     * of this TextView to the String mText. It next locates our Spinner in the layout (R.id.spinner)
     * and sets Spinner spin to it, creates ArrayAdapter<String> spinnerArrayAdapter from spinChoices
     * (our String array of actions that the BibleDialog can launch for us), configures it and sets
     * it as the adapter that Spinner spin will use, and sets the AdapterView.OnItemSelectedListener
     * to the spinSelected instance defined above. It next locates our "REPEAT" Button (R.id.repeat)
     * and sets the OnClickListener to an anonymous class which will cause the last action performed
     * to be repeated, and locates our "DISMISS" Button (R.id.dismiss) and sets the OnClickListener
     * to an anonymous class which will dismiss our BibleDialog. We finally return the View v we have
     * created and configured.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState Always null since onSaveInstanceState is not overridden.
     *
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bible_dialog, container, false);
        View tv = v.findViewById(R.id.label);
        lastLabelView = (TextView) tv;
        String dialogLabel = mLabel;
        ((TextView) tv).setText(dialogLabel);

        tv = v.findViewById(R.id.text);
        lastTextView = (TextView) tv;
        ((TextView) tv).setText(mText);

        Spinner spin = (Spinner) v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_spinner_item,
                        spinChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);
        spin.setOnItemSelectedListener(spinSelected);

        // Watch for button clicks.
        repeatButton = (Button) v.findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Repeat the last action processed when clicked
             *
             * @param v REPEAT Button which has been clicked
             */
            @Override
            public void onClick(View v) {
                ((BibleMain) getActivity()).handleAction(v, spinIndex);
            }
        });

        Button button = (Button) v.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Dismiss this BibleDialog. It does this by fetching the activity our DialogFragment
             * is associated with (BibleMain) and using this to access the convenience function
             * dismissDialog which will dismiss us.
             *
             * @param v DISMISS Button which has been clicked
             */
            @Override
            public void onClick(View v) {
                ((BibleMain) getActivity()).dismissDialog();
            }
        });

        return v;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally tied to Activity.onResume of the containing Activity's
     * lifecycle. I just override for educational purposes.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "I have been resumed");
    }
}
