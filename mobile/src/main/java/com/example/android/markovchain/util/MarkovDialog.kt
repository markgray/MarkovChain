package com.example.android.markovchain.util

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.android.markovchain.R

/**
 * `DialogFragment` to display number of possibilities given the first two words, and the verse
 * we randomly generated using `Markov.line`. No longer used since we switched to speaking the
 * verse when long clicked.
 */
@Suppress("unused")
class MarkovDialog : DialogFragment() {
    /**
     * Possibility statistics for the current verse
     */
    private var mPossibles: String? = null
    /**
     * Text of the current verse
     */
    private var mVerse: String? = null

    /**
     * Called to do initial creation of a `DialogFragment`. First we call our super's implementation
     * of `onCreate`. We then initialize our [String] field [mPossibles] with the string stored
     * in our argument [Bundle] under the key "possibles", and our [String] field [mVerse] with
     * the string stored in our argument [Bundle] under the key "verse". Finally we set our style
     * to STYLE_NORMAL without a custom theme.
     *
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPossibles = arguments!!.getString("possibles")
        mVerse = arguments!!.getString("verse")

        setStyle(STYLE_NORMAL, 0)
    }

    /**
     * Called to have the `DialogFragment` instantiate its user interface view. We initialize our
     * [View] variable `val v` by using our [LayoutInflater] parameter [inflater] to inflate our
     * layout file R.layout.markov_dialog, using our [ViewGroup] parameter [container] for layout
     * params without attaching to it. We initialize our [TextView] variable `val pv` by finding the
     * view in `v` with id R.id.possibles then set its text to our [String] field [mPossibles] and
     * initialize our [TextView] variable `val vv` by finding the view in `v` with id R.id.verse
     * and set its text to our [String] field [mVerse]. Next we initialize our [Button] variable
     * `val db` by finding the view in `v` with id R.id.dismiss and set its `OnClickListener` to a
     * lambda whose `onClick` override calls the `dismiss()` method to dismiss our fragment and its
     * dialog. Finally we return `v` to the caller.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     * The fragment should not add the view itself, but this can be used to generate
     * the LayoutParams of the view.
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.markov_dialog, container, false)
        val pv = v.findViewById<TextView>(R.id.possibles)
        pv.text = mPossibles
        val vv = v.findViewById<TextView>(R.id.verse)
        vv.text = mVerse

        val db = v.findViewById<Button>(R.id.dismiss)
        db.setOnClickListener { dismiss() }

        return v
    }

    /**
     * Our static method.
     */
    companion object {

        /**
         * Factory method for constructing and initializing a [MarkovDialog] instance. First we
         * initialize our [MarkovDialog] variable `val f` and our [Bundle] variable `val args` with
         * new instances. Then we store our [String] parameter [possibles] in `args` under the key
         * "possibles", and our [String] parameter [verse] under the key "verse". We set the arguments
         * of `f` to `args` and return `f` to the caller.
         *
         * @param possibles Possibility statistics for the current verse
         * @param verse     Text of the current verse
         * @return a [MarkovDialog] instance whose argument [Bundle] contains our parameters
         */
        fun newInstance(possibles: String, verse: String): MarkovDialog {
            val f = MarkovDialog()

            val args = Bundle()
            args.putString("possibles", possibles)
            args.putString("verse", verse)

            f.arguments = args

            return f
        }
    }
}
