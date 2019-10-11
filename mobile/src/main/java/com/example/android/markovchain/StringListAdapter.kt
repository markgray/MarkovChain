package com.example.android.markovchain

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.util.Random
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * This is the RecyclerView.Adapter used by our TranscendActivity to display the List of strings it
 * reads in its RecyclerView.
 */
class StringListAdapter
/**
 * Our constructor, just saves its parameters in their respective fields, and calls our super's
 * implementation of `setHasStableIds(true)` to indicate that we have stable ids.
 *
 * @param dataSet `List<String>` containing the data to populate views to be used by [RecyclerView].
 * @param layoutManager `LayoutManager` used by our `RecyclerView`
 */
(dataSet: List<String>, layoutManager: RecyclerView.LayoutManager)
    : RecyclerView.Adapter<StringListAdapter.ViewHolder>() {

    init {
        mDataSet = dataSet
        mLayoutManager = layoutManager as LinearLayoutManager
        super.setHasStableIds(true)
    }


    /**
     * Return the stable ID for the item at [position]. We just return the position passed.
     *
     * @param position Adapter position to query
     * @return the stable ID of the item at position
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     * The new ViewHolder will be used to display items of the adapter using [onBindViewHolder].
     * Since it will be re-used to display different items in the data set, it is a good idea to
     * cache references to sub views of the View to avoid unnecessary [View.findViewById] calls.
     *
     * We initialize our [View] variable `val v` with the view we construct by using the [LayoutInflater]
     * that the [LayoutInflater.from] method obtains from the `Context` of our [ViewGroup] parameter
     * [viewGroup] to inflate our item layout file R.layout.line_list_item using [viewGroup] for the
     * layout params without attaching to it. Then we return a new instance of [ViewHolder] constructed
     * to use `v` as its [View].
     *
     * @param viewGroup The [ViewGroup] into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType  The view type of the new [View].
     * @return A new [ViewHolder] that holds a [View] of the given view type.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.line_list_item, viewGroup, false)

        return ViewHolder(v)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the [ViewHolder.itemView] to reflect the item at
     * the given position.
     *
     * Note that unlike [ListView], RecyclerView will not call this method again if the position of
     * the item changes in the data set unless the item itself is invalidated or the new position
     * cannot be determined. For this reason, you should only use the [position] parameter while
     * acquiring the related data item inside this method and should not keep a copy of it. If you
     * need the position of an item later on (e.g. in a click listener), use [ViewHolder.getPosition]
     * which will have the updated position.
     *
     * We call the `getTextView` method of our [ViewHolder] parameter [viewHolder] to fetch the
     * [TextView] it holds, and set its text to the [String] in our `List<String>` dataset [mDataSet]
     * that is in position [position].
     *
     * @param viewHolder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position   The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        viewHolder.textView.text = mDataSet[position]

    }

    /**
     * Returns the total number of items in the data set hold by the adapter. We just return the size
     * of our `List<String>` dataset [mDataSet].
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return mDataSet.size
    }

    /**
     * `ViewHolder` class that our `Adapter` uses.
     */
    inner class ViewHolder
    /**
     * Our constructor. First we call our super's constructor. Then we set the `View v`
     * parameter's `OnLongClickListener` to our `StringListAdapter`'s field
     * `OnLongClickListener onLongClickListener` (which picks a random selection, instructs
     * the `LinearLayoutManager mLayoutManager` to scroll to that random selection, toasts
     * what it just did, and returns true to consume the event). Finally we initialize our field
     * `TextView textView` by finding the view with id R.id.vTextView in `v`.
     *
     * @param v `View` that we should hold
     */
    (v: View) : RecyclerView.ViewHolder(v) {

        /**
         * The `TextView` in the `View` that we hold which displays our items.
         *
         * @return current value of our instance's `TextView textView` field
         */
        val textView: TextView

        init {
            v.setOnLongClickListener(onLongClickListener)
            textView = v.findViewById(R.id.vTextView)
        }
    }

    companion object {
        /**
         * TAG used for logging
         */
        private const val TAG = "StringListAdapter"
        /**
         * Random number generator used to select a random verse
         */
        private val rand = Random()
        /**
         * Our data set.
         */
        lateinit var mDataSet: List<String>
        /**
         * `LinearLayoutManager` used by the `RecyclerView` we are the adapter for
         */
        lateinit var mLayoutManager: LinearLayoutManager

        /**
         * `OnLongClickListener` used by all of the views of our `ViewHolder`
         */
        private val onLongClickListener = View.OnLongClickListener { view ->
            /**
             * Called when any of our `View`'s are long clicked. We initialize our variable
             * `int selection` by choosing a random index into our adapter's `List<String>` dataset
             * [mDataSet] then call the `scrollToPositionWithOffset` method of our
             * field `LinearLayoutManager mLayoutManager` to have it scroll to position
             * `selection` and toast a message telling what we just did. Finally we return
             * true to the caller to consume the long click here.
             *
             * Parameter: [View] that was long clicked
             * @return true to consume the long click here
             */
            val selection = abs(rand.nextInt()) % mDataSet.size
            mLayoutManager.scrollToPositionWithOffset(selection, 0)
            Toast.makeText(view.context, "Moving to verse $selection", Toast.LENGTH_LONG).show()
            true
        }
    }

}
