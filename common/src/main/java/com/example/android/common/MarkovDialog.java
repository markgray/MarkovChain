package com.example.android.common;

import android.app.DialogFragment;
import android.os.Bundle;

/**
 * {@code DialogFragment} to display number of possibilities given the first two words, and the text
 * we randomly generated using {@code Markov.line}.
 */
public class MarkovDialog extends DialogFragment {
    private static final String TAG = "MarkovDialog"; // TAG used for logging
    public String mLabel; // label for the DialogFragment
    public String mText;  // text of the current verse


    MarkovDialog newInstance(String label, String text) {
        MarkovDialog f = new MarkovDialog();

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

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

}
