package com.example.android.markovchain.util

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.android.markovchain.R

/**
 * The [DialogFragment] that is launched when the user wishes to have [TextToSpeech] read a verse.
 */
class SpeechDialog : DialogFragment(), TextToSpeech.OnInitListener {
    /**
     * text of current verse
     */
    private var mText: String? = null

    /**
     * [TextToSpeech] instance we will use to synthesize speech
     */
    private var mTts: TextToSpeech? = null

    /**
     * [View] containing our layout, inflated in [onCreateView]
     */
    private lateinit var mView: View

    /**
     * Called to have the fragment instantiate its user interface view. First we initialize our [View]
     * field [mView] by using our [LayoutInflater] parameter [inflater] to inflate our layout file
     * R.layout.speech_layout, using our [ViewGroup] parameter [container] for the layout params
     * without attaching to it. Then we find the [TextView] in [mView] with the id R.id.speech_text
     * and set its `text` to our field [mText], and find the [Button] in [mView] with the id
     * R.id.speech_dismiss and set its `OnClickListener` to a lambda which calls our [dismissMe]
     * method to `stop` and `shutdown` our [TextToSpeech] instance in our field [mTts] then dismiss
     * *this* [SpeechDialog] instance. Having configured [mView] we return it to our caller.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the [View] for the fragment's UI, or null.
     * TODO: Move findViewById calls to onViewCreated? No advantage as I see it.
     */
    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.speech_layout, container, false)
        mView.findViewById<TextView>(R.id.speech_text).text = mText
        mView.findViewById<Button>(R.id.speech_dismiss).setOnClickListener { dismissMe() }

        return mView
    }

    /**
     * Called to do initial creation of a fragment. First we call through to our super's implementation
     * of `onCreate`, then we initialize our [String] field [mText] to the value stored in our argument
     * [Bundle] under the key "text". Finally we initialize our [TextToSpeech] field [mTts] with a
     * new instance that uses *this* as its `OnInitListener` (our [onInit] override will be called
     * when the engine is ready for use)
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mText = requireArguments().getString("text")
        mTts = TextToSpeech(activity, this)
    }

    /**
     * Called to signal the completion of the [TextToSpeech] engine initialization. If the [status]
     * passed us is SUCCESS, we initialize our [Bundle] variable `dummyBundle` to *null* to use to
     * help the compiler identify which `speak` overload we are calling, then call the `speak`
     * method of our [TextToSpeech] field [mTts] to speak the text contained in our [String] field
     * [mText] using the [TextToSpeech.QUEUE_ADD] queue mode (the new entry is added at the end of
     * the playback queue), passing `dummyBundle` so as to not use the deprecated method, and with
     * a *null* as the utterance ID. If [status] is not [TextToSpeech.SUCCESS] we just log the error.
     *
     * @param status [TextToSpeech.SUCCESS] or [TextToSpeech.ERROR].
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.i(TAG, "TextToSpeech successfully initialized")
            val dummyBundle: Bundle? = null
            (mTts ?: return).speak(mText, TextToSpeech.QUEUE_ADD, dummyBundle, null)
        } else {
            Log.e(TAG, "Could not initialize TTS.")
        }
    }

    /**
     * Method called when the user dismisses this [SpeechDialog] by clicking the "Dismiss" [Button].
     * If our [TextToSpeech] field [mTts] is not *null* we call its `stop` method to have it interupt
     * the current utterance and discard other utterances in the queue, and call its `shutdown` method
     * to have it release the resources used by the [TextToSpeech] engine. Then we call our [dismiss]
     * method to dismiss our fragment and its dialog.
     */
    private fun dismissMe() {
        mTts?.stop()
        mTts?.shutdown()
        mTts = null
        this.dismiss()
    }

    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "SpeechDialog"

        /**
         * Creates a new instance of [SpeechDialog] with its arguments set to the values of the
         * parameter passed us. First we create a new instance to initialize our [SpeechDialog]
         * variable `val f`. Then we initialize our [Bundle] variable `val args` with a new instance,
         * store our [String] parameter [text] under the key "text". Finally we set the argument
         * bundle of `f` to `args` and return `f` to the caller.
         *
         * @param text  text of current verse
         * @return an instance of [SpeechDialog] with arguments set to the value of our parameter
         */
        internal fun newInstance(text: String): SpeechDialog {
            Log.i(TAG, "newInstance called with: $text")
            val f = SpeechDialog()
            val args = Bundle()
            args.putString("text", text)
            f.arguments = args
            return f
        }

    }
}