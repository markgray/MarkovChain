package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class WhatIsMan extends Activity {
    /**
     * {@code TextView} used to display our book chapters
     */
    TextView whatTextView;
    TextView whatWaiting;
    LinearLayout whatChapter;
    ScrollView whatChapterScrollView;

    public static final int[] resourceIDS = {
            R.raw.chapter1, R.raw.chapter2, R.raw.chapter3, R.raw.chapter4,
            R.raw.chapter5, R.raw.chapter6, R.raw.chapter7, R.raw.chapter8,
            R.raw.chapter9, R.raw.chapter10, R.raw.chapter11, R.raw.chapter12,
            R.raw.chapter13, R.raw.chapter14, R.raw.chapter15
    };

    public static final String[] titles = {
            "Chapter 1: What is Man?",
            "Chapter 2: The Death of Jean",
            "Chapter 3: The Turning-Point of My Life",
            "Chapter 4: How to Make History Dates Stick",
            "Chapter 5: The Memorable Assassination",
            "Chapter 6: A Scrap of Curious History",
            "Chapter 7: Switzerland, the Cradle of Liberty",
            "Chapter 8: At the Shrine of St. Wagner",
            "Chapter 9: William Dean Howells",
            "Chapter 10: English as she is Taught",
            "Chapter 11: A Simplified Alphabet",
            "Chapter 12: As Concerns Interpreting the Deity",
            "Chapter 13: Concerning Tobacco",
            "Chapter 14: The Bee",
            "Chapter 15: Taming the Bicycle"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_man);
        whatChapter = findViewById(R.id.what_chapter);
        whatChapterScrollView = findViewById(R.id.what_chapter_scrollView);
        whatTextView = findViewById(R.id.what_textView);
        whatWaiting = findViewById(R.id.what_waiting);
        for (int i = 0; i < resourceIDS.length; i++) {
            addButton(resourceIDS[i], titles[i], whatChapter);
        }
    }

    public void addButton(final int resourceID, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setText(description);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatChapterScrollView.setVisibility(View.GONE);
                loadResourceHtml(resourceID);
            }
        });
        parent.addView(button);
    }

    private void loadResourceHtml(int resourceID) {
        @SuppressLint("StaticFieldLeak")
        WhatDataTask mWhatDataTask = new WhatDataTask(getApplicationContext()) {
            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             *
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param s The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Spanned s) {
                whatTextView.setText(s);
                whatWaiting.setVisibility(View.GONE);
                whatTextView.setVisibility(View.VISIBLE);
            }
        };
        mWhatDataTask.execute(resourceID);
    }

}
