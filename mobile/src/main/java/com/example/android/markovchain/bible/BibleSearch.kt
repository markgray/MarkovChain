package com.example.android.markovchain.bible

import androidx.fragment.app.DialogFragment
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.MultiAutoCompleteTextView
import android.widget.TextView
import com.example.android.markovchain.R
import com.example.android.markovchain.util.SpaceTokenizer

import java.util.Collections
import java.util.HashSet

/**
 * This `DialogFragment` allows you to search for words or phrases in the current verse on the
 * web using Google search.
 */
class BibleSearch : DialogFragment() {
    /**
     * Canonical Bible citation for current verse
     */
    private var mLabel: String? = null
    /**
     * Current verse
     */
    private var mText: String? = null
    /**
     * Array containing all words in current verse (duplicates removed)
     */
    private lateinit var mSuggestions: Array<String>
    /**
     * Suggestions Adapter for the `MultiAutoCompleteTextView` with id R.id.edit
     */
    private lateinit var mAdapter: ArrayAdapter<String>

    /**
     * Removes punctuation characters .,;:()!? from the verse passed it. First we initialize our
     * [StringBuilder] variable `val stringBuilder` with a new instance. Then looping over `i`
     * through all the characters in our [String] parameter [text] we grab each [Char] `val c`
     * from [text] and if it is not one of the punctuation characters ".,;:()!?" we append it
     * to `stringBuilder`, otherwise we ignore it. Finally we return the [String] value of
     * `stringBuilder` to the caller.
     *
     * @param text Verse containing punctuation characters
     * @return Same verse minus all punctuation
     */
    private fun noPunct(text: String): String {
        val stringBuilder = StringBuilder()
        for (i in text.indices) {
            val c = text[i]
            if (!".,;:()!?".contains(c.toString())) {
                stringBuilder.append(text[i])
            }
        }
        return stringBuilder.toString()
    }

    /**
     * Given an array of strings which might contain duplicate strings, remove all duplicates. First
     * we initialize our [HashSet] variable `val setOfStrings` with a new instance with an
     * initial capacity as large as the length of our `String[]` parameter [strings]. Then we add
     * all the String's in [strings] to `setOfStrings` using the `addAll` method of `Collections`
     * applying the * "Spread Operator" on [strings] to convert [strings] to the *varargs* argument
     * that `addAll` expects (this has the effect of only adding a [String] if it is not already
     * present in the set ,thus removing the duplicates). Finally we return the `Array<String>`
     * that its `toTypedArray` method converts `setOfStrings` to.
     *
     * @param strings String array with possible duplicate string members
     * @return Same array with only single occurrences of strings
     */
    private fun uniq(strings: Array<String>): Array<String> {
        val setOfStrings = HashSet<String>(strings.size)
        Collections.addAll(setOfStrings, *strings)
        return setOfStrings.toTypedArray()
    }

    /**
     * Called to do initial creation of a [BibleSearch] `DialogFragment`. This is called after
     * `onAttach(Activity)` and before `onCreateView(LayoutInflater, ViewGroup, Bundle)`. First
     * we call through to our super's implementation of `onCreate`, then we initialize our [String]
     * field [mLabel] to the string stored in our argument [Bundle] under the key "label", and our
     * [String] field [mText] to the string stored in our argument `Bundle` under the key "text".
     * We initialize our `String[]` field [mSuggestions] (the array of suggestions for our `EditText`)
     * by first removing all punctuation characters from [mText] using our [noPunct] method, then
     * splitting the result into a [String] array using the delimiter " ", (the `dropLastWhile`
     * method is added to the *pipeline* because the kotlin `split` adds a trailing empty `String`)
     * and removing all duplicates from the array that the `toTypedArray` method produces from this
     * using our method [uniq]. We then initialize our `ArrayAdapter<String>` field [mAdapter] using
     * [mSuggestions] as the constants to be used in the `ListView` when [mAdapter] is used for the
     * suggestions in our `EditText`. Finally we set our `DialogFragment`style to STYLE_NORMAL (for
     * no better reason then it was done in the sample code we studied).
     * TODO: Improve the styles used for all DialogFragment's and Spinner's
     *
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mLabel = requireArguments().getString("label")
        mText = requireArguments().getString("text")

        mSuggestions = uniq(
                noPunct(mText!!)
                        .split(" ".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
        )
        mAdapter = ArrayAdapter(BibleActivity.bibleContext,
                android.R.layout.simple_dropdown_item_1line, mSuggestions)

        Log.i(TAG, "onCreate called with: $mLabel $mText")

        setStyle(STYLE_NORMAL, 0)
    }

    /**
     * Called to have the fragment instantiate its user interface view. First we initialize our [View]
     * variable `val v` by using our [LayoutInflater] parameter [inflater] to inflate our layout file
     * R.layout.bible_search, using our [ViewGroup] parameter [container] for the layout params
     * without attaching to it. We initialize our [TextView] variable `var tv` by finding the view
     * in `v` with id R.id.label and set its text to our [String] field [mLabel], then we set `tv`
     * by finding the view in `v` with id R.id.text and set its text to our field `String mText`.
     * We initialize our [MultiAutoCompleteTextView] variable `val textView` by finding the view in
     * `v` with id R.id.edit, set its adapter to our `ArrayAdapter<String>` field [mAdapter] and set
     * its `Tokenizer` to a new instance of our class [SpaceTokenizer] (splits the text the user
     * enters into words using only spaces as the delimiter). We initialize our [Button] variable
     * `val button` by finding the view in `v` with id R.id.show ("SEARCH") and set its `OnClickListener`
     * to an a lambda whose `onClick` override will create an [Intent] to do a Google search on the
     * text which has been entered in `textView`. Finally we return `v` to our caller.
     *
     * @param inflater A `LayoutInflater` object that can be used to inflate an XML layout file
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView called")
        val v = inflater.inflate(R.layout.bible_search, container, false)

        var tv = v.findViewById<TextView>(R.id.label)
        tv.text = mLabel

        tv = v.findViewById(R.id.text)
        tv.text = mText

        val textView = v.findViewById<MultiAutoCompleteTextView>(R.id.edit)
        textView.setAdapter(mAdapter)
        textView.setTokenizer(SpaceTokenizer())

        // Watch for button clicks.
        val button = v.findViewById<Button>(R.id.show)
        /*
         * Called when the "SEARCH" Button (R.id.show) is clicked. First we retrieve the String
         * query from our `MultiAutoCompleteTextView` field `textView` to initialize our `String`
         * variable `val query`, initialize our `Intent` variable `val intent` with a new instance
         * whose action is ACTION_WEB_SEARCH, add `query` as extended data to `intent`
         * using SearchManager.QUERY as the name of the data, and then we launch the `Activity`
         * we specified in `Intent intent`. We make sure that the `BibleDialog` that launched us
         * has an up to date value for its fields `mLabel` and `mText`, and finally we dismiss this
         * `BibleSearch` `DialogFragment` instance.
         *
         * Parameter: View of Button that was clicked.
         */
        button.setOnClickListener {
            // Create an Intent to perform a Google search and launch it.
            val query = textView.text.toString()
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, query) // query contains search string
            startActivity(intent)


            BibleActivity.bibleDialog!!.mLabel = BibleActivity.dialogTitle
            BibleActivity.bibleDialog!!.mText = BibleActivity.dialogText
            this@BibleSearch.dismiss()
        }
        return v
    }

    companion object {
        /**
         * TAG used for logging
         */
        const val TAG = "BibleSearch"

        /**
         * Create and initialize a [BibleSearch] `DialogFragment`. First we initialize our
         * [BibleSearch] variable `val f` with a new instance. We initialize our [Bundle] variable
         * `val args` with a new instance then store our [String] parameter [label] in it under the
         * key "label" and our [String] parameter [text] under the key "text". We set the argument
         * bundle of `f` to `args` and return `f` to the caller.
         *
         * @param label Label to use
         * @param text  Text to use
         * @return Configured `BibleSearch` instance
         */
        fun newInstance(label: String, text: String): BibleSearch {
            Log.i(TAG, " newInstance called with: $label $text")
            val f = BibleSearch()

            val args = Bundle()
            args.putString("label", label)
            args.putString("text", text)

            f.arguments = args
            return f
        }
    }
}
