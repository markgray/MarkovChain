package com.example.android.markovchain;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Adapter used by BibleMain to populate the RecyclerView
 */
@SuppressWarnings("WeakerAccess")
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.ViewHolder> {
    private static final String TAG = "BibleAdapter"; // TAG used for logging
    private static Random rand = new Random(); // Random number generator used for moveToRandom
    private static LinearLayoutManager mLayoutManager; // RecyclerView.LayoutManager passed to our constructor
    private static ArrayList<String> mChapterAndVerse; // Citation for each verse
    private static ArrayList<String> mDataSet; // Verses of the King James Bible read in by BibleMain.initDataSet()
// TODO: Move all fields to beginning of class?

    /**
     * Initialize the data used for the Adapter. We save our parameters ArrayList<String> dataSet in
     * our field ArrayList<String> mDataSet, ArrayList<String> chapterAndVerse in our field
     * ArrayList<String> mChapterAndVerse, and RecyclerView.LayoutManager layoutManager in our field
     * LinearLayoutManager mLayoutManager. Then we call initializeNumberToBook to initialize our
     * map of book names indexed by the book number: HashMap<String, String> numberToBook.
     *
     * @param dataSet         List containing the text
     * @param chapterAndVerse chapter and verse annotation for each paragraph
     * @param layoutManager   layout manager to use
     */
    public BibleAdapter(ArrayList<String> dataSet, ArrayList<String> chapterAndVerse, RecyclerView.LayoutManager layoutManager) {
        mDataSet = dataSet;
        mChapterAndVerse = chapterAndVerse;
        mLayoutManager = (LinearLayoutManager) layoutManager;
        initializeNumberToBook();
    }

    static public boolean numberToBookInitialized = false; // Prevent us from accessing our map until initialization finishes
    static public HashMap<String, String> numberToBook; // Map of book names indexed by book number
    static final String[] numbers = {  // Array of book numbers
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66"
    };
    static final String[] books = {  // Array of book names
            "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
            "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings",
            "1 Chronicles", "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job",
            "Psalms", "Proverbs", "Ecclesiastes", "Song of Solomon", "Isaiah",
            "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea", "Joel",
            "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah",
            "Haggai", "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John",
            "Acts", "Romans", "1 Corinthians", "2 Corinthians", "Galatians",
            "Ephesians", "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians",
            "1 Timothy", "2 Timothy", "Titus", "Philemon", "Hebrews", "James", "1 Peter",
            "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"
    };

    /**
     * Build HashMap mapping book number to book name. If the number to name map is already
     * initialized (numberToBookInitialized is true) we simply return the current value of
     * HashMap<String, String> numberToBook, otherwise we allocate space on the heap for
     * HashMap<String, String> numberToBook and then loop through our String[] numbers
     * array and "put" the corresponding entry in the String[] books array into numberToBook
     * indexed by the entry in the entry in numbers. When done we set numberToBookInitialized
     * to true and return the initialized numberToBook
     *
     * @return The HashMap created (unneeded since numberToBook is also set in the method, but it
     *          does give the use of the method additional flexibility)
     */
    public HashMap<String, String> initializeNumberToBook() {
        if (!numberToBookInitialized) {
            numberToBook = new HashMap<>(66);
            for (int i = 0; i < numbers.length; i++) {
                numberToBook.put(numbers[i], books[i]);
            }
            numberToBookInitialized = true;
        }

        return numberToBook;
    }

    /**
     * Create a Canonical Bible citation: BookName:##:###:### from the numChatVerse passed it.
     * It keeps the book number in the citation since it might be of use to know the relative
     * position in the Bible. We grab the first two characters of our parameter String numChatVerse
     * (the book number "01" to "66") and use it to index into HashMap<String, String> numberToBook
     * to retrieve the book name corresponding to that number, and then we return that book name
     * concatenated to the front of our argument String numChatVerse with a ":" to separate the two.
     *
     * @param numChatVerse ##:###:### index string for verse as read from text
     * @return BookName:##:###:###
     */
    public static String makeCitation(String numChatVerse) {
        String bookNumber = numChatVerse.substring(0, 2);
        return numberToBook.get(bookNumber) + ":" + numChatVerse;
    }

    /**
     * Provides a reference to the type of views that we are using (custom ViewHolder), and also
     * allows us to store additional information about our content.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView vTextView; // TextView in our layout to use for the verse we display

        /**
         * Initializes a new instance of a ViewHolder. Sets the onClickListener to Toast the
         * canonical Bible citation for the verse that the view will hold, and sets the
         * onLongClickListener to launch a BibleDialog DialogFragment for the verse. Finally it
         * finds the TextView for displaying the verse and squirrels it away in the vTextView field
         * for later use.
         *
         * @param v View that this is the ViewHolder for
         */
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                /**
                 * Create a Toast of the canonical Bible citation for the verse contained
                 * in the View that was clicked
                 *
                 * @param v View that was clicked
                 */
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                    Toast.makeText(v.getContext(), makeCitation(mChapterAndVerse.get(getLayoutPosition())), Toast.LENGTH_LONG).show();
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Pops up a BibleDialog DialogFragment containing the citation and text of the verse
                 * contained in the View that was long clicked, and sets BibleMain.dialogVerse to
                 * point to the verse index number (which is also the adapter position.)
                 *
                 * @param view View that was long clicked
                 * @return true because the callback consumed the long click
                 */
                @Override
                public boolean onLongClick(View view) {

                    ((BibleMain) view.getContext()).showDialog(makeCitation(mChapterAndVerse.get(getLayoutPosition())),
                            (String) vTextView.getText());
                    BibleMain.dialogVerse = getAdapterPosition();
                    return true;
                }
            });
            vTextView = (TextView) v.findViewById(R.id.vTextView);
        }

        /**
         * Gets the value contained in the TextView vTextView field
         *
         * @return the TextView vTextView field
         */
        public TextView getTextView() {
            return vTextView;
        }


    }

    /**
     * Move to a random verse in the Bible. First we calculate a random verse number, then we call
     * moveToVerse to do the move.
     *
     * @param view Just included to give context for creating a Toast
     */
    public static void moveToRandom(View view) {
        int selection = Math.abs(rand.nextInt()) % mDataSet.size();
        moveToVerse(view, selection);
    }

    /**
     * Move to a specific verse in the Bible. First we check to make sure that the background thread
     * reading in the Bible has finished the task, and if not we block the current thread until the
     * ConditionVariable mDoneReading condition is opened or until 5000 milliseconds have passed.
     * Then we instruct the LinearLayoutManager mLayoutManager that this adapter is using to
     * scrollToPositionWithOffset to the selection verse number requested. Next we call
     * BibleMain.saveVerseNumber to save the verse number in our shared preference file. Then we
     * make a canonical Bible citation out of the mChapterAndVerse entry for the verse we moved to,
     * Toast this citation, save the citation in BibleMain.dialogTitle, retrieve the verse text from
     * mDataSet and save it in BibleMain.dialogText, and save the verse number (selection) in
     * BibleMain.dialogVerse
     *
     * @param view      Just used to getContext() for a Toast
     * @param selection Verse number 0 to mDataSet.size() - 1.
     */
    public static void moveToVerse(View view, int selection) {
        // Make sure the BibleMain,init() thread has finished reading the text in
        if (!BibleMain.doneReading) {
            BibleMain.mDoneReading.block(5000);
        }
        mLayoutManager.scrollToPositionWithOffset(selection, 0);
        BibleMain.saveVerseNumber(selection, BibleMain.LAST_VERSE_VIEWED);
        final String citation = makeCitation(mChapterAndVerse.get(selection));
        Toast.makeText(view.getContext(), "Moving to " + citation, Toast.LENGTH_LONG).show();

        BibleMain.dialogTitle = citation;
        BibleMain.dialogText = mDataSet.get(selection);
        BibleMain.dialogVerse = selection;
    }

    /**
     * Create new views (invoked by the layout manager). We create a new View v by inflating the
     * layout file for our items: R.layout.line_list_item, and return a ViewHolder constructed using
     * View v.
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType  The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.line_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     *
     * Note that unlike ListView, RecyclerView will not call this method again if the position of
     * the item changes in the data set unless the item itself is invalidated or the new position
     * cannot be determined. For this reason, you should only use the position parameter while
     * acquiring the related data item inside this method and should not keep a copy of it.
     * If you need the position of an item later on (e.g. in a click listener),
     * use getAdapterPosition() which will have the updated adapter position.
     * Override onBindViewHolder(ViewHolder, int, List) instead if Adapter can handle
     * efficient partial bind
     *
     * We simply set the TextView in the layout file used for the ViewHolder viewholder to the
     * text of the verse pointed to by int position.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position));
    }

    /**
     * Return the size of your data set (invoked by the layout manager). This is simple the size
     * of ArrayList<String> mDataSet.
     *
     * @return number of verses in the Bible
     */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
