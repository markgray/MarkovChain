package com.example.android.common;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * An implementation of RecyclerView.Adapter<MarkovAdapter.ViewHolder> designed to be used by an
 * Activity that needs to populate a RecyclerView using lines obtained from an instance of Markov
 */
public class MarkovAdapter extends RecyclerView.Adapter<MarkovAdapter.ViewHolder> {

    private static final String TAG = "MarkovAdapter"; // TAG used for Log
    private static final int NLINES = 10000; // Just used as an arbitrary number for getItemCount
    private static Random rand = new Random(); // Used to choose a random line to go to (a rather silly concept) TODO: Remove
    private Markov mMarkov; // The Markov instance we are reading from.
    private static LinearLayoutManager mLayoutManager; // LayoutManager instance used by the RecyclerView

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder). Each ViewHolder
     * will be used to display an individual data item of the RecyclerView's data set, and should
     * contain any information needed by onBindViewHolder to more efficiently display the data for
     * the position called for (TextView textView in our case).
     */
    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView; // TextView to write our Markov chain generated lines to.
        private Integer possibles; //

        /**
         * Constructor for our RecyclerView.ViewHolder to be used by onCreateViewHolder. First we
         * call our super's constructor.
         *
         * Then we set our OnClickListener to a placeholder anonymous class which simply logs the
         * current layout position.
         * TODO: Make this do something useful like Display the odds of that combination of words occurring
         *
         * Then we set our OnLongClickListener to a placeholder anonymous class which simply "moves"
         * to a random verse, and shows a Toast to that effect.
         * TODO: Pop up a DialogFragment that allows us to explore all variations starting from first two words.
         *
         * Finally we find the TextView textView (R.id.vTextView) that we will use to later to display
         * the line of generated nonsense.
         *
         * @param v View inflated by onCreateViewHolder for us to use for our current line
         */
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick (View view) {
                    int selection = Math.abs(rand.nextInt()) % NLINES;
                    mLayoutManager.scrollToPositionWithOffset(selection, 0);
                    Toast.makeText(view.getContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            textView = (TextView) v.findViewById(R.id.vTextView);
            possibles = 1;
        }

        /**
         * Just a getter method for our field TextView textView
         *
         * @return The TextView textView field for this instance of ViewHolder
         */
        public TextView getTextView() {
            return textView;
        }

        public Integer getPossibles() {
            return possibles;
        }

        public void setPossibles(Integer possibles) {
            this.possibles = possibles;
        }
    }

    /**
     * Constructor for this instance of MarkovAdapter. We set our field Markov mMarkov to our
     * parameter Markov markov, and our field LinearLayoutManager mLayoutManager to our parameter
     * RecyclerView.LayoutManager layoutManager.
     *
     * @param markov Markov class used to generate lines for the RecyclerView
     * @param layoutManager LayoutManager to be used by the RecyclerView
     */
    public MarkovAdapter(Markov markov, RecyclerView.LayoutManager layoutManager) {
        mMarkov = markov;
        mLayoutManager = (LinearLayoutManager) layoutManager;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * We simply create a View v by inflating our item layout file R.layout.line_list_item, then
     * return a ViewHolder created using this View v.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.line_list_item, parent, false);

        return new ViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <b>position</b> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     *
     * We first check to see if our Markov chain has finished being loaded (or built) and if so
     * set the text in ViewHolder holder to the sentence of random nonsense generated by calling
     * Markov.line().
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        Integer possibles = 1;
        // Get element from your data set at this position and replace the contents of the view
        // with that element
        if (mMarkov.chain.loaded) holder.getTextView().setText(mMarkov.line(possibles));
        holder.setPossibles(possibles);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter. We just return an
     * arbitrary number since Markov starts over at the beginning of its state table whenever
     * it encounters the end of the table thus generating an infinite number of random lines.
     * TODO: ponder returning a more useful value.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return NLINES;
    }
}
