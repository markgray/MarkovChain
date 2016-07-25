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
     * Create and initialize a BibleDialog DialogFragment
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
     * Update the text displayed in the BibleDialog for new verse
     *
     * @param label Canonical Bible citation for the verse
     * @param text Text of the verse
     */
    public void refresh(String label, String text) {
        lastLabelView.setText(label);
        lastTextView.setText(text);
        repeatButton.setVisibility(View.VISIBLE);
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
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
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
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.

     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
            @Override
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((BibleMain) getActivity()).handleAction(v, spinIndex);
            }
        });

        Button button = (Button) v.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BibleMain) getActivity()).dismissDiaglog();
            }
        });

        return v;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally tied to Activity.onResume of the containing Activity's
     * lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "I have been resumed");
    }
}
