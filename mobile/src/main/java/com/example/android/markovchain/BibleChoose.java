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


public class BibleChoose extends DialogFragment {
    public final static String TAG = "BibleChoose";
    public String mLabel;
    public String mText;
    public String mBook;
    public String mChapterAndVerse;

    public static BibleChoose newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleChoose f = new BibleChoose();
        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

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
        spin.setSelection(33);

        final EditText cavEditText = (EditText) v.findViewById(R.id.chapter_and_verse);

        // Watch for button clicks.
        Button button = (Button) v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
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
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            bookNumber = position;
            bookName = BibleAdapter.books[position];

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

}
