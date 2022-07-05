package com.example.android.markovchain.whatisman

import android.app.Activity
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android.markovchain.R

/**
 * This [Activity] loads html files from the raw resources of the app in the background, and
 * displays them in a [TextView].
 */
class WhatIsManActivity : AppCompatActivity() {
    /**
     * [TextView] used to display our book chapters
     */
    lateinit var whatTextView: TextView

    /**
     * [TextView] used to display "Waiting for data to load…" message while waiting
     */
    lateinit var whatWaiting: TextView

    /**
     * [LinearLayout] that we add our chapter selection [Button]'s to.
     */
    private lateinit var whatChapter: LinearLayout

    /**
     * [ScrollView] that holds the [LinearLayout] field [whatChapter] (which holds our chapter
     * selection [Button]'s)
     */
    private lateinit var whatChapterScrollView: ScrollView

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file R.layout.activity_what_is_man. We initialize our
     * [LinearLayout] field [whatChapter] by finding the view with id R.id.what_chapter (the chapter
     * selection buttons are placed here), initialize our [ScrollView] field [whatChapterScrollView]
     * by finding the view with id R.id.what_chapter_scrollView (holds our [LinearLayout] field
     * [whatChapter] which in turn holds our chapter selection buttons), initialize our [TextView]
     * field [whatTextView] by finding the view with id R.id.what_textView (the selected chapter
     * will be displayed here), and initialize our [TextView] field [whatWaiting] by finding the
     * view with id R.id.what_waiting (this will be displayed while our `WhatDataTask` loads the
     * chapter selected from our resources). Finally we loop over the [Int] variable `i` for all
     * of the resource ids in the [Int] array [resourceIDS] calling our method [addButton] method
     * to add a [Button] to [whatChapter] whose title is given by `titles[ i ]` and whose
     * `OnClickListener` sets the visibility of [whatChapterScrollView] to GONE, sets the visibility
     * of [whatWaiting] to VISIBLE and calls our method [loadResourceHtml] to have a [WhatDataTask]
     * instance load the html file with resource id `resourceIDS[ i ]` in the background into
     * [whatTextView] (its `onPostExecute` override also changes the visibility of [whatWaiting]
     * to GONE).
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
     * Adds a [Button] to its [ViewGroup] parameter [parent] whose label is given by its
     * [String] parameter [description] and whose `OnClickListener` sets the visibility of
     * the [ScrollView] field [whatChapterScrollView] that holds our chapter selection UI to GONE,
     * sets the visibility of our "Waiting" [TextView] field [whatWaiting] to VISIBLE  and
     * calls our method [loadResourceHtml] to have it load and display the Html resource file
     * whose resource id is given by our [Int] parameter [resourceID] in the background.
     *
     * @param resourceID  resource ID that our button's `OnClickListener` should call the method
     * [loadResourceHtml] to load in the background.
     * @param description Label for our [Button]
     * @param parent      [ViewGroup] we should add our [Button] to.
     */
    private fun addButton(resourceID: Int, description: String, parent: ViewGroup) {
        val button = Button(this)
        button.text = description
        /**
         * Called when the [Button] is clicked. We just set the visibility of our [ScrollView] field
         * [whatChapterScrollView] to GONE (disappears our chapter selection buttons), set the
         * visibility of our [TextView] field [whatWaiting] to VISIBLE and call our method
         * [loadResourceHtml] to load the file whose resource ID is that given by the `addButton`
         * method's parameter `resourceID`.
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
     * Loads the Html file with the resource ID of our [Int] parameter [resourceID] in the background
     * then displays it in our [TextView] field [whatTextView] when it is done loading. We initialize
     * our [WhatDataTask] variable `val mWhatDataTask` with a new instance, whose `onPostExecute`
     * override sets the text of [whatTextView] to the [Spanned] returned from the `doInBackground`
     * method of `mWhatDataTask`, sets the visibility of the [TextView] field [whatWaiting] to GONE,
     * and sets the visibility of [whatTextView] to VISIBLE. Having constructed `mWhatDataTask` we
     * call the `execute` method of `mWhatDataTask` (which in turn calls the `doInBackground`
     * method in a separate thread) to have it load the file whose resource ID is our parameter
     * [resourceID].
     *
     * @param resourceID resource ID of Html file located in our raw resources.
     */
    private fun loadResourceHtml(resourceID: Int) {
        val mWhatDataTask = object : WhatDataTask(applicationContext) {
            /**
             * Runs on the UI thread after [.doInBackground]. The parameter `Spanned s`
             * is the value returned by [.doInBackground]. We set the text of our field
             * `TextView whatTextView` to our parameter `s`, set the visibility of our
             * field `TextView whatWaiting` to GONE, then set the visibility of `whatTextView`
             * to VISIBLE.
             *
             * @param result The result of the operation computed by [.doInBackground].
             */
            override fun onPostExecute(result: Spanned?) {
                whatTextView.text = (result ?: return)
                whatWaiting.visibility = View.GONE
                whatTextView.visibility = View.VISIBLE
            }
        }
        mWhatDataTask.execute(resourceID)
    }

    /**
     * Our static constants.
     */
    companion object {
        /**
         * List of the resource ids for the chapters in "What Is Man"
         */
        val resourceIDS: IntArray = intArrayOf(
            R.raw.chapter1,
            R.raw.chapter2,
            R.raw.chapter3,
            R.raw.chapter4,
            R.raw.chapter5,
            R.raw.chapter6,
            R.raw.chapter7,
            R.raw.chapter8,
            R.raw.chapter9,
            R.raw.chapter10,
            R.raw.chapter11,
            R.raw.chapter12,
            R.raw.chapter13,
            R.raw.chapter14,
            R.raw.chapter15
        )

        /**
         * List of the titles for the chapters in "What Is Man" (used to label the selection buttons)
         */
        val titles: Array<String> = arrayOf(
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
        )
    }

}
