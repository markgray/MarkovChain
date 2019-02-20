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
    /**
     * TAG used for logging
     */
    private static final String TAG = "BibleDialog";
    /**
     * label for the {@code DialogFragment} (we use the canonical Bible citation for the verse)
     */
    public String mLabel;
    /**
     * text of the current verse
     */
    public String mText;
    /**
     * Button to use to repeat last operation selected in the Spinner
     */
    public Button repeatButton;

    /**
     * Create and initialize a {@code BibleDialog} {@code DialogFragment}. First we initialize our
     * variable {@code BibleDialog f} with a new instance and our variable {@code Bundle args} with
     * a new instance. We store our parameter {@code String label} in {@code args} under the key "label",
     * and our parameter {@code String text} under the key "text" then set the argument {@code Bundle}
     * of {@code f} to {@code args}. Finally we return {@code f} to the caller.
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

    /**
     * {@code TextView} in our inflated layout used for the Bible citation label
     */
    TextView lastLabelView;
    /**
     * {@code TextView} in our inflated layout used for the text of the current verse
     */
    TextView lastTextView;
    /**
     * Update the text displayed in the BibleDialog for a new verse. This is necessary only after
     * moving to a random verse at the moment because the same BibleDialog DialogFragment is
     * used. Other actions create DialogFragments which when dismissed need to have the BibleDialog
     * recreated so onCreateView is called, therefore setting the fields BibleMain.bibleDialog.mLabel
     * and BibleMain.bibleDialog.mText is done instead. We set the text of the TextView lastLabelView
     * to the label argument, and the text of the TextView lastTextView to the text argument.
     * lastLabelView and lastTextView are set in the callback onCreateView.
     *
     * @param label Canonical Bible citation for the verse
     * @param text Text of the verse
     */
    public void refresh(String label, String text) {
        lastLabelView.setText(label);
        lastTextView.setText(text);
    }

    /**
     * Called to do initial creation of a {@code DialogFragment}. This is called after {@code onAttach(Activity)}
     * and before {@code onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * First we call our super's implementation of {@code onCreate}, then we fetch from our arguments
     * the String stored under the index "label" and save it in our field {@code mLabel}, and we fetch
     * the String stored under the index "text" and save it it our field {@code mText}. Finally we set
     * the style of our {@code BibleDialog} {@code DialogFragment} to STYLE_NORMAL.
     *
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    /**
     * Objects used in the ArrayAdapter used for our Spinner
     */
    public String[] spinChoices = {"Choose Action", "Random verse", "Google",
                                   "Bookmark", "Go to verse", "Read aloud"};
    /**
     * Which spinChoices action has been chosen
     */
    public String spinChosen = "";
    /**
     * The index of the action chosen in the Spinner
     */
    public int spinIndex = 0;
    /**
     * Used to determine if Spinner has chosen a new action or
     * whether the "REPEAT" Button should be visible
     */
    public int lastIndex = 0;

    // Index values for items chosen in the Spinner, must match entries in String[] spinChoices
    /**
     * "Choose Action"
     */
    public static final int CHOICE_NONE = 0;
    /**
     * "Random verse"
     */
    public static final int CHOICE_RANDOM_VERSE = 1;
    /**
     * "Google"
     */
    public static final int CHOICE_GOOGLE = 2;
    /**
     * "Bookmark"
     */
    public static final int CHOICE_BOOKMARK = 3;
    /**
     * "Go to verse"
     */
    public static final int CHOICE_GO_TO_VERSE = 4;
    /**
     * "Read aloud"
     */
    public static final int CHOICE_READ_ALOUD = 5;

    /**
     * {@code AdapterView.OnItemSelectedListener} used for the function chooser {@code Spinner}
     */
    public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener() {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need to access the data
         * associated with the selected item.
         * <p>
         * First we save the position selected {@code int position} and the string associated with that
         * position we find in our array {@code spinChoices} in the fields {@code spinIndex} and
         * {@code spinChosen} respectively. Then if it is not the same as the as the {@code lastIndex}
         * chosen we call the {@code handleAction} method of our parent {@code BibleMain} activity to
         * perform whatever action is required when that {@code spinIndex} is chosen, and on return we
         * save this position as the new {@code lastIndex}. If the index was not 0 (our "Choose Action"
         * prompt) we set the visibility of our "REPEAT" {@code Button repeatButton} to VISIBLE (It
         * starts out with a visibility of GONE.)
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
         * the adapter becomes empty. We do nothing.
         *
         * @param parent The {@code AdapterView} that now contains no selected item.
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * {@code onCreate(Bundle)} and {@code onActivityCreated(Bundle)}.
     * <p>
     * First we initialize our variable {@code View v} by using our parameter {@code LayoutInflater inflater}
     * to inflate our layout file R.layout.bible_dialog using our parameter {@code ViewGroup container} for
     * the layout params without attaching to it. We initialize {@code TextView tv} by finding the view
     * in {@code v} with id R.id.label, set our field {@code lastLabelView} to {@code tv} and set the
     * text of {@code tv} to our field {@code mLabel}. Next we set {@code tv} by finding the view in {@code v}
     * with id R.id.text, set our field {@code lastTextView} to {@code tv} and set the text of {@code tv}
     * to our field {@code mText}.
     * <p>
     * Next we initialize our variable {@code Spinner spin} by finding the view in {@code v} with id
     * R.id.spinner, and initialize our variable {@code ArrayAdapter<String> spinnerArrayAdapter} with
     * a new instance which uses the {@code Context} of {@code v}, uses the system layout file
     * android.R.layout.simple_spinner_item to instantiate views and uses our field {@code String[] spinChoices}
     * as the data set it will represent. We set android.R.layout.simple_spinner_dropdown_item to be
     * the layout file {@code spinnerArrayAdapter} uses for the drop down views, then set the adapter
     * of {@code spin} to {@code spinnerArrayAdapter}, and set the {@code OnItemSelectedListener} of
     * {@code spin} to be our field {@code spinSelected}.
     * <p>
     * We next initialize our field {@code Button repeatButton} by finding the view in {@code v} with
     * id R.id.repeat ("REPEAT") and sets its {@code OnClickListener} to an anonymous class which will
     * cause the last action performed to be repeated. We initialize our variable {@code Button button}
     * by finding the view with id R.id.dismiss ("DISMISS") and set its {@code OnClickListener} to an
     * anonymous class which will {@code dismiss} our {@code BibleDialog}. Finally we return {@code v}
     * to the caller.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bible_dialog, container, false);
        TextView tv = v.findViewById(R.id.label);
        lastLabelView = tv;
        tv.setText(mLabel);

        tv = v.findViewById(R.id.text);
        lastTextView = tv;
        tv.setText(mText);

        Spinner spin = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_spinner_item,
                        spinChoices);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);
        spin.setOnItemSelectedListener(spinSelected);

        // Watch for button clicks.
        repeatButton = v.findViewById(R.id.repeat);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Repeat the last action processed when clicked. We call the {@code handleAction} method
             * of our {@code BibleMain} parent activity to handle a repeat of the {@code spinIndex}
             * action.
             *
             * @param v REPEAT Button which has been clicked
             */
            @Override
            public void onClick(View v) {
                ((BibleMain) getActivity()).handleAction(v, spinIndex);
            }
        });

        Button button = v.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Dismiss this BibleDialog. It does this by fetching the activity our {@code DialogFragment}
             * is associated with ({@code BibleMain}) and using this to access the convenience function
             * {@code dismissDialog} which will dismiss us.
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
     * This is generally tied to {@code Activity.onResume} of the containing
     * {@code Activity}'s lifecycle. I just override for educational purposes.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "I have been resumed");
    }
}
