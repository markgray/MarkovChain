package com.example.android.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An implementation of RecyclerView.Adapter<MarkovAdapter.ViewHolder> designed to be used by an
 * Activity that needs to populate a RecyclerView using lines obtained from an instance of Markov
 */
public class MarkovAdapter extends RecyclerView.Adapter<MarkovAdapter.ViewHolder> {

    private static final String TAG = "MarkovAdapter"; // TAG used for Log
    private static final int NLINES = 10000; // Just used as an arbitrary number for getItemCount
    private Markov mMarkov; // The Markov instance we are reading from.
    private static FragmentManager mFragmentManager;

    /**
     * Constructor for this instance of MarkovAdapter. First we set {@code FragmentManager mFragmentManager}
     * to the handle to the the FragmentManager for interacting with fragments associated with the activity
     * using us that is passed us in our parameter {@code FragmentManager fragmentManager}. Then we set our
     * field {@code Markov mMarkov} to our parameter {@code Markov markov}.
     *
     * @param fragmentManager handle to the the FragmentManager for interacting with fragments associated
     *                        with the activity using us
     * @param markov          Markov class used to generate lines for the RecyclerView
     */
    public MarkovAdapter(FragmentManager fragmentManager, Markov markov) {
        mFragmentManager = fragmentManager;
        mMarkov = markov;
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder). Each ViewHolder
     * will be used to display an individual data item of the RecyclerView's data set, and should
     * contain any information needed by onBindViewHolder to more efficiently display the data for
     * the position called for (TextView textView, and {@code MarkovStats markovStats} in our case).
     */
    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView; // TextView to write our Markov chain generated lines to.
        private MarkovStats markovStats; // MarkovStats instance used to report number of possibilities

        /**
         * Constructor for our RecyclerView.ViewHolder to be used by onCreateViewHolder. First we
         * call our super's constructor.
         * <p>
         * Then we set our OnClickListener to an anonymous class which toasts the number of possibilities
         * for the text in our {@code View v} given the first two words of the generated verse. (As
         * determined by {@code Markov} and stored in {@code MarkovStats markovStats}).
         * <p>
         * Then we set our OnLongClickListener to an anonymous class which will create and display a
         * {@code MarkovDialog} {@code DialogFragment} containing the text in our {@code View v} given
         * the first two words of the generated verse. (As determined by {@code Markov} and stored in
         * {@code MarkovStats markovStats}).
         * <p>
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
                    Toast.makeText(v.getContext(), "Number of possibilities:\n" + getPossibles(), Toast.LENGTH_LONG).show();
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String possibles = getPossibles().toString();
                    String verse = (String) textView.getText();
                    MarkovDialog markovDialog = MarkovDialog.newInstance(possibles, verse);

                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    Fragment prev = mFragmentManager.findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    markovDialog.show(ft, "dialog");
                    return true;
                }
            });
            textView = (TextView) v.findViewById(R.id.vTextView);
            //noinspection UnnecessaryBoxing
            markovStats = new MarkovStats();
        }

        /**
         * Just a getter method for our field TextView textView
         *
         * @return The TextView textView field for this instance of ViewHolder
         */
        public TextView getTextView() {
            return textView;
        }

        public MarkovStats getPossibles() {
            return markovStats;
        }

        public void setPossibles(MarkovStats possibles) {
            markovStats = possibles;
        }
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     * <p>
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
     * <p>
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

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        if (mMarkov.chain.loaded) holder.getTextView().setText(mMarkov.line(holder.markovStats));
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
