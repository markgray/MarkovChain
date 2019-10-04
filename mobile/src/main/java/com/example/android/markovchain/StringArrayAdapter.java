package com.example.android.markovchain;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class StringArrayAdapter extends RecyclerView.Adapter<StringArrayAdapter.ViewHolder>  {
    /**
     * TAG used for logging
     */
    private static final String TAG = "StringArrayAdapter";
    /**
     * Random number generator used to select a random verse
     */
    private static Random rand = new Random();
    /**
     * Our data set.
     */
    private static String[] mDataSet;
    /**
     * {@code LinearLayoutManager} used by the {@code RecyclerView} we are the adapter for
     */
    private static LinearLayoutManager mLayoutManager;

    /**
     * Our constructor, just saves its parameters in their respective fields.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     * @param layoutManager {@code LayoutManager} used by our {@code RecyclerView}
     */
    public StringArrayAdapter(String[] dataSet, RecyclerView.LayoutManager layoutManager) {
        mDataSet = dataSet;
        mLayoutManager = (LinearLayoutManager) layoutManager;
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
     * We initialize our variable {@code View v} with the view we construct by using the {@code LayoutInflater}
     * that the {@code from} method of {@code LayoutInflater} obtains from the {@code Context} of our parameter
     * {@code ViewGroup viewGroup} to inflate our item layout file R.layout.line_list_item using {@code viewGroup}
     * for the layout params without attaching to it. Then we return a new instance of {@code ViewHolder} constructed
     * to use {@code v} as its {@code View}.
     *
     * @param viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType  The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
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
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <b>position</b> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     * <p>
     * We call the {@code getTextView} of our parameter {@code ViewHolder viewHolder} to fetch the
     * {@code TextView} it holds, and set its text to the {@code String} in our dataset {@code String[] mDataSet}
     * that is in position {@code position}.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     *                   item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet[position]);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter. We just return the length
     * of our dataset {@code String[] mDataSet}.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    /**
     * {@code ViewHolder} class that our {@code Adapter} uses.
     */
    @SuppressWarnings("WeakerAccess")
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The {@code TextView} in the {@code View} that we hold which displays our items.
         */
        private final TextView textView;

        /**
         * Our constructor. First we call our super's constructor. Then we set the {@code OnClickListener}
         * of our parameter {@code View v} to an anonymous class whose {@code onClick} override just logs
         * which position was clicked, and we set the {@code OnLongClickListener} to an anonymous class
         * which picks a random selection, instructs the {@code LinearLayoutManager mLayoutManager} to
         * scroll to that random selection, toasts what it just did, and returns true to consume the
         * event. Finally we initialize our field {@code TextView textView} by finding the view with
         * id R.id.vTextView in {@code v}.
         *
         * @param v {@code View} that we should hold
         */
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when the {@code View} we hold is clicked. We just log the adapter position
                 * of our {@code View}.
                 *
                 * @param v {@code View} that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getLayoutPosition() + " clicked.");
                }
            });
            // Define long click listener for the ViewHolder's View.
            v.setOnLongClickListener(new View.OnLongClickListener() {
                /**
                 * Called when the {@code View} we hold is long clicked. We initialize our variable
                 * {@code int selection} by choosing a random index into our adapter's dataset
                 * {@code String[] mDataSet} then call the {@code scrollToPositionWithOffset} of our
                 * field {@code LinearLayoutManager mLayoutManager} to have it scroll to position
                 * {@code selection} and toast a message telling what we just did. Finally we return
                 * true to the caller to consume the long click here.
                 *
                 * @param view {@code View} that was long clicked
                 * @return true to consume the long click here
                 */
                @Override
                public boolean onLongClick(View view) {
                    int selection = Math.abs(rand.nextInt()) % mDataSet.length;
                    mLayoutManager.scrollToPositionWithOffset(selection, 0);
                    Toast.makeText(view.getContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            textView = v.findViewById(R.id.vTextView);
        }

        /**
         * A getter for our instance's {@code TextView textView} field.
         *
         * @return current value of our instance's {@code TextView textView} field
         */
        public TextView getTextView() {
            return textView;
        }
    }

}
