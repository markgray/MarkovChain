package com.example.android.markovchain

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * This `DialogFragment` will (eventually) save a bookmark to a particular verse with the date
 * it was bookmarked and an optional comment to an SQL database.
 */
@Suppress("MemberVisibilityCanBePrivate")
class BibleBookmark : DialogFragment() {
    /**
     * Canonical Bible citation for current verse
     */
    var mLabel: String? = null
    /**
     * Text of the current verse
     */
    var mText: String? = null

    /**
     * Called to do initial creation of a `DialogFragment`. This is called after `onAttach(Activity)`
     * and before `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     *
     *
     * Note that this can be called while the fragment's activity is still in the process of being
     * created. As such, you can not rely on things like the activity's content view hierarchy being
     * initialized at this point.  If you want to do work once the activity itself is created, see
     * [.onActivityCreated].
     *
     *
     * First we call through to our super's implementation of `onCreate`, then we initialize our
     * field `String mLabel` set to the `String` stored under the key "label" in our argument
     * `Bundle`, and `String mText` set to the `String` stored under the key "text"
     * in that `Bundle`. Finally we set the style of our `DialogFragment` to be STYLE_NORMAL
     * (for a reason which escapes me right now).
     *
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mLabel = arguments!!.getString("label")
        mText = arguments!!.getString("text")
        Log.i(TAG, "onCreate called with: $mLabel $mText")

        setStyle(STYLE_NORMAL, 0)
    }

    /**
     * Called to have the fragment instantiate its user interface view. This will be called between
     * [.onCreate] and [.onActivityCreated].
     *
     *
     * If you return a View from here, you will later be called in [.onDestroyView] when the
     * view is being released.
     *
     *
     * First we initialize `View v` by using our parameter `LayoutInflater inflater` to
     * inflate our layout file R.layout.bible_bookmark using our parameter `ViewGroup container`
     * for layout params without attaching to is, then we initialize `View tv` by finding the
     * view in `v` with id R.id.label and set its text to our field `String mLabel`, and
     * then we set `tv` to the view with id R.id.text and set its text to our field `String mText`.
     * Finally we locate the "DISMISS" Button (view with id R.id.dismiss) and set its `OnClickListener`
     * to an anonymous class which will dismiss this `BibleBookmark DialogFragment` when clicked,
     * while updating the label and text used by the `BibleDialog` which launched us and to which
     * we will be returning to.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * The fragment should not add the view itself, but this can be used to generate
     * the `LayoutParams` of the view.
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView called")
        val v = inflater.inflate(R.layout.bible_bookmark, container, false)
        var tv = v.findViewById<TextView>(R.id.label)
        tv.text = mLabel
        tv = v.findViewById(R.id.text)
        tv.text = mText

        /*
         * Dismiss this `DialogFragment`, and make sure that the `BibleDialog bibleDialog`
         * which we are returning to uses the correct `dialogTitle` for its `mLabel`
         * field and correct `dialogText` for its `mText` field (in case we changed the
         * verse being shown).
         *
         * Parameter: Button which was clicked
         */
        v.findViewById<View>(R.id.dismiss).setOnClickListener {
            this@BibleBookmark.dismiss()

            BibleMain.bibleDialog!!.mLabel = BibleMain.dialogTitle
            BibleMain.bibleDialog!!.mText = BibleMain.dialogText
        }

        return v
    }

    companion object {
        /**
         * TAG for logging
         */
        const val TAG = "BibleBookmark"

        /**
         * Create a new `BibleBookmark DialogFragment`. We initialize `BibleBookmark f`, with
         * a new instance, then initialize `Bundle args` with a new instance, add our parameter
         * `String label` to `args` using the index "label", add our parameter `String text`
         * to `args` using the index "text", and then set the arguments for `f` to `args`.
         * Finally we return `f` to our caller.
         *
         * @param label Canonical Bible citation for the verse being bookmarked
         * @param text  Text of the verse being bookmarked
         * @return A `BibleBookmark` instance with the arguments bundle containing the parameters
         * `label` and `text` passed to us.
         */
        fun newInstance(label: String, text: String): BibleBookmark {
            Log.i(TAG, " newInstance called with: $label $text")
            val f = BibleBookmark()
            val args = Bundle()
            args.putString("label", label)
            args.putString("text", text)

            f.arguments = args
            return f
        }
    }
}
