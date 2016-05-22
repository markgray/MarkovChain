package com.example.android.common;

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
 * Adapter used by BibleFragment to populate the RecyclerView
 */
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.ViewHolder>  {
    private static final String TAG = "StringListAdapter";
    private static Random rand = new Random();
    private static ArrayList<String> mDataSet;
    private static LinearLayoutManager mLayoutManager;
    private static ArrayList<String> mChapterAndVerse;


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
                    Toast.makeText(v.getContext(), "Verse " + mChapterAndVerse.get(getLayoutPosition()), Toast.LENGTH_LONG).show();
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int selection = Math.abs(rand.nextInt()) % mDataSet.size();
                    mLayoutManager.scrollToPositionWithOffset(selection, 0);
                    Toast.makeText(view.getContext(), "Moving to verse " + mChapterAndVerse.get(selection), Toast.LENGTH_LONG).show();
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
     * Initialze the data used for the Adapter
     *
     * @param dataSet List containing the text
     * @param chapterAndVerse chapter and verse annotation for each paragraph
     * @param layoutManager layout manager to use
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

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.line_list_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
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
