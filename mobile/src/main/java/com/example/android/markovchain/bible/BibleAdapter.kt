package com.example.android.markovchain.bible

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R
import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import kotlin.math.abs

/**
 * Adapter used by `BibleMain` to populate its `RecyclerView`
 */
class BibleAdapter
/**
 * Our constructor. We save our parameter `ArrayList<String> dataSet` in our field
 * `ArrayList<String> mDataSet`, our parameter `ArrayList<String> chapterAndVerse`
 * in our field `ArrayList<String> mChapterAndVerse`, and our parameter
 * `RecyclerView.LayoutManager layoutManager` in our field `LinearLayoutManager mLayoutManager`.
 * Then we call our `initializeNumberToBook` method to initialize our map of book names indexed
 * by the book number: `HashMap<String, String> numberToBook`.
 *
 * @param dataSet         List containing the text
 * @param chapterAndVerse chapter and verse annotation for each paragraph
 * @param layoutManager   layout manager to use
 */
(dataSet: ArrayList<String>, chapterAndVerse: ArrayList<String>, layoutManager: RecyclerView.LayoutManager) : RecyclerView.Adapter<BibleAdapter.ViewHolder>() {

    init {
        mDataSet = dataSet
        mChapterAndVerse = chapterAndVerse
        mLayoutManager = layoutManager as LinearLayoutManager
        initializeNumberToBook()
    }

    /**
     * Builds a `HashMap` mapping book number to book name. If the number to name map is already
     * initialized (`numberToBookInitialized` is true) we simply return the current value of
     * `HashMap<String, String> numberToBook`, otherwise we allocate space on the heap for
     * `HashMap<String, String> numberToBook` and then loop through our `String[] numbers`
     * array and "put" the corresponding entry in the `String[] books` array into `numberToBook`
     * indexed by the entry in `numbers`. When done we set `numberToBookInitialized` to true
     * and return the initialized `numberToBook` to the caller.
     *
     * @return The HashMap created (unneeded since numberToBook is also set in the method, but it
     * does give the use of the method additional flexibility)
     */
    private fun initializeNumberToBook(): HashMap<String, String> {
        if (!numberToBookInitialized) {
            numberToBook = HashMap(66)
            for (i in numbers.indices) {
                numberToBook[numbers[i]] = books[i]
            }
            numberToBookInitialized = true
        }

        return numberToBook
    }

    /**
     * Provides a reference to the type of views that we are using (custom `ViewHolder`), and
     * also allows us to store additional information about our content.
     */
    class ViewHolder
    /**
     * Our constructor. First we call our super's constructor. Then we set the `onClickListener`
     * of our parameter `View v` to an anonymous class whose `onClick` override toasts
     * the canonical Bible citation for the verse that the view will hold, and sets its
     * `onLongClickListener` to an anonymous class which will launch a `BibleDialog DialogFragment`
     * for the verse.
     *
     * @param v View that this is the ViewHolder for
     */
    (v: View) : RecyclerView.ViewHolder(v) {

        /**
         * `TextView` in our layout to use for the verse we display.
         *
         * @return the current contents of the `TextView vTextView` field
         */
        var textView: TextView = v.findViewById(R.id.vTextView)

        init {
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener { view ->
                /**
                 * Creates a Toast of the canonical Bible citation for the verse contained
                 * in the `View` that was clicked.
                 *
                 * @param view View that was clicked
                 */
                Log.d(TAG, "Element $layoutPosition clicked.")
                Toast.makeText(
                        view.context,
                        makeCitation(mChapterAndVerse[layoutPosition]),
                        Toast.LENGTH_LONG
                ).show()
            }
            // Define long click listener for the ViewHolder's View.
            v.setOnLongClickListener { view ->
                /**
                 * Pops up a `BibleDialog DialogFragment` containing the citation and text of
                 * the verse contained in the View that was long clicked, and sets the `dialogVerse`
                 * field of `BibleMain` to point to the verse index number (which is also the adapter
                 * position.) Finally we return true to consume the long click here.
                 *
                 * @param view View that was long clicked
                 * @return true because the callback consumed the long click
                 */
                (view.context as BibleActivity).showDialog(makeCitation(mChapterAndVerse[layoutPosition]),
                        textView.text as String)
                BibleActivity.dialogVerse = adapterPosition
                true
            }
        }

    }

    /**
     * Create new views (invoked by the layout manager). We create a new `View v` by using the
     * `LayoutInflater` for the `Context` of our parameter `ViewGroup viewGroup` to
     * inflate the layout file for our items: R.layout.line_list_item, and return a `ViewHolder`
     * constructed using this `v`.
     *
     * @param viewGroup The `ViewGroup` into which the new `View` will be added after it
     * is bound to an adapter position.
     * @param viewType  The view type of the new `View`.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.line_list_item, viewGroup, false)

        return ViewHolder(v)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the `View` held in the `ViewHolder viewHolder` parameter to reflect
     * the item at the given position `Int position`.
     *
     * We simply set the text in the `TextView` returned by the `getTextView` method of our
     * parameter `ViewHolder viewHolder` to the text of the verse pointed to by our parameter
     * `int position` in our dataset `ArrayList<String> mDataSet`.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        viewHolder.textView.text = mDataSet[position]
    }

    /**
     * Return the size of your data set (invoked by the layout manager). This is simply the size
     * of our `ArrayList<String> mDataSet` field.
     *
     * @return number of verses in the Bible
     */
    override fun getItemCount(): Int {
        return mDataSet.size
    }

    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "BibleAdapter"
        /**
         * Random number generator used for moveToRandom
         */
        private val rand = Random()
        /**
         * `RecyclerView.LayoutManager` passed to our constructor
         */
        lateinit var mLayoutManager: LinearLayoutManager
        /**
         * Verses of the King James Bible read in by the `initDataSet()` method of `BibleMain`,
         * it is set by our constructor to its `ArrayList<String> dataSet` parameter.
         */
        lateinit var mDataSet: ArrayList<String>
        /**
         * Chapter and verse Citation for each verse in our dataset `mDataSet`, it is set by our
         * constructor to its `ArrayList<String> chapterAndVerse` parameter.
         */
        lateinit var mChapterAndVerse: ArrayList<String>

        /**
         * Flag to prevent us from accessing our `numberToBook` map until initialization of it finishes
         */
        var numberToBookInitialized = false
        /**
         * Map of book names indexed by book number
         */
        lateinit var numberToBook: HashMap<String, String>
        /**
         * Array of book numbers
         */
        val numbers = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
                "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
                "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66")
        /**
         * Array of book names
         */
        val books = arrayOf("Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
                "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
                "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms",
                "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations",
                "Ezekiel", "Daniel", "Hosea", "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum",
                "Habakkuk", "Zephaniah", "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke",
                "John", "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
                "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
                "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John",
                "2 John", "3 John", "Jude", "Revelation")

        /**
         * Creates a Canonical Bible citation: "BookName:##:###:###" from the `String numChatVerse`
         * passed it. It keeps the book number in the citation since it might be of use to know the
         * relative position in the Bible. We grab the first two characters of our parameter
         * `String numChatVerse` (the book number "01" to "66") and use it to index into
         * `HashMap<String, String> numberToBook` to retrieve the book name corresponding to that
         * number, and then we return that book name concatenated to the front of our argument
         * `String numChatVerse` with a ":" to separate the two.
         *
         * @param numChatVerse ##:###:### index string for verse as read from text
         * @return BookName:##:###:###
         */
        fun makeCitation(numChatVerse: String): String {
            val bookNumber = numChatVerse.substring(0, 2)
            return numberToBook[bookNumber] + ":" + numChatVerse
        }

        /**
         * Move to a random verse in the Bible. First we calculate a random verse number `int selection`,
         * then we call the `moveToVerse` method to do the move.
         *
         * @param view Just included to give context for creating a Toast
         */
        fun moveToRandom(view: View) {
            val selection = abs(rand.nextInt()) % mDataSet.size
            moveToVerse(view, selection)
        }

        /**
         * Move to a specific verse in the Bible. First we check to make sure that the background
         * thread reading in the Bible has finished the task (the `doneReading` field of `BibleMain`
         * is true), and if not we block the current thread until the `ConditionVariable mDoneReading`
         * of `BibleMain` is opened or until 5000 milliseconds have passed. When we are sure that
         * our dataset has been read in we instruct the `LinearLayoutManager mLayoutManager` that
         * this adapter is using to scroll to the `int selection` verse number requested by calling
         * its `scrollToPositionWithOffset` method. Next we call the `saveVerseNumber` method
         * of `BibleMain` to save the verse number in our shared preference file. Then we make a
         * canonical Bible citation out of the `mChapterAndVerse` entry for the verse we moved to,
         * Toast this citation, save the citation in the `dialogTitle` field of `BibleMain`,
         * retrieve the verse text from `mDataSet` and save it in the `dialogText` field of
         * `BibleMain`, and save the verse number (`selection`) in the `dialogVerse` field of
         * `BibleMain`.
         *
         * @param view      Just used to call the `getContext()` method to get a `Context` for a Toast
         * @param selection Verse number 0 to `mDataSet.size()-1`.
         */
        fun moveToVerse(view: View, selection: Int) {
            // Make sure the BibleMain,init() thread has finished reading the text in
            if (!BibleActivity.doneReading) {
                BibleActivity.mDoneReading.block(5000)
            }
            mLayoutManager.scrollToPositionWithOffset(selection, 0)
            BibleActivity.saveVerseNumber(selection, BibleActivity.LAST_VERSE_VIEWED)
            val citation = makeCitation(mChapterAndVerse[selection])
            Toast.makeText(view.context, "Moving to $citation", Toast.LENGTH_LONG).show()

            BibleActivity.dialogTitle = citation
            BibleActivity.dialogText = mDataSet[selection]
            BibleActivity.dialogVerse = selection
        }
    }

}