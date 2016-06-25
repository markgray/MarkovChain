package com.example.android.markovchain;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;

public class BibleSearch extends DialogFragment {
    public final static String TAG = "BibleSearch";
    public String mLabel;
    public String mText;
    public String[] mSuggestions;
    public ArrayAdapter<String> adapter;

    static BibleSearch newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleSearch f = new BibleSearch();

        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    public String noPunct(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length() ; i++) {
            char c = text.charAt(i);
            if (!".,;:()!?".contains(String.valueOf(c))) {
                stringBuilder.append(text.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public String[] uniq(String[] strings) {
        HashSet<String> setOfStrings = new HashSet<>(strings.length);
        Collections.addAll(setOfStrings, strings);
        String[] returnStringArray = new String[setOfStrings.size()];
        for (int i = 0; i < setOfStrings.size(); i++) {
            returnStringArray[i] = (String) setOfStrings.toArray()[i];
        }
        return returnStringArray;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        //noinspection ConstantConditions
        mSuggestions = uniq(noPunct(mText).split(" "));
        adapter = new ArrayAdapter<>(BibleMain.bibleContext,
                android.R.layout.simple_dropdown_item_1line, mSuggestions);

        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_search, container, false);
        View tv = v.findViewById(R.id.label);
        String dialogLabel = mLabel;
        ((TextView) tv).setText(dialogLabel);

        tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(mText);

        final MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) v.findViewById(R.id.edit);
        textView.setAdapter(adapter);
        textView.setTokenizer(new SpaceTokenizer());

        // Watch for button clicks.
        Button button = (Button) v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                String query = textView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                startActivity(intent);
                BibleSearch.this.dismiss();
            }
        });
        return v;
    }
}
