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

    /**
     * Create and iniitialize a BibleDialog DialogFragment
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
    public void refresh(String label, String text) {
        lastLabelView.setText(label);
        lastTextView.setText(text);
    }

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

    public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinIndex = position;
            spinChosen = spinChoices[position];

            if (spinIndex != lastIndex) {
                ((BibleMain) getActivity()).handleAction(view, spinIndex);
                lastIndex = spinIndex;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

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
        Button button = (Button) v.findViewById(R.id.repeat);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((BibleMain) getActivity()).handleAction(v, spinIndex);
            }
        });

        button = (Button) v.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BibleMain) getActivity()).dismissDiaglog();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "I have been resumed");
    }
}
