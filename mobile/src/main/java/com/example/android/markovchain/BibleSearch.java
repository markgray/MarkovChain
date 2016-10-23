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

/**
 * This DialogFragment allows you to search for words or phrases in the current verse on the web
 * using Google search.
 */
public class BibleSearch extends DialogFragment {
    public final static String TAG = "BibleSearch"; // TAG used for logging
    public String mLabel; // Canonical Bible citation for current verse
    public String mText;  // Current verse
    public String[] mSuggestions; // Array containing all words in current verse (duplicates removed)
    public ArrayAdapter<String> mAdapter; // Suggestions Adapter for MultiAutoCompleteTextView textView

    /**
     * Create and initialize a BibleSearch DialogFragment. First we create a new BibleSearch instance
     * "f", we create a Bundle "args" and store our parameter <code>label</code> in it under the key
     * "label" and our parameter <code>text</code> under the key "text". We set the arguments of our
     * BibleSearch instance <code>f</code> to the Bundle args and return <code>f</code> to the caller.
     *
     * @param label Label to use
     * @param text  Text to use
     * @return Initialized BibleSearch
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
     * Remove punctuation characters .,;:()!? from the verse passed it. First we create a StringBuilder
     * stringBuilder, then looping through all the characters in our parameter String text we grab
     * each <code>char c</code> from <code>text</code> and if it is not one of the punctuation
     * characters ".,;:()!?" we append it to our <code>StringBuilder stringBuilder</code>, otherwise
     * we ignore it. Finally we return the String contents of <code>stringBuilder</code> to the
     * caller.
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
     * Given an array of strings which might contain duplicate strings remove all duplicates. First
     * we create HashSet<String> setOfStrings, then we add all the String's in our parameter string
     * to the set using Collections.addAll. This has the effect of only adding a String if it is not
     * already present in the set (thus removing the duplicates). We create Object[] tempObjectArray
     * to temporarily hold a copy of all of the Objects in our setOfStrings (necessary because
     * <code>HashSet<String>.toArray()</code> returns an array of Object's, not String's). Then we
     * create the array <code>String[] returnStringArray</code> and loop through the Object's in
     * <code>tempObjectArray</code>, casting them to String before storing them in returnStringArray.
     * Finally we return returnStringArray to our caller.
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
     * First we call through to our super's implementation of onCreate, then we initialize our
     * fields <code>mLabel</code> and <code>mText</code> from our arguments using the keys "label"
     * and "text" respectively. Then we initialize our field <code>String[] mSuggestions</code>
     * (the array of suggestions for our EditText) by first removing all punctuation characters
     * from <code>mText</code>, splitting the result into a String array using the delimiter " ",
     * and removing all duplicates from that array using our method <code>uniq</code>. We then
     * initialize our field <code>ArrayAdapter<String> mAdapter</code> using <code>mSuggestions</code>
     * as the constants to be used in the ListView when <code>mAdapter</code> is used for the
     * suggestions in our EditText. Finally we set our DialogFragment style to STYLE_NORMAL (for
     * no better reason then it was done in the sample code we studied.
     * TODO: Improve the styles used for all DialogFragment's and Spinner's
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden.
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
        textView.setAdapter(mAdapter);
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
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
            }
        });
        return v;
    }
}
