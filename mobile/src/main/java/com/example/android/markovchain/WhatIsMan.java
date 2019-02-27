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
    /**
     * {@code TextView} used to display "Waiting for data to load…" message while waiting
     */
    TextView whatWaiting;
    /**
     * {@code LinearLayout} that we add our chapter selection {@code Button}s to.
     */
    LinearLayout whatChapter;
    /**
     * {@code ScrollView} that holds the {@code LinearLayout whatChapter}
     */
    ScrollView whatChapterScrollView;

    /**
     * List of the resource ids for the chapters in "What Is Man"
     */
    public static final int[] resourceIDS = {
            R.raw.chapter1, R.raw.chapter2, R.raw.chapter3, R.raw.chapter4,
            R.raw.chapter5, R.raw.chapter6, R.raw.chapter7, R.raw.chapter8,
            R.raw.chapter9, R.raw.chapter10, R.raw.chapter11, R.raw.chapter12,
            R.raw.chapter13, R.raw.chapter14, R.raw.chapter15
    };

    /**
     * List of the titles for the chapters in "What Is Man" (used to label the selection buttons)
     */
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

    /**
     * Called when the activity is starting. First we call our super's implementation of {@code onCreate},
     * then we set our content view to our layout file R.layout.activity_what_is_man. We initialize our
     * field {@code LinearLayout whatChapter} by finding the view with id R.id.what_chapter (the chapter
     * selection buttons are placed here), initialize our field {@code ScrollView whatChapterScrollView}
     * by finding the view with id R.id.what_chapter_scrollView (holds the {@code LinearLayout whatChapter}
     * that holds our chapter selection buttons), initialize our field {@code TextView whatTextView} by
     * finding the view with id R.id.what_textView (the selected chapter will be displayed here), and
     * initialize our field {@code TextView whatWaiting} by finding the view with id R.id.what_waiting
     * (this will be displayed while our {@code WhatDataTask} loads the chapter selected from our resources).
     * Finally we loop over {@code int i} for all of the resource ids in {@code int[] resourceIDS} calling
     * our method {@code addButton} to add a {@code Button} to {@code whatChapter} whose title is given by
     * {@code titles[i]} and whose {@code OnClickListener} sets the visibility of {@code whatChapterScrollView}
     * to GONE, and calls our method {@code loadResourceHtml} to have a {@code WhatDataTask} instance load
     * the html file with resource id {@code resourceIDS[i]} in the background into {@code whatTextView}
     * (its {@code onPostExecute} override also changes the visibility of {@code whatWaiting} to GONE).
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use
     */
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

    /**
     * Adds a {@code Button} to its parameter {@code ViewGroup parent} whose label is given by its
     * parameter {@code String description} and whose {@code OnClickListener} sets the visibility of
     * the {@code ScrollView whatChapterScrollView} that holds our chapter selection UI to GONE and
     * calls our method {@code loadResourceHtml} to have it load and display the Html resource file
     * with id {@code int resourceID} in the background.
     *
     * @param resourceID  resource ID that our button's {@code OnClickListener} should call the method
     *                    {@code loadResourceHtml} to load in the background.
     * @param description Label for our {@code Button}
     * @param parent      {@code ViewGroup} we should add our {@code Button} to.
     */
    public void addButton(final int resourceID, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setText(description);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the {@code Button} is clicked. We just set the visibility of our field
             * {@code ScrollView whatChapterScrollView} to GONE (disappears our chapter selection
             * buttons), set the visibility of our field {@code TextView whatWaiting} to VISIBLE
             * and call our method {@code loadResourceHtml} to load the file whose resource
             * ID is that given by the {@code addButton} method's parameter {@code resourceID}.
             *
             * @param v {@code View} that was clicked.
             */
            @Override
            public void onClick(View v) {
                whatChapterScrollView.setVisibility(View.GONE);
                whatWaiting.setVisibility(View.VISIBLE);
                loadResourceHtml(resourceID);
            }
        });
        parent.addView(button);
    }

    /**
     * Loads the Html file with the resource ID of our parameter {@code int resourceID} in the background
     * then displays it in our field {@code TextView whatTextView} when it is done loading. We initialize
     * our variable {@code WhatDataTask mWhatDataTask} with a new instance, whose {@code onPostExecute}
     * override sets the text of {@code whatTextView} to the {@code Spanned} returned from the method
     * {@code doInBackground} of {@code mWhatDataTask}, sets the visibility of {@code TextView whatWaiting}
     * to GONE, and sets the visibility of {@code whatTextView} to VISIBLE. Having done this we call the
     * {@code execute} method of {@code mWhatDataTask} (which in turn calls the {@code doInBackground}
     * method in a separate thread) to have it load the file whose resource ID is our parameter
     * {@code resourceID}.
     *
     * @param resourceID resource ID of Html file located in our raw resources.
     */
    private void loadResourceHtml(int resourceID) {
        @SuppressLint("StaticFieldLeak")
        WhatDataTask mWhatDataTask = new WhatDataTask(getApplicationContext()) {
            /**
             * Runs on the UI thread after {@link #doInBackground}. The parameter {@code Spanned s}
             * is the value returned by {@link #doInBackground}. We set the text of our field
             * {@code TextView whatTextView} to our parameter {@code s}, set the visibility of our
             * field {@code TextView whatWaiting} to GONE, then set the visibility of {@code whatTextView}
             * to VISIBLE.
             *
             * @param s The result of the operation computed by {@link #doInBackground}.
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
