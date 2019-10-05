package com.example.android.markovchain;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
 * This {@code DialogFragment} allows a user to choose a specific verse of the Bible
 */
@SuppressWarnings("WeakerAccess")
public class BibleChoose extends DialogFragment {
    /**
     * TAG used for logging
     */
    public final static String TAG = "BibleChoose";
    /**
     * Bible citation for the current verse
     */
    public String mLabel;
    /**
     * Text of the current verse
     */
    public String mText;
    /**
     * Name of the book chosen using the spinner
     */
    public String mBook;
    /**
     * Chapter and verse read from the {@code EditText} used for that purpose
     */
    public String mChapterAndVerse;

    /**
     * Create a new {@code BibleChoose} {@code DialogFragment} instance. First we initialize our variable
     * {@code BibleChoose f} with a new instance, and initialize our variable {@code Bundle args} with a
     * a new instance. We add our parameter {@code String label} to {@code args} using the key "label",
     * add our parameter {@code String text} to {@code args} using the key "text", and then we set the
     * arguments of {@code f} to be the {@code Bundle args}. Finally we return {@code f} to the caller.
     *
     * @param label Canonical Bible citation of current verse
     * @param text  Verse text
     * @return {@code BibleChoose} instance with its arguments set to a bundle that contains our parameters
     * {@code label} and {@code text}
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
     * Called to do initial creation of this {@code BibleChoose} {@code DialogFragment} instance.
     * First we call our super's implementation of {@code onCreate}. We initialize our field
     * {@code String mLabel} to the string stored in our argument {@code Bundle} under the key
     * "label", and initialize our field {@code String mText} to the string stored in our argument
     * {@code Bundle} under the key "text". We log the fact that we were called and finally set our
     * style to STYLE_NORMAL with a default theme.
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use
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
     * Called to have the fragment instantiate its user interface view. We initialize our variable
     * {@code View v} by using our parameter {@code LayoutInflater inflater} to inflate our layout
     * field R.layout.bible_choose using our parameter {@code ViewGroup container} for the layout
     * params without attaching to it. We initialize our variable {@code TextView tv} by finding the
     * view in {@code v} with the id R.id.label and set its text to our field {@code String mLabel}
     * (canonical Bible citation for our current verse). We then set {@code tv} to the view in {@code v}
     * found with the id R.id.text and set its text to our field {@code String mText} (text for our
     * current verse). We initialize our variable {@code Spinner spin} by finding the view in {@code v}
     * with id R.id.spinner. We initialize our variable {@code ArrayAdapter<String> spinnerArrayAdapter}
     * with a new instance which uses the {@code Context} of {@code v} as its context, the system
     * layout file android.R.layout.simple_spinner_item for its item layout file, and the {@code books}
     * field of {@code BibleMain} as its object dataset. We set the layout resource to create the drop
     * down views of {@code spinnerArrayAdapter} to the file android.R.layout.simple_spinner_dropdown_item.
     * We then set the adapter of {@code spin} to {@code spinnerArrayAdapter}, set its {@code OnItemSelectedListener}
     * to our field {@code AdapterView.OnItemSelectedListener spinSelected}, and set its selection to
     * the item number determined by the {@code indexFromCitation} method of {@code BibleMain} when
     * passed the citation in our field {@code mLabel}. We next initialize our variable {@code EditText cavEditText}
     * by finding the view in {@code v} with id R.id.chapter_and_verse. We initialize our variable {@code Button button}
     * by finding the view in {@code v} with id R.id.show and set its {@code OnClickListener} to an anonymous
     * class whose {@code onClick} override which loads our field {@code String mBook} with the data corresponding
     * to the currently selected item in {@code spin} and loads our field {@code String mChapterAndVerse} with
     * the text contents of {@code cavEditText}. If {@code mChapterAndVerse} contains the string ":"
     * we initialize our variable {@code String citationChosen} to the string formed by concatenating
     * the book number in the {@code String[] numbers} field of {@code BibleAdapter} in position {@code bookNum}
     * to the string ":" followed by {@code mChapterAndVerse}, otherwise we set it to the string formed
     * by concatenating the book number in the {@code String[] numbers} field of {@code BibleAdapter} in
     * position {@code bookNum} to the string ":" followed by {@code mChapterAndVerse} followed by the
     * string ":1". We initialize our variable {@code String citationFallback} to the string formed by
     * concatenating the book number in the {@code String[] numbers} field of {@code BibleAdapter} in
     * position {@code bookNum} followed by the string ":1:1". We then initialize our variable {@code int verseNumber}
     * to the value returned by the {@code findFromCitation} method of {@code BibleMain} when passed
     * {@code citationChosen} and {@code citationFallback}. We then call the {@code moveToVerse} method
     * of {@code BibleAdapter} to move to verse {@code verseNumber}, dismiss this instance of {@code BibleChoose},
     * set the {@code mLabel} field of the {@code bibleDialog} field of {@code BibleMain} to the {@code dialogTitle}
     * field of {@code BibleMain}, and the {@code mText} field of the {@code bibleDialog} field of {@code BibleMain}
     * to the {@code dialogText} field of {@code BibleMain}.
     * <p>
     * After setting the {@code OnClickListener} we return {@code v} to the caller.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate
     *                  any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.  The fragment should not add the view itself,
     *                  but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use
     * @return The View v for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_choose, container, false);
        TextView tv = v.findViewById(R.id.label);
        tv.setText(mLabel);

        tv = v.findViewById(R.id.text);
        tv.setText(mText);

        final Spinner spin = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_spinner_item,
                        BibleAdapter.books);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);
        spin.setOnItemSelectedListener(spinSelected);
        spin.setSelection(BibleMain.indexFromCitation(mLabel));

        final EditText cavEditText = v.findViewById(R.id.chapter_and_verse);

        // Watch for button clicks.
        Button button = v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the button with id R.id.show ("GO") is clicked. First we load our field
             * {@code String mBook} with the data corresponding to the currently selected item in
             * {@code spin} and our field {@code String mChapterAndVerse} with the text contents of
             * {@code cavEditText}. We initialize our variable {@code int bookNum} to the item position
             * of the currently selected item in {@code spin}. If {@code mChapterAndVerse} contains the
             * string ":" we initialize our variable {@code String citationChosen} to the string formed
             * by concatenating the book number in the {@code String[] numbers} field of {@code BibleAdapter}
             * in position {@code bookNum} to the string ":" followed by {@code mChapterAndVerse}, otherwise
             * we set it to the string formed by concatenating the book number in the {@code String[] numbers}
             * field of {@code BibleAdapter} in position {@code bookNum} to the string ":" followed by
             * {@code mChapterAndVerse} followed by the string ":1". We initialize our variable
             * {@code String citationFallback} to the string formed by concatenating the book number
             * in the {@code String[] numbers} field of {@code BibleAdapter} in position {@code bookNum}
             * followed by the string ":1:1". We then initialize our variable {@code int verseNumber}
             * to the value returned by the {@code findFromCitation} method of {@code BibleMain} when
             * passed {@code citationChosen} and {@code citationFallback}. We then call the {@code moveToVerse}
             * method of {@code BibleAdapter} to have it move to verse {@code verseNumber}, dismiss this
             * instance of {@code BibleChoose}, set the {@code mLabel} field of the {@code bibleDialog}
             * field of {@code BibleMain} to the {@code dialogTitle} field of {@code BibleMain}, and
             * the {@code mText} field of the {@code bibleDialog} field of {@code BibleMain} to the
             * {@code dialogText} field of {@code BibleMain}.
             *
             * @param v {@code View} that was clicked.
             */
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

    /**
     * Book number selected in the {@code Spinner}
     */
    public int bookNumber = 0;
    /**
     * Book name corresponding to the {@code bookNumber}
     */
    public String bookName;
    /**
     * {@code OnItemSelectedListener} for the adapter of the {@code Spinner}
     */
    public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener() {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         * Implementers can call {@code getItemAtPosition(position)} if they need to access
         * the data associated with the selected item.
         * <p>
         * We simply squirrel away the {@code int position} passed us in the field {@code bookNumber}
         * and the book name corresponding to that book number in the field {@code bookName}
         *
         * @param parent   The AdapterView where the selection happened
         * @param view     The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
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
         * <p>
         * We do nothing.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

}
