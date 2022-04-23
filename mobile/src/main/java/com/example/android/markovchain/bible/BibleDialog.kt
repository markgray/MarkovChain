package com.example.android.markovchain.bible

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.android.markovchain.R

class BibleDialog : DialogFragment() {
    /**
     * label for the `DialogFragment` (we use the canonical Bible citation for the verse)
     */
    var mLabel: String? = null
    /**
     * text of the current verse
     */
    var mText: String? = null
    /**
     * Button to use to repeat last operation selected in the Spinner
     */
    lateinit var repeatButton: Button

    /**
     * `TextView` in our inflated layout used for the Bible citation label
     */
    private lateinit var lastLabelView: TextView
    /**
     * `TextView` in our inflated layout used for the text of the current verse
     */
    private lateinit var lastTextView: TextView

    /**
     * Objects used in the ArrayAdapter used for our Spinner
     */
    var spinChoices = arrayOf("Choose Action", "Random verse", "Google",
            "Bookmark", "Go to verse", "Read aloud")
    /**
     * Which spinChoices action has been chosen
     */
    var spinChosen = ""
    /**
     * The index of the action chosen in the Spinner
     */
    var spinIndex = 0
    /**
     * Used to determine if Spinner has chosen a new action or
     * whether the "REPEAT" Button should be visible
     */
    var lastIndex = 0

    /**
     * `AdapterView.OnItemSelectedListener` used for the function chooser `Spinner`
     */
    private var spinSelected: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        /**
         * Callback method to be invoked when an item in this view has been selected.
         * This callback is invoked only when the newly selected position is different
         * from the previously selected position or if there was no selected item.
         *
         * Implementers can call `getItemAtPosition(position)` if they need to access the data
         * associated with the selected item.
         *
         * First we save the position selected (the parameter [position]) and the string associated
         * with that position that we find in our array [spinChoices] in the fields [spinIndex] and
         * [spinChosen] respectively. Then if it is not the same as the as the [lastIndex]
         * chosen we call the `handleAction` method of our parent `BibleMain` activity to
         * perform whatever action is required when that [spinIndex] is chosen, and on return we
         * save this position as the new [lastIndex]. If the index was not 0 (our "Choose Action"
         * prompt) we set the visibility of our "REPEAT" `Button repeatButton` to VISIBLE (It
         * starts out with a visibility of GONE.)
         *
         * @param parent The AdapterView where the selection happened
         * @param view The view within the AdapterView that was clicked
         * @param position The position of the view in the adapter
         * @param id The row id of the item that is selected
         */
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            spinIndex = position
            spinChosen = spinChoices[position]

            if (spinIndex != lastIndex) {

                (activity as BibleActivity).handleAction(view!!, spinIndex)
                lastIndex = spinIndex
            }
            if (spinIndex != 0) {
                repeatButton.visibility = View.VISIBLE
            }
        }

        /**
         * Callback method to be invoked when the selection disappears from this view.
         * The selection can disappear for instance when touch is activated or when
         * the adapter becomes empty. We do nothing.
         *
         * @param parent The `AdapterView` that now contains no selected item.
         */
        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    /**
     * Update the text displayed in the [BibleDialog] for a new verse. This is necessary only after
     * moving to a random verse at the moment because the same `BibleDialog DialogFragment` is
     * used. Other actions create `DialogFragments` which when dismissed need to have the [BibleDialog]
     * recreated so `onCreateView` is called, therefore setting the fields `BibleMain.bibleDialog.mLabel`
     * and `BibleMain.bibleDialog.mText` is done instead. We set the text of the [lastLabelView]
     * `TextView` to the [label] argument, and the text of the [lastTextView] `TextView`  to the
     * [text] argument. [lastLabelView] and [lastTextView] are set in the [onCreateView] callback.
     *
     * @param label Canonical Bible citation for the verse
     * @param text Text of the verse
     */
    fun refresh(label: String, text: String) {
        lastLabelView.text = label
        lastTextView.text = text
    }

    /**
     * Called to do initial creation of a `DialogFragment`. This is called after `onAttach(Activity)`
     * and before `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     *
     * First we call our super's implementation of `onCreate`, then we fetch from our arguments
     * the [String] stored under the index "label" and save it in our field [mLabel], and we fetch
     * the [String] stored under the index "text" and save it it our field [mText]. Finally we set
     * the style of our `BibleDialog` `DialogFragment` to STYLE_NORMAL.
     *
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabel = requireArguments().getString("label")
        mText = requireArguments().getString("text")

        setStyle(STYLE_NORMAL, 0)
    }

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * `onCreate(Bundle)` and `onActivityCreated(Bundle)`.
     *
     * First we initialize our [View] variable `val v` by using our [LayoutInflater] parameter
     * [inflater] to inflate our layout file R.layout.bible_dialog using our [ViewGroup] parameter
     * [container] for the layout params without attaching to it. We initialize our [TextView]
     * `val tv` by finding the view in `v` with id R.id.label, set our field [lastLabelView] to
     * `tv` and set the text of `tv` to our field [mLabel]. Next we set `tv` by finding the view
     * in `v` with id R.id.text, set our field [lastTextView] to `tv` and set the text of `tv`
     * to our field [mText].
     *
     * Next we initialize our [Spinner] variable `val spin` by finding the view in `v` with id
     * R.id.spinner, and initialize our variable `ArrayAdapter<String> spinnerArrayAdapter` with
     * a new instance which uses the `Context` of `v`, uses the system layout file
     * android.R.layout.simple_spinner_item to instantiate views and uses our `String[]` field
     * [spinChoices] as the data set it will represent.
     *
     * We set android.R.layout.simple_spinner_dropdown_item to be the layout file that
     * `spinnerArrayAdapter` uses for the drop down views, then set the adapter of `spin` to
     * `spinnerArrayAdapter`, and set the `OnItemSelectedListener` of `spin` to be our field
     * [spinSelected].
     *
     * We next initialize our [Button] field [repeatButton] by finding the view in `v` with
     * id R.id.repeat ("REPEAT") and sets its `OnClickListener` to a lambda which will cause
     * the last action performed to be repeated. We initialize our [Button] variable `val button`
     * by finding the view with id R.id.dismiss ("DISMISS") and set its `OnClickListener` to a
     * lambda which will `dismiss` our `BibleDialog`. Finally we return `v` to the caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use
     * @return the [View] for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.bible_dialog, container, false)
        var tv = v.findViewById<TextView>(R.id.label)
        lastLabelView = tv
        tv.text = mLabel

        tv = v.findViewById(R.id.text)
        lastTextView = tv
        tv.text = mText

        val spin = v.findViewById<Spinner>(R.id.spinner)

        val spinnerArrayAdapter = ArrayAdapter(v.context,
                android.R.layout.simple_spinner_item,
                spinChoices)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = spinnerArrayAdapter
        spin.onItemSelectedListener = spinSelected

        // Watch for button clicks.
        repeatButton = v.findViewById(R.id.repeat)
        repeatButton.setOnClickListener { view ->
            /**
             * Repeat the last action processed when clicked. We call the `handleAction` method
             * of our `BibleMain` parent activity to handle a repeat of the `spinIndex`
             * action.
             *
             * @param view REPEAT Button which has been clicked
             */
            (activity as BibleActivity).handleAction(view, spinIndex)
        }

        val button = v.findViewById<Button>(R.id.dismiss)
        /*
         * Dismiss this BibleDialog. It does this by fetching the activity our `DialogFragment`
         * is associated with (`BibleMain`) and using this to access the convenience function
         * `dismissDialog` which will dismiss us.
         *
         * Parameter: DISMISS Button which has been clicked
         */

        button.setOnClickListener {
            (activity as BibleActivity).dismissDialog()
        }

        return v
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally tied to `Activity.onResume` of the containing
     * `Activity`'s lifecycle. I just override for educational purposes.
     */
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "I have been resumed")
    }

    /**
     * Our static constants and static methods.
     */
    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "BibleDialog"

        /**
         * Create and initialize a `BibleDialog` `DialogFragment`. First we initialize our
         * [BibleDialog] variable `val f` with a new instance and our [Bundle] variable `val args`
         * with a new instance. We store our [String] parameter [label] in `args` under the key
         * "label", and our [String] parameter [text] under the key "text" then set the argument
         * `Bundle` of `f` to `args`. Finally we return `f` to the caller.
         *
         * @param label Label to use
         * @param text  Text to use
         * @return Initialized BibleDialog
         */
        fun newInstance(label: String, text: String): BibleDialog {
            val f = BibleDialog()

            val args = Bundle()
            args.putString("label", label)
            args.putString("text", text)

            f.arguments = args

            return f
        }

        // Index values for items chosen in the Spinner, must match entries in String[] spinChoices

        /**
         * "Choose Action"
         */
        const val CHOICE_NONE = 0
        /**
         * "Random verse"
         */
        const val CHOICE_RANDOM_VERSE = 1
        /**
         * "Google"
         */
        const val CHOICE_GOOGLE = 2
        /**
         * "Bookmark"
         */
        const val CHOICE_BOOKMARK = 3
        /**
         * "Go to verse"
         */
        const val CHOICE_GO_TO_VERSE = 4
        /**
         * "Read aloud"
         */
        const val CHOICE_READ_ALOUD = 5
    }
}
