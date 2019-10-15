package com.example.android.markovchain

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

@Suppress("unused", "MemberVisibilityCanBePrivate")
class SpeechDialog : DialogFragment(), TextToSpeech.OnInitListener {
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
    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.speech_layout, container, false)
        mView.findViewById<TextView>(R.id.speech_text).text = mText
        mView.findViewById<Button>(R.id.speech_dismiss).setOnClickListener { dismissMe() }

        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mText = arguments!!.getString("text")
        mTts = TextToSpeech(activity, this)
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status [TextToSpeech.SUCCESS] or [TextToSpeech.ERROR].
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.i(TAG, "TextToSpeech successfully initialized")
            val dummyBundle: Bundle? = null
            mTts!!.speak(mText, TextToSpeech.QUEUE_ADD, dummyBundle, null)
        } else {
            Log.e(TAG, "Could not initialize TTS.")
        }
    }

    fun dismissMe() {
        mTts?.stop()
        mTts?.shutdown()
        this.dismiss()
    }

    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "SpeechDialog"

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