package com.example.android.markovchain;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
 * This {@code DialogFragment} reads the current verse using the systems text to speech synthesizer.
 */
@SuppressWarnings("WeakerAccess")
public class BibleSpeak extends DialogFragment implements OnInitListener {
    /**
     * TAG used for logging
     */
    private static final String TAG = "BibleSpeak";
    /**
     * canonical Bible citation for current verse
     */
    public String mLabel;
    /**
     * text of current verse
     */
    public String mText;
    /**
     * {@code TextToSpeech} instance we will use to synthesize speech
     */
    private TextToSpeech mTts;
    /**
     * {@code View} containing our layout, inflated in {@code onCreateView}
     */
    private View mView;

    /**
     * Create a new instance of {@code BibleSpeak} with its arguments set to the values of the parameters
     * passed us. First we create a new instance to initialize our variable {@code BibleSpeak f}. Then
     * we initialize our variable {@code Bundle args} with a new instance, store our parameter {@code String label}
     * in it under the key "label", and our parameter {@code String text} under the key "text". Finally
     * we set the argument bundle of {@code f} to {@code args} and return {@code f} to the caller.
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
     * Called to have the fragment instantiate its user interface view. First we initialize our field
     * {@code View mView} by using our parameter {@code LayoutInflater inflater} to inflate our layout
     * file R.layout.bible_speak using our parameter {@code ViewGroup container} for the layout params
     * without attaching to it. We then call our method {@code setDisplayedText} to set the label and
     * text of {@code mView} to our fields {@code String mLabel} and {@code String mText} respectively.
     * Next we initialize our variable {@code Button button} by finding the view in {@code mView} with
     * id R.id.dismiss ("DISMISS") and set its {@code OnClickListener} to an anonymous class whose
     * {@code onClick} override will dismiss this {@code BibleSpeak} BibleSpeak{@code BibleSpeak},
     * and then we set {@code button} to the view in {@code mView} with id R.id.next ("NEXT") and
     * set its {@code OnClickListener} to an anonymous class whose {@code onClick} override will move
     * to the next verse of the Bible and speak it out loud. Finally we return {@code View mView} to
     * the caller.
     *
     * @param inflater  A LayoutInflater object that can be used to inflate an XML layout file
     * @param container If non-null, this is the parent {@code ViewGroup} that the fragment's UI will
     *                  be attached to.
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     * @return Return the View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        mView = inflater.inflate(R.layout.bible_speak, container, false);
        setDisplayedText(mView);

        // Watch for dismiss button clicks
        Button button = mView.findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, dismiss this DialogFragment
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
                BibleSpeak.this.dismiss();
            }
        });
        // Watch for NEXT  button clicks
        button = mView.findViewById(R.id.next);
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
     * Set the label and text of the {@code View v} passed us to those saved in the fields {@code mLabel}
     * and {@code mText} respectively. First we initialize our variable {@code TextView tv} by finding
     * the view in our parameter {@code View v} with id R.id.label and set its text to our field
     * {@code String mLabel}, then we set {@code tv} to the view found in {@code v} with id R.id.text
     * and set its text to our field {@code String mText}.
     *
     * @param v Main View for the DialogFragment (always mView at the moment)
     */
    public void setDisplayedText(View v) {
        TextView tv = v.findViewById(R.id.label);
        tv.setText(mLabel);

        tv = v.findViewById(R.id.text);
        tv.setText(mText);
    }

    /**
     * Called to do initial creation of a fragment. First we call through to our super's implementation
     * of {@code onCreate}, then we initialize our field {@code String mLabel} to the value stored in
     * our argument bundle under the key "label", and our field {@code String mText} to the value stored
     * under the key "text" (the canonical Bible citation for the current verse, and the text of the
     * current verse respectively). Then we set the style of our {@code DialogFragment} to STYLE_NORMAL
     * (for no reason I can think of). Finally if the {@code textToSpeech} field of {@code BibleMain}
     * is null (first time {@code BibleSpeak} has been launched) we create a new instance of
     * {@code TextToSpeech} that uses 'this' as its {@code OnInitListener} (our {@code onInit} override
     * will be called when the engine is ready for use) and save it in our field {@code TextToSpeech mTts}
     * as well as a permanent copy in the {@code textToSpeech} field of {@code BibleMain}, otherwise we
     * initialize our field {@code TextToSpeech mTts} from the existing {@code TextToSpeech} instance
     * in the {@code textToSpeech} field of {@code BibleMain} and immediately instruct {@code mTts} to
     * speak our {@code String mText} (since it is preexisting, the {@code TextToSpeech} instance has
     * already finished its initialization phase and is ready to use, for the first use of a newly created
     * {@code TextToSpeech} instance we delay instructing it to speak until our {@code onInit} callback
     * is called to signal the completion of the {@code TextToSpeech} engine initialization).
     *
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
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
     * Called to signal the completion of the {@code TextToSpeech} engine initialization. If the status
     * passed us is SUCCESS, we try to set the language of our field {@code TextToSpeech mTts} to Locale.US
     * (Locale constant for en_US) and if the result of that attempt is either LANG_MISSING_DATA or
     * LANG_NOT_SUPPORTED we just log the error and return, otherwise we instruct {@code mTts} to speak
     * our field {@code String mText}. If the status passed to us was not SUCCESS we log our failure
     * to initialize the {@code TextToSpeech} engine.
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
