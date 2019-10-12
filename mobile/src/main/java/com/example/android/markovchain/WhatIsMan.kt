package com.example.android.markovchain

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/**
 * This `Activity` loads html files from the raw resources of the app in the background, and
 * displays them in a `TextView`.
 */
@Suppress("MemberVisibilityCanBePrivate")
class WhatIsMan : Activity() {
    /**
     * `TextView` used to display our book chapters
     */
    lateinit var whatTextView: TextView
    /**
     * `TextView` used to display "Waiting for data to load…" message while waiting
     */
    lateinit var whatWaiting: TextView
    /**
     * `LinearLayout` that we add our chapter selection `Button`s to.
     */
    lateinit var whatChapter: LinearLayout
    /**
     * `ScrollView` that holds the `LinearLayout whatChapter`
     */
    lateinit var whatChapterScrollView: ScrollView

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file R.layout.activity_what_is_man. We initialize our
     * field `LinearLayout whatChapter` by finding the view with id R.id.what_chapter (the chapter
     * selection buttons are placed here), initialize our field `ScrollView whatChapterScrollView`
     * by finding the view with id R.id.what_chapter_scrollView (holds the `LinearLayout whatChapter`
     * that holds our chapter selection buttons), initialize our field `TextView whatTextView` by
     * finding the view with id R.id.what_textView (the selected chapter will be displayed here), and
     * initialize our field `TextView whatWaiting` by finding the view with id R.id.what_waiting
     * (this will be displayed while our `WhatDataTask` loads the chapter selected from our resources).
     * Finally we loop over `int i` for all of the resource ids in `int[] resourceIDS` calling
     * our method `addButton` to add a `Button` to `whatChapter` whose title is given by
     * `titles[ i ]` and whose `OnClickListener` sets the visibility of `whatChapterScrollView`
     * to GONE, and calls our method `loadResourceHtml` to have a `WhatDataTask` instance load
     * the html file with resource id `resourceIDS[ i ]` in the background into `whatTextView`
     * (its `onPostExecute` override also changes the visibility of `whatWaiting` to GONE).
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_is_man)

        whatChapter = findViewById(R.id.what_chapter)
        whatChapterScrollView = findViewById(R.id.what_chapter_scrollView)
        whatTextView = findViewById(R.id.what_textView)
        whatWaiting = findViewById(R.id.what_waiting)
        for (i in resourceIDS.indices) {
            addButton(resourceIDS[i], titles[i], whatChapter)
        }
    }

    /**
     * Adds a `Button` to its parameter `ViewGroup parent` whose label is given by its
     * parameter `String description` and whose `OnClickListener` sets the visibility of
     * the `ScrollView whatChapterScrollView` that holds our chapter selection UI to GONE and
     * calls our method `loadResourceHtml` to have it load and display the Html resource file
     * with id `int resourceID` in the background.
     *
     * @param resourceID  resource ID that our button's `OnClickListener` should call the method
     * `loadResourceHtml` to load in the background.
     * @param description Label for our `Button`
     * @param parent      `ViewGroup` we should add our `Button` to.
     */
    fun addButton(resourceID: Int, description: String, parent: ViewGroup) {
        val button = Button(this)
        button.text = description
        /**
         * Called when the `Button` is clicked. We just set the visibility of our field
         * `ScrollView whatChapterScrollView` to GONE (disappears our chapter selection
         * buttons), set the visibility of our field `TextView whatWaiting` to VISIBLE
         * and call our method `loadResourceHtml` to load the file whose resource
         * ID is that given by the `addButton` method's parameter `resourceID`.
         *
         * Parameter: `View` that was clicked.
         */
        button.setOnClickListener {
            whatChapterScrollView.visibility = View.GONE
            whatWaiting.visibility = View.VISIBLE
            loadResourceHtml(resourceID)
        }
        parent.addView(button)
    }

    /**
     * Loads the Html file with the resource ID of our parameter `int resourceID` in the background
     * then displays it in our field `TextView whatTextView` when it is done loading. We initialize
     * our variable `WhatDataTask mWhatDataTask` with a new instance, whose `onPostExecute`
     * override sets the text of `whatTextView` to the `Spanned` returned from the method
     * `doInBackground` of `mWhatDataTask`, sets the visibility of `TextView whatWaiting`
     * to GONE, and sets the visibility of `whatTextView` to VISIBLE. Having done this we call the
     * `execute` method of `mWhatDataTask` (which in turn calls the `doInBackground`
     * method in a separate thread) to have it load the file whose resource ID is our parameter
     * `resourceID`.
     *
     * @param resourceID resource ID of Html file located in our raw resources.
     */
    private fun loadResourceHtml(resourceID: Int) {
        @SuppressLint("StaticFieldLeak")
        val mWhatDataTask = object : WhatDataTask(applicationContext) {
            /**
             * Runs on the UI thread after [.doInBackground]. The parameter `Spanned s`
             * is the value returned by [.doInBackground]. We set the text of our field
             * `TextView whatTextView` to our parameter `s`, set the visibility of our
             * field `TextView whatWaiting` to GONE, then set the visibility of `whatTextView`
             * to VISIBLE.
             *
             * @param s The result of the operation computed by [.doInBackground].
             */
            override fun onPostExecute(s: Spanned) {
                whatTextView.text = s
                whatWaiting.visibility = View.GONE
                whatTextView.visibility = View.VISIBLE
            }
        }
        mWhatDataTask.execute(resourceID)
    }

    companion object {

        /**
         * List of the resource ids for the chapters in "What Is Man"
         */
        val resourceIDS = intArrayOf(R.raw.chapter1, R.raw.chapter2, R.raw.chapter3, R.raw.chapter4, R.raw.chapter5, R.raw.chapter6, R.raw.chapter7, R.raw.chapter8, R.raw.chapter9, R.raw.chapter10, R.raw.chapter11, R.raw.chapter12, R.raw.chapter13, R.raw.chapter14, R.raw.chapter15)

        /**
         * List of the titles for the chapters in "What Is Man" (used to label the selection buttons)
         */
        val titles = arrayOf("Chapter 1: What is Man?", "Chapter 2: The Death of Jean", "Chapter 3: The Turning-Point of My Life", "Chapter 4: How to Make History Dates Stick", "Chapter 5: The Memorable Assassination", "Chapter 6: A Scrap of Curious History", "Chapter 7: Switzerland, the Cradle of Liberty", "Chapter 8: At the Shrine of St. Wagner", "Chapter 9: William Dean Howells", "Chapter 10: English as she is Taught", "Chapter 11: A Simplified Alphabet", "Chapter 12: As Concerns Interpreting the Deity", "Chapter 13: Concerning Tobacco", "Chapter 14: The Bee", "Chapter 15: Taming the Bicycle")
    }

}
