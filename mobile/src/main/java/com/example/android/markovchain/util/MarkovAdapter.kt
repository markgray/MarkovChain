package com.example.android.markovchain.util

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R

/**
 * An implementation of `RecyclerView.Adapter<MarkovAdapter.ViewHolder>` designed to be used by
 * an Activity that needs to populate a [RecyclerView] using lines obtained from an instance of
 * [Markov]
 */
@Suppress("MemberVisibilityCanBePrivate")
class MarkovAdapter
/**
 * Constructor for this instance of [MarkovAdapter]. First we set our [FragmentManager] field
 * [mFragmentManager] to the handle to the the `FragmentManager` for interacting with fragments
 * associated with the activity using us that is passed us in our [FragmentManager] parameter
 * `fragmentManager`. Then we set our [Markov] field [mMarkov] to our [Markov] parameter [mMarkov].
 *
 * @param fragmentManager handle to the the [FragmentManager] for interacting with fragments
 * associated with the activity using us
 * @param mMarkov         [Markov] class used to generate lines for the [RecyclerView]
 */
(fragmentManager: FragmentManager, val mMarkov: Markov) : RecyclerView.Adapter<MarkovAdapter.ViewHolder>() {

    init {
        mFragmentManager = fragmentManager
    }

    /**
     * Provides a reference to the type of views that you are using (custom `ViewHolder`). Each
     * `ViewHolder` will be used to display an individual data item of the `RecyclerView`'s
     * data set, and should contain any information needed by `onBindViewHolder` to more efficiently
     * display the data for the position called for (`TextView textView`, and `MarkovStats markovStats`
     * in our case).
     */
    class ViewHolder
    /**
     * Constructor for our `RecyclerView.ViewHolder` to be used by `onCreateViewHolder`.
     * First we call our super's constructor.
     *
     * Then we set the `OnClickListener` of our [View] parameter `v` to a lambda which toasts
     * the number of possibilities for the text in our `View v` given the first two words of
     * the generated verse (as determined by `Markov` and stored in `MarkovStats markovStats`).
     *
     * Then we set the `OnLongClickListener` of our [View] parameter `v` to a lambda which will
     * create and display a `MarkovDialog` `DialogFragment` containing the text in our `View v`
     * and the number of possibilities for that text given the first two words of the generated
     * verse. (As determined by `Markov` and stored in `MarkovStats markovStats`).
     *
     * Finally we find the [TextView] with id (R.id.vTextView) that we will use to later display the
     * line of generated nonsense to initialize our field [textView], and initialize our [MarkovStats]
     * field `markovStats` with a new instance.
     *
     * @param v [View] inflated by `onCreateViewHolder` for us to use to hold our current line.
     */
    (v: View) : RecyclerView.ViewHolder(v) {

        /**
         * `TextView` to write our Markov mChain generated lines to.
         */
        lateinit var textView: TextView
        /**
         * `MarkovStats` instance used to report number of possibilities for each line
         */
        var possibles: MarkovStats? = null

        init {
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener { view ->
                /**
                 * Called when our parameter's `View view` has been clicked. We just toast a message
                 * displaying the number of possible lines that could have been created given the first
                 * two words of our current line. We just toast a string formed by concatenating the
                 * string "Number of possibilities:\n" followed by the string value of our field
                 * `MarkovStats markovStats` (its `toString()` method formats the statistics
                 * stored in it by `Markov` into a String which it returns).
                 *
                 * @param view `View` that was clicked
                 */
                Log.d(TAG, "Element $layoutPosition clicked.")
                Toast.makeText(view.context, "Number of possibilities:\n" + possibles!!, Toast.LENGTH_LONG).show()
            }
            // Define OnLongClickListener for the ViewHolder's View.
            v.setOnLongClickListener {
                val verse = textView.text as String
                val speechDialog = SpeechDialog.newInstance(verse)
                val ft = mFragmentManager.beginTransaction()
                val prev = mFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                speechDialog.show(ft, "dialog")
                true
            }
            textView = v.findViewById(R.id.vTextView)
            possibles = MarkovStats()
        }
    }

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent an item.
     * This new `ViewHolder` should be constructed with a new [View] that can represent
     * the items of the given type. You can either create a new `View` manually or inflate it
     * from an XML layout file.
     *
     * The new ViewHolder will be used to display items of the adapter using [onBindViewHolder].
     * Since it will be re-used to display different items in the data set, it is a good idea to
     * cache references to sub views of the View to avoid unnecessary [View.findViewById] calls.
     *
     * We simply initialize our [View] variable `val v` by using the [LayoutInflater] from the context
     * of our [ViewGroup] parameter [parent] to inflate our item layout file R.layout.line_list_item
     * into it, then return a [ViewHolder] constructed using this `View v`.
     *
     * @param parent   The [ViewGroup] into which the new [View] will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new [View].
     * @return A new [ViewHolder] that holds a [View] of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view.
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.line_list_item, parent, false)

        return ViewHolder(v)
    }

    /**
     * Called by [RecyclerView] to display the data at the specified position. This method
     * should update the contents of the [ViewHolder.itemView] to reflect the item at
     * the given position.
     *
     * Note that unlike [ListView], [RecyclerView] will not call this method again if the position
     * of the item changes in the data set unless the item itself is invalidated or the new position
     * cannot be determined. For this reason, you should only use the [position] parameter while
     * acquiring the related data item inside this method and should not keep a copy of it. If you
     * need the position of an item later on (e.g. in a click listener), use [ViewHolder.getPosition]
     * which will have the updated position.
     *
     * We first check to see if our Markov mChain has finished being loaded (or built) and if so
     * set the text in our [ViewHolder] parameter [holder] to the sentence of random nonsense
     * generated by calling the `line` method of the [MarkovAdapter] field `Markov mMarkov`.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//      Log.d(TAG, "Element $position set.")

        // Get element from your data set at this position and replace the contents of the view
        // with that element
        if (mMarkov.mChain.loaded) holder.textView.text = mMarkov.line(holder.possibles!!)
    }

    /**
     * Returns the total number of items in the data set held by the adapter. We just return the
     * arbitrary number NLINES since Markov starts over at the beginning of its state table whenever
     * it encounters the end of the table thus generating an infinite number of random lines.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return NLINES
    }

    /**
     * Our static constants and variables.
     */
    companion object {

        /**
         * TAG used for Logging
         */
        private const val TAG = "MarkovAdapter"
        /**
         * Just used as an arbitrary number for getItemCount
         */
        private const val NLINES = 10000
        /**
         * Handle to the the `FragmentManager` for interacting with fragments associated with the
         * activity that is using us.
         */
        private lateinit var mFragmentManager: FragmentManager
    }
}
