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

public class BibleSpeak extends DialogFragment implements OnInitListener {

    private static final String TAG = "BibleSpeak";
    public String mLabel;
    public String mText;
    private TextToSpeech mTts;
    private View mView;

    static BibleSpeak newInstance(String label, String text) {
        Log.i(TAG, " newInstance called with: " + label + " " + text);
        BibleSpeak f = new BibleSpeak();

        Bundle args = new Bundle();
        args.putString("label", label);
        args.putString("text",text);

        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView called");
        mView = inflater.inflate(R.layout.bible_speak, container, false);
        setDisplayedText(mView);

        // Watch for button clicks.
        Button button = (Button) mView.findViewById(R.id.dissmiss);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, dismiss this DialogFragment
                BibleSpeak.this.dismiss();
                BibleMain.bibleDialog.mLabel = BibleMain.dialogTitle;
                BibleMain.bibleDialog.mText = BibleMain.dialogText;
            }
        });
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

    public void setDisplayedText(View v) {
        View tv = v.findViewById(R.id.label);
        String dialogLabel = mLabel;
        ((TextView) tv).setText(dialogLabel);

        tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(mText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLabel = getArguments().getString("label");
        mText = getArguments().getString("text");
        Log.i(TAG, "onCreate called with: " + mLabel + " " + mText);

        setStyle(DialogFragment.STYLE_NORMAL, 0);
        mTts = new TextToSpeech(BibleMain.bibleContext, this);

    }

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
