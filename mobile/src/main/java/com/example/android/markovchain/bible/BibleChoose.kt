package com.example.android.markovchain.bible

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.android.markovchain.R

/**
 * This [DialogFragment] allows a user to choose a specific verse of the Bible
 */
@Suppress("MemberVisibilityCanBePrivate")
class BibleChoose : DialogFragment() {
    /**
     * Bible citation for the current verse
     */
    var mLabel: String? = null
    /**
     * Text of the current verse
     */
    var mText: String? = null
    /**
     * Name of the book chosen using the spinner
     */
    lateinit var mBook: String
    /**
     * Chapter and verse read from the [EditText] used for that purpose
     */
    lateinit var mChapterAndVerse: String

    /**
     * Book number of the book selected in the `Spinner`
     */
    var bookNumber = 0
    /**
     * Book name corresponding to the `bookNumber`
     */
    lateinit var bookName: String
    /**
     * `OnItemSelectedListener` for the adapter of the `Spinner`
     */
    var spinSelected: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         * Implementers can call `getItemAtPosition(position)` if they need to access
         * the data associated with the selected item.
         *
         * We simply squirrel away the `int position` passed us in the field [bookNumber]
         * and the book name corresponding to that book number in the field [bookName]
         *
         * @param parent   The AdapterView where the selection happened
         * @param view     The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
         */
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            bookNumber = position
            bookName = BibleAdapter.books[position]
        }

        /**
         * Callback method to be invoked when the selection disappears from this view.
         * The selection can disappear for instance when touch is activated or when
         * the adapter becomes empty. We do nothing.
         *
         * @param parent The AdapterView that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    /**
     * Called to do initial creation of this [BibleChoose] instance. First we call our super's
     * implementation of `onCreate`. We initialize our [String] field [mLabel] to the string
     * stored in our argument [Bundle] under the key "label", and initialize our [String] field
     * [mText] to the string stored in our argument [Bundle] under the key "text". We log the
     * fact that we were called and finally set our style to STYLE_NORMAL with a default theme.
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabel = arguments!!.getString("label")
        mText = arguments!!.getString("text")
        Log.i(TAG, "onCreate called with: $mLabel $mText")

        setStyle(STYLE_NORMAL, 0)
    }

    /**
     * Called to have the fragment instantiate its user interface view. We initialize our [View]
     * `val v` by using our [LayoutInflater] parameter [inflater] to inflate our layout file
     * R.layout.bible_choose using our [ViewGroup] parameter [container] for the layout params
     * without attaching to it. We initialize our [TextView] `var tv` by finding the view in `v`
     * with the id R.id.label and set its text to our [String] field [mLabel] (canonical Bible
     * citation for our current verse). We then set `tv` to the view in `v` found with the id
     * R.id.text and set its text to our [String] field [mText] (text for our current verse). We
     * initialize our [Spinner] `val spin` by finding the view in `v` with id R.id.spinner. We
     * initialize our `ArrayAdapter<String>` variable `val spinnerArrayAdapter` with a new instance
     * which uses the `Context` of `v` as its context, the system spinner item layout file
     * android.R.layout.simple_spinner_item for its item layout file, and the `books` field of
     * `BibleMain` as its object dataset. We set the layout resource to create the drop down views
     * of `spinnerArrayAdapter` to the file android.R.layout.simple_spinner_dropdown_item.
     * We then set the adapter of `spin` to `spinnerArrayAdapter`, set its `OnItemSelectedListener`
     * to our `AdapterView.OnItemSelectedListener` field [spinSelected], and set its selection to
     * the item number determined by the `indexFromCitation` method of `BibleMain` when
     * passed the citation in our field [mLabel]. We next initialize our [EditText] variable
     * `val cavEditText` by finding the view in `v` with id R.id.chapter_and_verse. We initialize
     * our [Button] variable `val button` by finding the view in `v` with id R.id.show and set its
     * `OnClickListener` to a lambda whose `onClick` override loads our [String] field [mBook] with
     * the data corresponding to the currently selected item in `spin` and loads our [String] field
     * [mChapterAndVerse] with the text contents of `cavEditText`. If [mChapterAndVerse] contains
     * the string ":" we initialize our [String] variable `val citationChosen` to the string formed
     * by concatenating the book number in the `String[] numbers` field of `BibleAdapter` in position
     * `bookNum` to the string ":" followed by [mChapterAndVerse], otherwise we set it to the string
     * formed by concatenating the book number in the `String[] numbers` field of `BibleAdapter` in
     * position `bookNum` to the string ":" followed by [mChapterAndVerse] followed by the string
     * ":1". We initialize our [String] variable `val citationFallback` to the string formed by
     * concatenating the book number in the `String[] numbers` field of `BibleAdapter` in
     * position `bookNum` followed by the string ":1:1". We then initialize our [Int] variable
     * `val verseNumber` to the value returned by the `findFromCitation` method of `BibleMain` when
     * passed `citationChosen` and `citationFallback`. We then call the `moveToVerse` method
     * of `BibleAdapter` to move to verse `verseNumber`, dismiss this instance of `BibleChoose`,
     * set the `mLabel` field of the `bibleDialog` field of `BibleMain` to the `dialogTitle`
     * field of `BibleMain`, and the `mText` field of the `bibleDialog` field of `BibleMain`
     * to the `dialogText` field of `BibleMain`.
     *
     * After setting this `OnClickListener` for `button` we return `v` to the caller.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
     * @return The View v for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView called")
        val v = inflater.inflate(R.layout.bible_choose, container, false)
        var tv = v.findViewById<TextView>(R.id.label)
        tv.text = mLabel

        tv = v.findViewById(R.id.text)
        tv.text = mText

        val spin = v.findViewById<Spinner>(R.id.spinner)

        val spinnerArrayAdapter = ArrayAdapter(v.context,
                android.R.layout.simple_spinner_item,
                BibleAdapter.books)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = spinnerArrayAdapter
        spin.onItemSelectedListener = spinSelected
        spin.setSelection(BibleMain.indexFromCitation(mLabel!!))

        val cavEditText = v.findViewById<EditText>(R.id.chapter_and_verse)

        // Watch for button clicks.
        val button = v.findViewById<Button>(R.id.show)
        button.setOnClickListener { view ->
            /**
             * Called when the button with id R.id.show ("GO") is clicked. First we load our [String]
             * field [mBook] with the data corresponding to the currently selected item in `spin` and
             * our [String] field [mChapterAndVerse] with the text contents of `cavEditText`. We
             * initialize our [Int] variable `val bookNum` to the item position of the currently
             * selected item in `spin`. If [mChapterAndVerse] contains the string ":" we initialize
             * our [String] variable `val citationChosen` to the string formed by concatenating the
             * book number in the `String[] numbers` field of `BibleAdapter` in position `bookNum`
             * to the string ":" followed by [mChapterAndVerse], otherwise we set it to the string
             * formed by concatenating the book number in the `String[] numbers` field of `BibleAdapter`
             * in position `bookNum` to the string ":" followed by [mChapterAndVerse] followed by the
             * string ":1". We initialize our [String] variable `val citationFallback` to the string
             * formed by concatenating the book number in the `String[] numbers` field of `BibleAdapter`
             * in position `bookNum` followed by the string ":1:1". We then initialize our [Int]
             * variable `val verseNumber` to the value returned by the `findFromCitation` method of
             * `BibleMain` when passed `citationChosen` and `citationFallback`. We then call the
             * `moveToVerse` method of `BibleAdapter` to have it move to verse `verseNumber`, dismiss
             * this instance of `BibleChoose`, set the `mLabel` field of the `bibleDialog` field of
             * `BibleMain` to the `dialogTitle` field of `BibleMain`, and the `mText` field of the
             * `bibleDialog` field of `BibleMain` to the `dialogText` field of `BibleMain`.
             *
             * @param view `View` that was clicked.
             */
            // When button is clicked, call up to owning activity.
            mBook = spin.selectedItem as String
            mChapterAndVerse = cavEditText.text.toString()
            Log.i(TAG, "$mBook $mChapterAndVerse")
            val bookNum = spin.selectedItemPosition
            val citationChosen = if (mChapterAndVerse.contains(":"))
                BibleAdapter.numbers[bookNum] + ":" + mChapterAndVerse
            else
                BibleAdapter.numbers[bookNum] + ":" + mChapterAndVerse + ":1"
            val citationFallback = BibleAdapter.numbers[bookNum] + ":1:1"
            Log.i(TAG, "$citationChosen or $citationFallback")
            val verseNumber = BibleMain.findFromCitation(citationChosen, citationFallback)
            BibleAdapter.moveToVerse(view, verseNumber)
            this@BibleChoose.dismiss()

            BibleMain.bibleDialog!!.mLabel = BibleMain.dialogTitle
            BibleMain.bibleDialog!!.mText = BibleMain.dialogText
        }

        return v
    }

    companion object {
        /**
         * TAG used for logging
         */
        const val TAG = "BibleChoose"

        /**
         * Create a new [BibleChoose] instance. First we initialize our [BibleChoose] variable
         * `val f` with a new instance, and initialize our [Bundle] variable `val args` with a
         * a new instance. We add our [String] parameter [label] to `args` using the key "label",
         * add our [String] parameter [text] to `args` using the key "text", and then we set the
         * arguments of `f` to be the [Bundle] `args`. Finally we return `f` to the caller.
         *
         * @param label Canonical Bible citation of current verse
         * @param text  Verse text
         * @return [BibleChoose] instance with its arguments set to a bundle that contains our
         * parameters `label` and `text`
         */
        fun newInstance(label: String, text: String): BibleChoose {
            Log.i(TAG, " newInstance called with: $label $text")
            val f = BibleChoose()
            val args = Bundle()
            args.putString("label", label)
            args.putString("text", text)

            f.arguments = args
            return f
        }
    }

}
