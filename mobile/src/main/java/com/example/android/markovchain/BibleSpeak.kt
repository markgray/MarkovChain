package com.example.android.markovchain

import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener

import java.util.Locale

/**
 * This `DialogFragment` reads the current verse using the systems text to speech synthesizer.
 */
@Suppress("MemberVisibilityCanBePrivate")
class BibleSpeak : DialogFragment(), OnInitListener {
    /**
     * canonical Bible citation for current verse
     */
    var mLabel: String? = null
    /**
     * text of current verse
     */
    var mText: String? = null
    /**
     * `TextToSpeech` instance we will use to synthesize speech
     */
    var mTts: TextToSpeech? = null
    /**
     * `View` containing our layout, inflated in `onCreateView`
     */
    var mView: View? = null

    /**
     * Called to have the fragment instantiate its user interface view. First we initialize our [View]
     * field [mView] by using our [LayoutInflater] parameter [inflater] to inflate our layout file
     * R.layout.bible_speak, using our [ViewGroup] parameter [container] for the layout params
     * without attaching to it. We then call our method [setDisplayedText] to set the label and
     * text of [mView] to our [String] fields [mLabel] and [mText] respectively. Next we initialize
     * our [Button] variable `var button` by finding the view in [mView] with id R.id.dismiss
     * ("DISMISS") and set its `OnClickListener` to a lambda whose `onClick` override will dismiss
     * this [BibleSpeak] `DialogFragment`, and then we set `button` to the view in [mView] with id
     * R.id.next ("NEXT") and set its `OnClickListener` to a lambda whose `onClick` override will
     * move to the next verse of the Bible and speak it out loud. Finally we return [mView] to
     * the caller.
     *
     * @param inflater  A LayoutInflater object that can be used to inflate an XML layout file
     * @param container If non-null, this is the parent `ViewGroup` that the fragment's UI will
     * be attached to.
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     * @return Return the View for the fragment's UI.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView called")
        mView = inflater.inflate(R.layout.bible_speak, container, false)
        setDisplayedText(mView!!)

        // Watch for dismiss button clicks
        var button = mView!!.findViewById<Button>(R.id.dismiss)
        button.setOnClickListener {
            // When button is clicked, dismiss this DialogFragment

            BibleMain.bibleDialog!!.mLabel = BibleMain.dialogTitle
            BibleMain.bibleDialog!!.mText = BibleMain.dialogText
            this@BibleSpeak.dismiss()
        }
        // Watch for NEXT  button clicks
        button = mView!!.findViewById(R.id.next)
        button.setOnClickListener { v ->
            // When button is clicked, move to the next verse and speak it
            BibleAdapter.moveToVerse(v, BibleMain.dialogVerse + 1)
            mLabel = BibleMain.dialogTitle
            mText = BibleMain.dialogText
            setDisplayedText(mView!!)

            val dummyBundle: Bundle? = null
            mTts!!.speak(mText, TextToSpeech.QUEUE_ADD, dummyBundle,null)
        }

        return mView
    }

    /**
     * Set the label and text of the [View] parameter [v] passed us to the strings in our fields
     * [mLabel] and [mText] respectively. First we initialize our [TextView] variable `var tv` by
     * finding the view in our [View] parameter [v] with id R.id.label and set its text to our
     * [String] field [mLabel], then we set `tv` to the view found in `v` with id R.id.text and set
     * its text to our [String] field [mText].
     *
     * @param v Main View for the DialogFragment (always mView at the moment)
     */
    fun setDisplayedText(v: View) {
        var tv = v.findViewById<TextView>(R.id.label)
        tv.text = mLabel

        tv = v.findViewById(R.id.text)
        tv.text = mText
    }

    /**
     * Called to do initial creation of a fragment. First we call through to our super's implementation
     * of `onCreate`, then we initialize our [String] field [mLabel] to the value stored in our argument
     * bundle under the key "label", and our [String] field [mText] to the value stored under the key
     * "text" (the canonical Bible citation for the current verse, and the text of the current verse
     * respectively). Then we set the style of our [DialogFragment] to STYLE_NORMAL. Finally if the
     * `textToSpeech` field of `BibleMain` is null (first time [BibleSpeak] has been launched) we
     * create a new instance of [TextToSpeech] that uses *this* as its `OnInitListener` (our `onInit`
     * override will be called when the engine is ready for use) and save it in our [TextToSpeech]
     * field [mTts] as well as a permanent copy in the `textToSpeech` field of `BibleMain`, otherwise
     * we initialize our [TextToSpeech] field [mTts] from the existing [TextToSpeech] instance in the
     * `textToSpeech` field of `BibleMain` and immediately instruct `mTts` to speak our [String] field
     * [mText] (since it is preexisting, the [TextToSpeech] instance has already finished its
     * initialization phase and is ready to use, for the first use of a newly created [TextToSpeech]
     * instance we delay instructing it to speak until our `onInit` callback is called to signal the
     * completion of the [TextToSpeech] engine initialization).
     *
     * @param savedInstanceState We do not override `onSaveInstanceState` so do not use.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mLabel = arguments!!.getString("label")
        mText = arguments!!.getString("text")
        Log.i(TAG, "onCreate called with: $mLabel $mText")

        setStyle(STYLE_NORMAL, 0)

        if (BibleMain.textToSpeech == null) {
            mTts = TextToSpeech(BibleMain.bibleContext, this)
            BibleMain.textToSpeech = mTts
        } else {
            mTts = BibleMain.textToSpeech

            val dummyBundle: Bundle? = null
            mTts!!.speak(mText, TextToSpeech.QUEUE_ADD, dummyBundle, null)
        }

    }

    /**
     * Called to signal the completion of the [TextToSpeech] engine initialization. If the status
     * passed us is SUCCESS, we try to set the language of our [TextToSpeech] field [mTts] to Locale.US
     * (Locale constant for en_US) and if the result of that attempt is either LANG_MISSING_DATA or
     * LANG_NOT_SUPPORTED we just log the error and return, otherwise we instruct [mTts] to speak
     * our [String] field [mText]. If the status passed to us was not SUCCESS we log our failure
     * to initialize the [TextToSpeech] engine.
     *
     * @param status [TextToSpeech.SUCCESS] or [TextToSpeech.ERROR].
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = mTts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "TTS language is not available.")
            } else {

                val dummyBundle: Bundle? = null
                mTts!!.speak(mText, TextToSpeech.QUEUE_ADD, dummyBundle,null)
            }
        } else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TTS.")
        }

    }

    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "BibleSpeak"

        /**
         * Create a new instance of [BibleSpeak] with its arguments set to the values of the parameters
         * passed us. First we create a new instance to initialize our [BibleSpeak] variable `val f`.
         * Then we initialize our [Bundle] variable `val args` with a new instance, store our [String]
         * parameter [label] in it under the key "label", and our [String] parameter [text] under the
         * key "text". Finally we set the argument bundle of `f` to `args` and return `f` to the caller.
         *
         * @param label canonical Bible citation for current verse
         * @param text  text of current verse
         * @return an instance of BibleSpeak with arguments set to the values of our parameters
         */
        internal fun newInstance(label: String, text: String): BibleSpeak {
            Log.i(TAG, " newInstance called with: $label $text")
            val f = BibleSpeak()

            val args = Bundle()
            args.putString("label", label)
            args.putString("text", text)

            f.arguments = args
            return f
        }
    }
}
