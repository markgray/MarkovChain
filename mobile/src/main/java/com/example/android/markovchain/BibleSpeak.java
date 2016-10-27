package com.example.android.markovchain;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.Locale;

/**
 * This DialogFragment reads the current verse using the systems text to speech synthesizer.
 */
public class BibleSpeak extends DialogFragment implements OnInitListener {

    private static final String TAG = "BibleSpeak"; // TAG used for logging
    public String mLabel; // canonical Bible citation for current verse
    public String mText;  // text of current verse
    private TextToSpeech mTts; // TextToSpeech instance we will use to synthesize speech
    private View mView; // View containing our layout inflated in onCreateView

    /**
     * Create a new instance of BibleSpeak f with its arguments set to the values of the parameters
     * passed us. First we create a new instance of BibleSpeak f, then we create Bundle args and
     * insert the label parameter under key "label" and the text parameter under key "text".
     * We use Bundle f to supply the construction arguments for the BibleSpeak DialogFragment by
     * calling f.setArguments(args). The arguments in <code>args</code> can then be retrieved in
     * BibleSpeak.onCreate(). The arguments supplied here will be retained across fragment destroy
     * and creation.
     *
     * @param label canonical Bible citation for current verse
     * @param text  text of current verse
     * @return an instance of BibleSpeak with arguments set to the values of our parameters
     */
    static BibleSpeak newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleSpeak f = new BibleSpeak();

        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.</p>
     *
     * First we inflate our layout into our field View mView, then we call our method
     * setDisplayedText to set the label of our View mView to the String mLabel and the
     * text of our View mView to the String mText. Next we locate the Button "DISMISS"
     * (R.id.dismiss) and set its OnClickListener to an anonymous class which will dismiss
     * this BibleSpeak DialogFragment, and then we locate the Button "NEXT" (R.id.next) and
     * set its OnClickListener to an anonymous class which will move to the next verse of the
     * Bible and speak it out loud. Finally we return View mView to the caller.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *        any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     *        UI should be attached to.  The fragment should not add the view itself,
     *        but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     *
     * @return Return the View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        mView = inflater.inflate(R.layout.bible_speak, container, false);
        setDisplayedText(mView);

        // Watch for dismiss button clicks
        Button button = (Button) mView.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, dismiss this DialogFragment
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
                BibleSpeak.this.dismiss();
            }
        });
        // Watch for NEXT  button clicks
        button = (Button) mView.findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, move to the next verse and speak it
                BibleAdapter.moveToVerse(v, BibleMain.dialogVerse+1);
                mLabel = BibleMain.dialogTitle;
                mText =  BibleMain.dialogText;
                setDisplayedText(mView);
                //noinspection deprecation
                mTts.speak(mText, TextToSpeech.QUEUE_ADD, null);
            }
        });

        return mView;
    }

    /**
     * Set the label and text of the view to those saved in mLabel and mText. First we locate the
     * View tv for the label (R.id.label) and set its text to the String mLabel, then we locate the
     * View for the text (R.id.text) and set its text to the String mText.
     *
     * @param v Main View for the DialogFragment (always mView at the moment)
     */
    public void setDisplayedText(View v) {
        View tv = v.findViewById(R.id.label);
        ((TextView) tv).setText(mLabel);

        tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(mText);
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * onAttach(Activity) and before onCreateView(LayoutInflater, ViewGroup, Bundle).
     *
     * First we call through to our super's implementation of onCreate, then we initialize our field
     * String mLabel using what was stored in our arguments under the key "label", and our field
     * String mText using what was stored in our arguments under the key "text" (the canonical Bible
     * citation for the current verse, and the text of the current verse respectively). Then we set
     * the style of our DialogFragment to STYLE_NORMAL (for no reason I can think of.) Finally if
     * BibleMain.textToSpeech is null (first time BibleSpeak has been called) we create a new
     * instance of TextToSpeech and save it in our field TextToSpeech mTts as well as a permanent
     * copy in BibleMain.textToSpeech, otherwise we initialize our field TextToSpeech mTts from the
     * existing TextToSpeech instance in BibleMain.textToSpeech and immediately instruct mTts to
     * speak our String mText (since it is preexisting, the TextToSpeech instance has already
     * finished its initialization phase and is ready to use, for the first use of a newly created
     * TextToSpeech instance we delay instructing it to speak until the onInit callback is called
     * to signal the completion of the TextToSpeech engine initialization).
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);

        if (BibleMain.textToSpeech == null) {
            mTts = new TextToSpeech(BibleMain.bibleContext, this);
            BibleMain.textToSpeech = mTts;
        } else {
            mTts = BibleMain.textToSpeech;
            //noinspection deprecation
            mTts.speak(mText, TextToSpeech.QUEUE_ADD, null);
        }

    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization. If the status
     * passed us is TextToSpeech.SUCCESS, we try to set the language of TextToSpeech mTts to Locale.US
     * (Locale constant for en_US) and if the result of that attempt is either LANG_MISSING_DATA or
     * LANG_NOT_SUPPORTED we just log the error and return, otherwise we instruct TextToSpeech mTts
     * to speak our String mText. If the status passed to us was not TextToSpeech.SUCCESS we log
     * our failure to initialize the TextToSpeech engine.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "TTS language is not available.");
            } else {
                //noinspection deprecation
                mTts.speak(mText, TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            // Initialization failed.
            Log.e(TAG, "Could not initialize TTS.");
        }

    }
}
