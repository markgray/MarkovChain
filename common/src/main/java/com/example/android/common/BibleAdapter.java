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
import java.util.Random;

/**
 * Adapter used by BibleFragment to populate the RecyclerView
 */
public class BibleAdapter extends RecyclerView.Adapter<BibleAdapter.ViewHolder>  {
    private static final String TAG = "StringListAdapter";
    private static Random rand = new Random();
    private static ArrayList<String> mDataSet;
    private static LinearLayoutManager mLayoutManager;
    private final ArrayList<String> mChapterAndVerse;


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
                    Toast.makeText(v.getContext(), "Verse " + getLayoutPosition(), Toast.LENGTH_LONG).show();
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int selection = Math.abs(rand.nextInt()) % mDataSet.size();
                    mLayoutManager.scrollToPositionWithOffset(selection, 0);
                    Toast.makeText(view.getContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
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
