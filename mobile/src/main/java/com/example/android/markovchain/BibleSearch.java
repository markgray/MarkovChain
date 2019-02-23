package com.example.android.markovchain;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
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

/**
 * This {@code DialogFragment} allows you to search for words or phrases in the current verse on the
 * web using Google search.
 */
public class BibleSearch extends DialogFragment {
    /**
     * TAG used for logging
     */
    public final static String TAG = "BibleSearch";
    /**
     * Canonical Bible citation for current verse
     */
    public String mLabel;
    /**
     * Current verse
     */
    public String mText;
    /**
     * Array containing all words in current verse (duplicates removed)
     */
    public String[] mSuggestions;
    /**
     * Suggestions Adapter for the {@code MultiAutoCompleteTextView} with id R.id.edit
     */
    public ArrayAdapter<String> mAdapter;

    /**
     * Create and initialize a {@code BibleSearch} {@code DialogFragment}. First we initialize our
     * variable {@code BibleSearch f} with a new instance. We initialize our variable {@code Bundle args}
     * with a new instance then store our parameter {@code String label} in it under the key "label"
     * and our parameter {@code String text} under the key "text". We set the argument bundle of
     * {@code f} to {@code args} and return {@code f} to the caller.
     *
     * @param label Label to use
     * @param text  Text to use
     * @return Configured {@code BibleSearch} instance
     */
    static BibleSearch newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleSearch f = new BibleSearch();

        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    /**
     * Removes punctuation characters .,;:()!? from the verse passed it. First we initialize our
     * variable {@code StringBuilder stringBuilder} with a new instance. Then looping over {@code i}
     * through all the characters in our parameter {@code String text} we grab each {@code char c}
     * from {@code text} and if it is not one of the punctuation characters ".,;:()!?" we append it
     * to {@code stringBuilder}, otherwise we ignore it. Finally we return the String value of
     * {@code stringBuilder} to the caller.
     *
     * @param text Verse containing punctuation characters
     * @return Same verse minus all punctuation
     */
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

    /**
     * Given an array of strings which might contain duplicate strings, remove all duplicates. First
     * we initialize our variable {@code HashSet<String> setOfStrings} with a new instance with an
     * initial capacity as large as the length of our parameter {@code String[] strings}, then we add
     * all the String's in {@code strings} to {@code setOfStrings} using the {@code addAll} method of
     * {@code Collections} (this has the effect of only adding a String if it is not already present
     * in the set ,thus removing the duplicates). We initialize {@code Object[] tempObjectArray} to
     * the array of string objects returned by the {@code toArray} method of {@code setOfStrings}
     * (necessary because {@code toArray} returns an array of {@code Object} instead of strings),
     * and allocate an array of strings the same size as {@code setOfStrings} to initialize our variable
     * {@code String[] returnStringArray}. Then we loop over {@code int i} setting {@code returnStringArray[i]}
     * to {@code tempObjectArray[i]} (casting it to {@code String} of course). Finally we return
     * {@code returnStringArray} to our caller.
     *
     * @param strings String array with possible duplicate string members
     * @return Same array with only single occurrences of strings
     */
    private String[] uniq(String[] strings) {
        HashSet<String> setOfStrings = new HashSet<>(strings.length);
        Collections.addAll(setOfStrings, strings);
        Object[] tempObjectArray = setOfStrings.toArray();
        String[] returnStringArray = new String[setOfStrings.size()];
        for (int i = 0; i < setOfStrings.size() ; i++) {
            returnStringArray[i] = (String) tempObjectArray[i];
        }
        return returnStringArray;
    }

    /**
     * Called to do initial creation of a DialogFragment. This is called after {@code onAttach(Activity)}
     * and before {@code onCreateView(LayoutInflater, ViewGroup, Bundle)}. First we call through to
     * our super's implementation of {@code onCreate}, then we initialize our field {@code String mLabel}
     * to the string stored in our argument {@code Bundle} under the key "label", and our field
     * {@code String mText} to the string stored in our argument {@code Bundle} under the key "text".
     * We initialize our field {@code String[] mSuggestions} (the array of suggestions for our {@code EditText})
     * by first removing all punctuation characters from {@code mText} using our {@code noPunct} method,
     * splitting the result into a String array using the delimiter " ", and removing all duplicates
     * from that array using our method {@code uniq}. We then initialize our field {@code ArrayAdapter<String> mAdapter}
     * using {@code mSuggestions} as the constants to be used in the {@code ListView} when {@code mAdapter}
     * is used for the suggestions in our {@code EditText}. Finally we set our {@code DialogFragment}
     * style to STYLE_NORMAL (for no better reason then it was done in the sample code we studied).
     * TODO: Improve the styles used for all DialogFragment's and Spinner's
     *
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        //noinspection ConstantConditions
        mSuggestions = uniq(noPunct(mText).split(" "));
        mAdapter = new ArrayAdapter<>(BibleMain.bibleContext,
                android.R.layout.simple_dropdown_item_1line, mSuggestions);

        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    /**
     * Called to have the fragment instantiate its user interface view. First we initialize our variable
     * {@code View v} by using our parameter {@code LayoutInflater inflater} to inflate our layout
     * file R.layout.bible_search using our parameter {@code ViewGroup container} for the layout params
     * without attaching to it. We initialize our variable {@code TextView tv} by finding the view in
     * {@code v} with id R.id.label and set its text to our field {@code String mLabel}, then we set
     * {@code tv} by finding the view in {@code v} with id R.id.text and set its text to our field
     * {@code String mText}. We initialize our variable {@code MultiAutoCompleteTextView textView} by
     * finding the view in {@code v} with id R.id.edit, set its adapter to our field {@code ArrayAdapter<String> mAdapter}
     * and set its {@code Tokenizer} to a new instance of our class {@code SpaceTokenizer} (splits the
     * text the user enters into words using only spaces as the delimiter). We initialize our variable
     * {@code Button button} by finding the view in {@code v} with id R.id.show ("SEARCH") and set its
     * {@code OnClickListener} to an anonymous class whose {@code onClick} override will create an
     * {@code Intent} to do a Google search on the text which has been entered in {@code textView}.
     * Finally we return {@code v} to our caller.

     * @param inflater A {@code LayoutInflater} object that can be used to inflate an XML layout file
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     * @return Return the View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.bible_search, container, false);

        TextView tv = v.findViewById(R.id.label);
        tv.setText(mLabel);

        tv = v.findViewById(R.id.text);
        tv.setText(mText);

        final MultiAutoCompleteTextView textView = v.findViewById(R.id.edit);
        textView.setAdapter(mAdapter);
        textView.setTokenizer(new SpaceTokenizer());

        // Watch for button clicks.
        Button button = v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the "SEARCH" Button (R.id.show) is clicked. First we retrieve the String
             * query from our field {@code MultiAutoCompleteTextView textView} to initialize our variable
             * {@code String query}, initialize our variable {@code Intent intent} with a new instance
             * whose action is ACTION_WEB_SEARCH, add {@code query} as extended data to {@code intent}
             * using SearchManager.QUERY as the name of the data, and then we launch the {@code Activity}
             * we specified in {@code Intent intent}. We make sure that the {@code BibleDialog} that
             * launched us has an up to date value for its fields {@code mLabel} and {@code mText},
             * and finally we dismiss this {@code BibleSearch} {@code DialogFragment} instance.
             *
             * @param v View of Button that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Create an Intent to perform a Google search and launch it.
                String query = textView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                startActivity(intent);

                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
                BibleSearch.this.dismiss();
            }
        });
        return v;
    }
}
