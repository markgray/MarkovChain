package com.example.android.markovchain.bible

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.ConditionVariable
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * This is the main activity of our Bible Text reading function.
 */
class BibleActivity : FragmentActivity() {
    /**
     * Reference to the `RecyclerView` in our layout
     */
    private lateinit var mRecyclerView: RecyclerView

    /**
     * `LayoutManager` for our `RecyclerView`
     */
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    /**
     * The Adapter used to fill our `RecyclerView`
     */
    private lateinit var mAdapter: BibleAdapter

    /**
     * List of Bible verses
     */
    var stringList: ArrayList<String> = ArrayList()

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`.
     * We set the field [mDoneReading] to false so that our UI thread knows to wait until our text
     * file is read into memory before trying to access the data. We reset our [ConditionVariable]
     * field [mDoneReading] to the closed state so that any threads that call its `block()` method
     * will block until someone calls its `open()` method. We next set our field [Context] field
     * [bibleContext] to the context of the single, global Application object of the current process.
     * This is because we will later need a [Context] whose lifecycle is separate from the current
     * context, that is tied to the lifetime of the process rather than the current component (I
     * forget why we need this though?). We next call our method [initDataSet] which will spawn a
     * background thread to read in the data file we will be using (R.raw.king_james_text_and_verse)
     * and create the data structures needed by our activity. Then we set our content View to our
     * layout file R.layout.activity_bible_fragment, locate the [RecyclerView] in the layout with
     * the id R.id.bible_recyclerview and save a reference to it in our [RecyclerView] field
     * [mRecyclerView]. We initialize our [BibleAdapter] field [mAdapter] to a new instance using
     * the List containing the text (`ArrayList<String> stringList`), the List containing chapter
     * and verse annotation for each paragraph (`ArrayList<String> bookChapterVerse`), and using the
     * layout manager `RecyclerView.LayoutManager mLayoutManager` (an instance of `LinearLayoutManager`
     * created above). We now set the adapter of [mRecyclerView] to [mAdapter], and the layout manager
     * to be used to [mLayoutManager].
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        doneReading = false
        mDoneReading.close()
        bibleContext = applicationContext
        initDataSet()
        setContentView(R.layout.activity_bible_fragment)
        mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView = findViewById(R.id.bible_recyclerview)
        mAdapter = BibleAdapter(stringList, bookChapterVerse, mLayoutManager)
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background, but
     * has not (yet) been killed. We initialize our [Int] `val lastFirstVisiblePosition` to the verse
     * number of the first completely visible verse by calling the `findFirstCompletelyVisibleItemPosition`
     * method of the `LayoutManager` of [mRecyclerView] then call our method [saveVerseNumber] to save
     * the verse number to our shared preference file under the index LAST_VERSE_VIEWED. Finally we
     * call our super's implementation of `onPause`.
     */
    override fun onPause() {

        val lastFirstVisiblePosition = (mRecyclerView.layoutManager as LinearLayoutManager)
            .findFirstCompletelyVisibleItemPosition()
        saveVerseNumber(lastFirstVisiblePosition, LAST_VERSE_VIEWED)
        super.onPause()
    }

    /**
     * Called after `onRestoreInstanceState`, `onRestart`, or `onPause`, for our
     * activity to start interacting with the user.
     *
     * Our first action is to initialize our [Int] `val lastFirstVisiblePosition` to the number of
     * the last verse viewed from our shared preference file by calling our method [restoreVerseNumber],
     * then we use this number to move our `BibleAdapter` to this verse. Finally we call our
     * super's implementation of `onResume`.
     */
    override fun onResume() {
        val lastFirstVisiblePosition = restoreVerseNumber(0, LAST_VERSE_VIEWED)
        BibleAdapter.moveToVerse(mRecyclerView, lastFirstVisiblePosition)
        super.onResume()
    }

    /**
     * Perform any final cleanup before our activity is destroyed. If our [textToSpeech] field is not
     * null we have been using text to speech in `BibleSpeak` so we interrupt the current utterance
     * and discard other utterances in the queue by calling the `stop()` method of `textToSpeech`,
     * release the resources used by the `TextToSpeech` engine by calling its `shutdown()`
     * method and then set textToSpeech to null (unnecessary since we are in `onDestroy`, but
     * better safe than sorry!). Finally we call our super's implementation of of `onDestroy()`.
     */
    public override fun onDestroy() {
        Log.i(TAG, "onDestroy has been called")
        // Don't forget to shutdown!
        if (textToSpeech != null) {
            (textToSpeech ?: return).stop()
            (textToSpeech ?: return).shutdown()
            textToSpeech = null
        }
        super.onDestroy()
    }

    /**
     * Retrieve the last viewed verse from shared preferences file. First we initialize our variable
     * `SharedPreferences pref` to a reference to the preferences file for the class `CLASS`
     * (`BibleMain` in our case), then we retrieve and return the int value stored in `pref`
     * under the key `key` (defaulting to our parameter `int verse` if no value is stored
     * yet).
     *
     * @param verse verse number default value to return if no value stored yet
     * @param key   key it was stored under (presently only "LAST_VERSE_VIEWED")
     * @return      verse number stored in shared preferences, or the default value passed it
     */
    private fun restoreVerseNumber(verse: Int, key: String): Int {
        val pref = getSharedPreferences(CLASS, Context.MODE_PRIVATE)
        return pref.getInt(key, verse)
    }

    /**
     * Convenience function for starting a `BibleDialog` fragment. First we set our fields
     * `String dialogTitle` and `String dialogText` to our arguments for later use, then
     * we call the factory method `newInstance` of `BibleDialog` to create a new
     * `BibleDialog bibleDialog` using our parameters `label` and `text` for its
     * label and text and save it in our static field `BibleDialog bibleDialog` for later use.
     * Finally we call our overloaded method `showDialog(DialogFragment)` to pop up the instance
     * of BibleDialog we just created in `bibleDialog`.
     * TODO: Create only one BibleDialog ever!
     *
     * @param label citation for verse contained in text
     * @param text  text of current verse
     */
    fun showDialog(label: String, text: String) {
        dialogTitle = label
        dialogText = text

        // Create and show the dialog.
        bibleDialog = BibleDialog.newInstance(label, text) // TODO: Create only one BibleDialog ever!
        showDialog(bibleDialog)
    }

    /**
     * Shows the `DialogFragment dialogFragment` passed to it. First we fetch a reference to
     * the `FragmentManager` used for interacting with fragments associated with this activity
     * and use it to start a series of `Fragment` edit operations using our variable
     * `FragmentTransaction ft`. Next we initialize our variable `Fragment prev` by using
     * the `FragmentManager` to search for any existing `Fragment`'s with the tag "dialog"
     * (the tag used for all the `DialogFragment`'s we display), and if one is found (`prev`
     * is not equal to null) we use `ft` to remove it. We add `ft` to the BackStack so
     * that the back Button will remember this transaction once we commit the transaction, then we
     * call the `show` method of our parameter [dialogFragment] to display the dialog,
     * adding the fragment using `ft`, with the tag "dialog" and then committing that
     * `FragmentTransaction`.
     *
     * @param dialogFragment DialogFragment subclass which already has had setArguments
     * called to attach a Bundle of arguments
     * @return identifier of the committed transaction, as per `FragmentTransaction.commit()`.
     */
    private fun showDialog(dialogFragment: DialogFragment?): Int {
        // dialogFragment must already have setArguments set to bundle!
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        return dialogFragment!!.show(ft, "dialog")
    }

    /**
     * Convenience function to dismiss the main `BibleDialog` `BibleDialog bibleDialog`.
     * This is called only from the `OnClickListener` for the "DISMISS" Button in `BibleDialog`,
     * and exists due to errors encountered calling `DialogFragment.dismiss()` directly from that
     * callback. We simply use our reference to it in `BibleDialog bibleDialog` to call its
     * `dismiss()` method, then set our field `BibleDialog bibleDialog` to *null* in hopes it will
     * be garbage collected. TODO: Create only one BibleDialog ever!
     */
    fun dismissDialog() {
        (bibleDialog ?: return).dismiss()
        bibleDialog = null
    }

    /**
     * Handle whichever action was chosen in the `Spinner` contained in the `BibleDialog`
     * fragment. This is called from `BibleDialog` both in the `onItemSelected` callback
     * for the `Spinner`, and in the `onClick` callback for the "REPEAT" Button. We switch
     * based on the parameter `spinIndex` which is the position of the Spinner view which was
     * selected:
     *  - CHOICE_NONE: Do nothing, and break.
     *  - CHOICE_RANDOM_VERSE: Instruct the BibleAdapter to move to a random verse by calling
     * its `moveToRandom` method (the `View v` is passed so that it can toast the
     * verse moved to), then instruct the `BibleDialog bibleDialog` to refresh its
     * display of the citation and verse that was chosen, then break.
     *  - CHOICE_GOOGLE: Launch a new instance of the `BibleSearch` `DialogFragment`
     * using the current `dialogTitle` (citation) and `dialogText` (verse), then break.
     *  - CHOICE_BOOKMARK: Launch a new instance of the `BibleBookmark` `DialogFragment`
     * using the current `dialogTitle` (citation) and `dialogText` (verse), then break.
     *  - CHOICE_GO_TO_VERSE: Launch a new instance of the `BibleChoose` `DialogFragment`
     * using the current `dialogTitle` (citation) and `dialogText` (verse), then break.
     *  - CHOICE_READ_ALOUD: Launch a new instance of the `BibleSpeak` `DialogFragment`
     * using the current `dialogTitle` (citation) and `dialogText` (verse), then break.
     *  - default: If it is none of the above, then just break, although this could only happen
     * as the result of a programming error so we should probably throw an exception.
     *
     * @param v Just a view passed to give context for creating a Toast when called for
     * @param spinIndex Spinner index chosen
     */
    fun handleAction(v: View, spinIndex: Int) {
        when (spinIndex) {
            BibleDialog.CHOICE_NONE -> {
            }

            BibleDialog.CHOICE_RANDOM_VERSE -> {
                BibleAdapter.moveToRandom(v)
                (bibleDialog ?: return).refresh(dialogTitle, dialogText)
            }

            BibleDialog.CHOICE_GOOGLE -> {
                showDialog(BibleSearch.newInstance(dialogTitle, dialogText))
                BibleAdapter.moveToVerse(v, dialogVerse)
                (bibleDialog ?: return).refresh(dialogTitle, dialogText)
            }

            BibleDialog.CHOICE_BOOKMARK -> showDialog(BibleBookmark.newInstance(dialogTitle, dialogText))
            BibleDialog.CHOICE_GO_TO_VERSE -> showDialog(BibleChoose.newInstance(dialogTitle, dialogText))
            BibleDialog.CHOICE_READ_ALOUD -> showDialog(BibleSpeak.newInstance(dialogTitle, dialogText))
            else -> {
            }
        }
    }

    /**
     * Reads the raw file king_james_text_and_verse.txt, separating it into citations (the list in
     * `ArrayList<String> bookChapterVerse`), and verse text (the list `ArrayList<String> stringList`).
     * First we open `InputStream inputStream` to read our copy of the Bible (R.raw.king_james_text_and_verse)
     * from our raw resource directory. Then we create `BufferedReader reader` from an instance of
     * `InputStreamReader` created from `inputStream` (`InputStreamReader` does the character conversion
     * needed to convert the byte stream coming from the `inputStream` to characters, and `BufferedReader`
     * allows us to use `readLine` on the characters coming from the `InputStreamReader`). Then we set
     * the `doneReading` flag to false so that other code does not try to access our data until it is
     * ready to be used. Then we create the `Thread mThread` to read in the data from `reader` and
     * start that thread running.
     */
    private fun initDataSet() {
        val inputStream = applicationContext.resources.openRawResource(R.raw.king_james_text_and_verse)
        val reader = BufferedReader(InputStreamReader(inputStream))
        doneReading = false

        /*
         * This is the thread that will do our work. First it calls *close()* on the
         * ConditionVariable mDoneReading so that other Thread's may block on mDoneReading until
         * we call *open()*. Then it proceeds to read and process every line in reader
         * until readLine() returns null (EOF). The processing consists of separating the text
         * into the citation for each verse (first line of a paragraph) which it adds to the field
         * ArrayList<String> bookChapterVerse, and the lines of the text of the verse (all the
         * lines until an empty line is encountered) which it appends to the StringBuilder builder.
         * When the empty line terminating the verse is found (line.isEmpty() returns *true*, it
         * converts StringBuilder build to a String and adds it to ArrayList<String> stringList,
         * then it sets the length of StringBuilder builder to 0 and breaks from the verse building
         * loop in order to start reading in the next citation and verse. Once it is done reading
         * all the lines in BufferedReader reader (reader.readLine() returns null) it closes
         * BufferedReader reader (which also closes the InputStreamReader and InputStream it was
         * reading from (I hope)), opens the ConditionVariable mDoneReading and sets doneReading to true.
         */
        val mThread = object : Thread() {
            /**
             * Line read from `BufferedReader reader`
             */
            var line: String? = null

            /**
             * `StringBuilder` used to assemble the lines of the current verse.
             */
            var builder = StringBuilder()

            /**
             * Called when the `Thread`'s `start()` method is called. First we call the `close()`
             * method of our field `ConditionVariable mDoneReading` so that other `Thread`'s may
             * block on `mDoneReading` until we call its `open()` method. Then wrapped in a try block
             * intended to catch and log IOException we call our [lineFiller] method to read each
             * `line` from `reader` until EOF is indicated by a null return. The structure of the file
             * is such that the first line is the citation followed by the lines of the verse, terminated
             * by an empty line, so in an outer loop we add the citation `line` to our list of
             * citations `ArrayList<String> bookChapterVerse`, then in an inner loop we append the
             * `line` to `builder` then branch on whether the `line` was empty or not:
             *  - If the `line` is empty: we add the string value of `builder` to our list of verses
             *  `ArrayList<String> stringList`, set the length of `builder` to 0, and  break out of
             *  the inner loop to work on the next citation and its verse.
             *  - If the `line` is **not** empty:: We append a space character to `builder`
             *
             * When we have reached the EOF we log the number of verses read, close `reader` and
             * exit the try block. Having read in the file we open `ConditionVariable mDoneReading`
             * and set our done reading flag `boolean doneReading` to true.
             */
            override fun run() {
                mDoneReading.close()
                try {
                    while (lineFiller()) {
                        bookChapterVerse.add(line ?: return)
                        while (lineFiller()) {
                            builder.append(line)
                            if ((line ?: return).isEmpty()) {
                                stringList.add(builder.toString())
                                builder.setLength(0)
                                break
                            } else {
                                builder.append(" ")
                            }
                        }
                    }
                    Log.i(TAG, "Verses read: " + stringList.size)
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mDoneReading.open()
                doneReading = true
            }

            /**
             * Convenience function to replace the java idiom:
             *
             * "while((line = reader.readLine()) != null)
             *
             * Reads the next line from [reader] into [line] and returns *true* if [line] is not
             * equal to *null*.
             *
             * @return *true* if the [line] read is not *null* or *false* if it is (EOF occurred)
             */
            fun lineFiller(): Boolean {
                line = reader.readLine()
                return line != null
            }
        }
        mThread.start()
    }

    companion object {
        /**
         * TAG for logging
         */
        const val TAG: String = "BibleMain"

        /**
         * Key for shared preference to save verse number
         */
        const val LAST_VERSE_VIEWED: String = "LAST_VERSE_VIEWED"

        /**
         * Used to access shared preference file
         */
        private val CLASS = BibleActivity::class.java.simpleName

        /**
         * Application Context for `BibleMain` and dialogs to use when necessary
         */
        @SuppressLint("StaticFieldLeak") // TODO: Fix static field leak
        lateinit var bibleContext: Context

        /**
         * Flag used to signal that we are done reading in the Bible
         */
        var doneReading: Boolean = false

        /**
         * Used to block until reading is finished
         */
        var mDoneReading: ConditionVariable = ConditionVariable()

        /**
         * List of citations corresponding to each `stringList` verse
         */
        var bookChapterVerse: ArrayList<String> = ArrayList()

        /**
         * Contains reference to the `BibleDialog` launched by long clicking a verse
         */
        @SuppressLint("StaticFieldLeak") // TODO: fix static field length
        var bibleDialog: BibleDialog? = null

        /**
         * Canonical Bible citation used as label for `BibleDialog` and the other dialogs
         */
        lateinit var dialogTitle: String

        /**
         * Verse text used as text contents for `BibleDialog` and the other dialogs
         */
        lateinit var dialogText: String

        /**
         * Verse number used in `BibleDialog` and the other dialogs
         */
        var dialogVerse: Int = 0

        /**
         * `TextToSpeech` instance used by `BibleSpeak`
         */
        var textToSpeech: TextToSpeech? = null

        /**
         * Saves the currently viewed verse number (or any other int) to shared preferences file
         * under the key "key". First we initialize our variable `SharedPreferences pref` to a
         * reference to the preferences file for the class `CLASS` (`BibleMain`'s class). We create
         * an `SharedPreferences.Editor editor` from `pref`, use this `Editor` to save as an int
         * value our parameter verse using the key `key`, and finally commit our changes from our
         * `Editor` to `pref`. (Only memory copy is written to, an asynchronous `commit` is started
         * to write to disk.)
         *
         * @param verse verse number or other int
         * @param key   key to store it under (presently only "LAST_VERSE_VIEWED")
         */
        fun saveVerseNumber(verse: Int, key: String) {
            val pref = bibleContext.getSharedPreferences(CLASS, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(key, verse)
            editor.apply()
        }

        /**
         * Finds the verse index number of a given standard Bible citation `String citation`, or of
         * the fallback citation `String fallback` in case there are no exact matches for the given
         * citation. We start by initializing our [Int] variable ``fallBackIndex` to 0 so that if
         * neither [citation] nor [fallback] is found by exact match we will return 0 (very beginning
         * of Bible). Then we loop over `i` through the entire list of citations in our list
         * `ArrayList<String> bookChapterVerse` checking whether the `String candidateVerse`
         * in position `i` of [bookChapterVerse] is equal to [citation] (in which case we immediately
         * return the index `i` for the verse in question) or whether `candidateVerse` is equal to
         * `fallback` (in which case we set `fallBackIndex` to the index `i`). Finally if an exact
         * match for `citation` has not been found in our [bookChapterVerse] list we return
         * `fallBackIndex` to the caller.
         *
         * @param citation Bible citation we are looking for
         * @param fallback a fallback citation to use if that citation is not found
         * @return Index of the verse we are interested in
         */
        fun findFromCitation(citation: String, fallback: String): Int {
            var fallBackIndex = 0
            for (i in bookChapterVerse.indices) {
                val candidateVerse = bookChapterVerse[i]
                if (citation == candidateVerse) {
                    return i
                }
                if (fallback == candidateVerse) {
                    fallBackIndex = i
                }
            }
            return fallBackIndex
        }

        /**
         * Returns the book number index for a citation `String citation` which uses the name instead
         * of the number. First we strip off the book name from the citation into `String bookLook`
         * (all characters up to the first ":"). We then loop for all of the pairs of [Int] `indexOf`
         * indices and [String] `book` elements produced by the `withIndex()` method of the `books`
         * array of book names of the [BibleAdapter] class until we find a `book` that matches our
         * `bookLook` name upon which we return the `indexOf` corresponding to the `book` to the
         * caller. If none of the books in `books` match we return 0.
         *
         * @param citation Standard Bible citation
         * @return Index of the book name
         */
        fun indexFromCitation(citation: String): Int {
            val bookLook = citation.substring(0, citation.indexOf(":"))
            for ((indexOf, book) in BibleAdapter.books.withIndex()) {
                if (bookLook == book) {
                    return indexOf
                }
            }
            return 0
        }
    }

}
