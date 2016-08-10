package com.example.android.markovchain;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * This DialogFragment allows a user to choose a specific verse of the Bible
 */
public class BibleChoose extends DialogFragment {
    public final static String TAG = "BibleChoose";
    public String mLabel;
    public String mText;
    public String mBook;
    public String mChapterAndVerse;

    /**
     * Create a new BibleChoose DialogFragment instance
     *
     * @param label Canonical Bible citation of current verse
     * @param text  Verse text
     * @return BibleChoose instance with args .setArguments to a bundle containing label and text
     */
    public static BibleChoose newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleChoose f = new BibleChoose();
        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    /**
     * Called to do initial creation of a BibleChoose DialogFragment. It sets the fields mLabel and
     * mText of the instance to the values "label" and "text" contained in the arguments Bundle
     * respectively, and sets the style used by the DialogFragment to STYLE_NORMAL.
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
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
     * Called to have the fragment instantiate its user interface view. It inflates our layout file
     * R.layout.bible_choose to be our View v, locates the TextView for our label in v (R.id.label)
     * and sets the text to be the canonical Bible citation for our current verse mLabel, finds the
     * TextView for our verse (R.id.text) and sets the text to the verse text for our current verse
     * mText. It then finds the Spinner spin (R.id.spinner) which we will use for our dropdown Bible
     * book chooser. It creates ArrayAdapter<String> spinnerArrayAdapter from the names of the books
     * in the Bible (BibleAdapter.books) using android.R.layout.simple_spinner_item as their layout,
     * and sets "spinnerArrayAdapter" to be the adapter for "spin". Sets the OnItemSelectedListener
     * to an anonymous method which merely stores the selected item away in bookNumber and bookName
     * for later use. Sets the Spinner selection to the book the current verse is contained in by
     * parsing the Bible citation for the verse contained in dialogLabel by using the method
     * BibleMain.indexFromCitation(dialogLabel). It next finds the EditText used to enter chapter
     * and verse (R.id.chapter_and_verse) and stashes it away for later use in the onClickListener
     * for the "GO" Button. Next it locates the "GO" Button (R.id.show) and sets the onClickListener
     * to a method instended to move to the verse selected by the Spinner spin and the EditText
     * cavEditText.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate
     *                  any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState Always null since onSaveInstanceState is not overridden.
     *
     * @return The View v for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_choose, container, false);
        View tv = v.findViewById(R.id.label);
        String dialogLabel = mLabel;
        ((TextView) tv).setText(dialogLabel);

        tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(mText);

        final Spinner spin = (Spinner) v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_spinner_item,
                        BibleAdapter.books);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);
        spin.setOnItemSelectedListener(spinSelected);
        spin.setSelection(BibleMain.indexFromCitation(dialogLabel));

        final EditText cavEditText = (EditText) v.findViewById(R.id.chapter_and_verse);

        // Watch for button clicks.
        Button button = (Button) v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                mBook = (String) spin.getSelectedItem();
                mChapterAndVerse = cavEditText.getText().toString();
                Log.i(TAG, mBook + " " + mChapterAndVerse);
                int bookNum = spin.getSelectedItemPosition();
                final String citationChosen = mChapterAndVerse.contains(":") ?
                        BibleAdapter.numbers[bookNum] + ":" + mChapterAndVerse
                        : BibleAdapter.numbers[bookNum] + ":" + mChapterAndVerse + ":1";
                final String citationFallback = BibleAdapter.numbers[bookNum] + ":1:1";
                Log.i(TAG, citationChosen + " or " + citationFallback);
                int verseNumber = BibleMain.findFromCitation(citationChosen, citationFallback);
                BibleAdapter.moveToVerse(v, verseNumber);
                BibleChoose.this.dismiss();
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
            }
        });

        return v;
    }

    public int bookNumber = 0;
    public String bookName;
    public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener() {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         * Implementers can call getItemAtPosition(position) if they need to access
         * the data associated with the selected item.
         *
         * @param parent The AdapterView where the selection happened
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            bookNumber = position;
            bookName = BibleAdapter.books[position];

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

}
