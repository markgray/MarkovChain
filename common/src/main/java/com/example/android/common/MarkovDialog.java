package com.example.android.common;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * {@code DialogFragment} to display number of possibilities given the first two words, and the verse
 * we randomly generated using {@code Markov.line}.
 */
public class MarkovDialog extends DialogFragment {
    private static final String TAG = "MarkovDialog"; // TAG used for logging
    public String mPossibles; // possibles for the DialogFragment
    public String mVerse;  // verse of the current verse


    public static MarkovDialog newInstance(String possibles, String verse) {
        MarkovDialog f = new MarkovDialog();

        Bundle args = new Bundle();
        args.putString("possibles", possibles);
        args.putString("verse", verse);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPossibles = getArguments().getString("possibles");
        mVerse = getArguments().getString("verse");

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.markov_dialog, container, false);
        TextView pv = (TextView) v.findViewById(R.id.possibles);
        pv.setText(mPossibles);
        TextView vv = (TextView) v.findViewById(R.id.verse);
        vv.setText(mVerse);

        Button db = (Button) v.findViewById(R.id.dismiss);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
