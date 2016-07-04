package com.example.android.markovchain;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.ViewHolder> {
    private static final String TAG = "BibleAdapter";
    private static Random rand = new Random();
    private static ArrayList<String> mDataSet;
    private static LinearLayoutManager mLayoutManager;
    private static ArrayList<String> mChapterAndVerse;

    /**
     * Create a Canonical Bible citation: BookName:##:###:### from the numChatVerse passed it.
     * It keeps the book number in the citation since it might be of use to know the relative
     * position in the Bible.
     *
     * @param numChatVerse ##:###:### index string for verse as read from text
     * @return BookName:##:###:###
     */
    public static String makeCitation(String numChatVerse) {
        String bookNumber = numChatVerse.substring(0, 2);
        return numberToBook.get(bookNumber) + ":" + numChatVerse;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                    Toast.makeText(v.getContext(), makeCitation(mChapterAndVerse.get(getLayoutPosition())), Toast.LENGTH_LONG).show();
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ((BibleMain) view.getContext()).showDialog(makeCitation(mChapterAndVerse.get(getLayoutPosition())),
                            (String) textView.getText());
                    BibleMain.dialogVerse = getAdapterPosition();
                    return true;
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }


    }

    /**
     * Move to a random verse in the Bible
     *
     * @param view Just included to give context for creating a Toast
     */
    public static void moveToRandom(View view) {
        int selection = Math.abs(rand.nextInt()) % mDataSet.size();
        moveToVerse(view, selection);
    }

    /**
     * Move to a specific verse in the Bible
     *
     * @param view      Just used to getContext() for a Toast
     * @param selection Verse number 0 to mDataSet.size()
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
     * Initialze the data used for the Adapter
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

    static public boolean numberToBookInitialized = false;
    static public HashMap<String, String> numberToBook;
    static final String[] numbers = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
            "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "65", "66"
    };
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
     * Build HashMap mapping book number to book name
     *
     * @return The HashMap created (unneeded since numberToBook is also set in method
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
     * Create new views (invoked by the layout manager)
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
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
