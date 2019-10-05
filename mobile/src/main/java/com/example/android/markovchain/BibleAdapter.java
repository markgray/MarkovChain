package com.example.android.markovchain;

import androidx.annotation.NonNull;
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
 * Adapter used by {@code BibleMain} to populate its {@code RecyclerView}
 */
@SuppressWarnings("WeakerAccess")
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.ViewHolder> {
    /**
     * TAG used for logging
     */
    private static final String TAG = "BibleAdapter";
    /**
     * Random number generator used for moveToRandom
     */
    private static Random rand = new Random();
    /**
     * {@code RecyclerView.LayoutManager} passed to our constructor
     */
    private static LinearLayoutManager mLayoutManager;
    /**
     * Verses of the King James Bible read in by the {@code initDataSet()} method of {@code BibleMain},
     * it is set by our constructor to its {@code ArrayList<String> dataSet} parameter.
     */
    private static ArrayList<String> mDataSet;
    /**
     * Chapter and verse Citation for each verse in our dataset {@code mDataSet}, it is set by our
     * constructor to its {@code ArrayList<String> chapterAndVerse} parameter.
     */
    private static ArrayList<String> mChapterAndVerse;

    /**
     * Flag to prevent us from accessing our {@code numberToBook} map until initialization of it finishes
     */
    static public boolean numberToBookInitialized = false;
    /**
     * Map of book names indexed by book number
     */
    static public HashMap<String, String> numberToBook;
    /**
     * Array of book numbers
     */
    static final String[] numbers = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66"
    };
    /**
     * Array of book names
     */
    static final String[] books = {
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
     * Our constructor. We save our parameter {@code ArrayList<String> dataSet} in our field
     * {@code ArrayList<String> mDataSet}, our parameter {@code ArrayList<String> chapterAndVerse}
     * in our field {@code ArrayList<String> mChapterAndVerse}, and our parameter
     * {@code RecyclerView.LayoutManager layoutManager} in our field {@code LinearLayoutManager mLayoutManager}.
     * Then we call our {@code initializeNumberToBook} method to initialize our map of book names indexed
     * by the book number: {@code HashMap<String, String> numberToBook}.
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

    /**
     * Builds a {@code HashMap} mapping book number to book name. If the number to name map is already
     * initialized ({@code numberToBookInitialized} is true) we simply return the current value of
     * {@code HashMap<String, String> numberToBook}, otherwise we allocate space on the heap for
     * {@code HashMap<String, String> numberToBook} and then loop through our {@code String[] numbers}
     * array and "put" the corresponding entry in the {@code String[] books} array into {@code numberToBook}
     * indexed by the entry in {@code numbers}. When done we set {@code numberToBookInitialized} to true
     * and return the initialized {@code numberToBook} to the caller.
     *
     * @return The HashMap created (unneeded since numberToBook is also set in the method, but it
     *          does give the use of the method additional flexibility)
     */
    @SuppressWarnings("UnusedReturnValue")
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
     * Creates a Canonical Bible citation: "BookName:##:###:###" from the {@code String numChatVerse}
     * passed it. It keeps the book number in the citation since it might be of use to know the relative
     * position in the Bible. We grab the first two characters of our parameter {@code String numChatVerse}
     * (the book number "01" to "66") and use it to index into {@code HashMap<String, String> numberToBook}
     * to retrieve the book name corresponding to that number, and then we return that book name
     * concatenated to the front of our argument {@code String numChatVerse} with a ":" to separate
     * the two.
     *
     * @param numChatVerse ##:###:### index string for verse as read from text
     * @return BookName:##:###:###
     */
    public static String makeCitation(String numChatVerse) {
        String bookNumber = numChatVerse.substring(0, 2);
        return numberToBook.get(bookNumber) + ":" + numChatVerse;
    }

    /**
     * Provides a reference to the type of views that we are using (custom {@code ViewHolder}), and
     * also allows us to store additional information about our content.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * {@code TextView} in our layout to use for the verse we display
         */
        private final TextView vTextView;

        /**
         * Our constructor. First we call our super's constructor. Then we set the {@code onClickListener}
         * of our parameter {@code View v} to an anonymous class whose {@code onClick} override toasts
         * the canonical Bible citation for the verse that the view will hold, and sets its
         * {@code onLongClickListener} to an anonymous class which will launch a {@code BibleDialog DialogFragment}
         * for the verse. Finally we find the {@code TextView} with id R.id.vTextView in {@code v} that
         * is to be used for displaying the verse and squirrel it away in our {@code TextView vTextView}
         * field for later use.
         *
         * @param v View that this is the ViewHolder for
         */
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                /**
                 * Creates a Toast of the canonical Bible citation for the verse contained
                 * in the {@code View} that was clicked.
                 *
                 * @param v View that was clicked
                 */
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                    Toast.makeText(v.getContext(), makeCitation(mChapterAndVerse.get(getLayoutPosition())), Toast.LENGTH_LONG).show();
                }
            });
            // Define long click listener for the ViewHolder's View.
            v.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Pops up a {@code BibleDialog DialogFragment} containing the citation and text of
                 * the verse contained in the View that was long clicked, and sets the {@code dialogVerse}
                 * field of {@code BibleMain} to point to the verse index number (which is also the adapter
                 * position.) Finally we return true to consume the long click here.
                 *
                 * @param view View that was long clicked
                 * @return true because the callback consumed the long click
                 */
                @Override
                public boolean onLongClick(View view) {

                    ((BibleMain) view.getContext()).showDialog(makeCitation(mChapterAndVerse.get(getLayoutPosition())),
                            (String) vTextView.getText());
                    BibleMain.Companion.setDialogVerse(getAdapterPosition());
                    return true;
                }
            });
            vTextView = v.findViewById(R.id.vTextView);
        }

        /**
         * Getter for the value contained in the {@code TextView vTextView} field.
         *
         * @return the current contents of the {@code TextView vTextView} field
         */
        public TextView getTextView() {
            return vTextView;
        }


    }

    /**
     * Move to a random verse in the Bible. First we calculate a random verse number {@code int selection},
     * then we call the {@code moveToVerse} method to do the move.
     *
     * @param view Just included to give context for creating a Toast
     */
    public static void moveToRandom(View view) {
        int selection = Math.abs(rand.nextInt()) % mDataSet.size();
        moveToVerse(view, selection);
    }

    /**
     * Move to a specific verse in the Bible. First we check to make sure that the background thread
     * reading in the Bible has finished the task (the {@code doneReading} field of {@code BibleMain}
     * is true), and if not we block the current thread until the {@code ConditionVariable mDoneReading}
     * of {@code BibleMain} is opened or until 5000 milliseconds have passed. When we are sure that
     * our dataset has been read in we instruct the {@code LinearLayoutManager mLayoutManager} that
     * this adapter is using to scroll to the {@code int selection} verse number requested by calling
     * its {@code scrollToPositionWithOffset} method. Next we call the {@code saveVerseNumber} method
     * of {@code BibleMain} to save the verse number in our shared preference file. Then we make a
     * canonical Bible citation out of the {@code mChapterAndVerse} entry for the verse we moved to,
     * Toast this citation, save the citation in the {@code dialogTitle} field of {@code BibleMain},
     * retrieve the verse text from {@code mDataSet} and save it in the {@code dialogText} field of
     * {@code BibleMain}, and save the verse number ({@code selection}) in the {@code dialogVerse}
     * field of {@code BibleMain}.
     *
     * @param view      Just used to call the {@code getContext()} to get a {@code Context} for a Toast
     * @param selection Verse number 0 to {@code mDataSet.size()-1}.
     */
    public static void moveToVerse(View view, int selection) {
        // Make sure the BibleMain,init() thread has finished reading the text in
        if (!BibleMain.Companion.getDoneReading()) {
            BibleMain.Companion.getMDoneReading().block(5000);
        }
        mLayoutManager.scrollToPositionWithOffset(selection, 0);
        BibleMain.Companion.saveVerseNumber(selection, BibleMain.LAST_VERSE_VIEWED);
        final String citation = makeCitation(mChapterAndVerse.get(selection));
        Toast.makeText(view.getContext(), "Moving to " + citation, Toast.LENGTH_LONG).show();

        BibleMain.Companion.setDialogTitle(citation);
        BibleMain.Companion.setDialogText(mDataSet.get(selection));
        BibleMain.Companion.setDialogVerse(selection);
    }

    /**
     * Create new views (invoked by the layout manager). We create a new {@code View v} by using the
     * {@code LayoutInflater} for the {@code Context} of our parameter {@code ViewGroup viewGroup} to
     * inflate tje layout file for our items: R.layout.line_list_item, and return a {@code ViewHolder}
     * constructed using this {@code v}.
     *
     * @param viewGroup The {@code ViewGroup} into which the new {@code View} will be added after it
     *                  is bound to an adapter position.
     * @param viewType  The view type of the new {@code View}.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.line_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should update
     * the contents of the {@code View} held in the {@code ViewHolder viewHolder} parameter to reflect
     * the item at the given position {@code int position}.
     * <p>
     * We simply set the text in the {@code TextView} returned by the {@code getTextView} method of our
     * parameter {@code ViewHolder viewHolder} to the text of the verse pointed to by our parameter
     * {@code int position} in our dataset {@code ArrayList<String> mDataSet}.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position));
    }

    /**
     * Return the size of your data set (invoked by the layout manager). This is simply the size
     * of our {@code ArrayList<String> mDataSet} field.
     *
     * @return number of verses in the Bible
     */
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
