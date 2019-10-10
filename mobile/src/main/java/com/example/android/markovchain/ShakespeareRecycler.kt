package com.example.android.markovchain

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

/**
 * This Activity lets you read Shakespeare's sonnets in a RecyclerView.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ShakespeareRecycler : Activity() {
    /**
     * The [RecyclerView] in our layout which display the text of Shakespeare's sonnets
     */
    lateinit var mRecyclerView: RecyclerView
    /**
     * [RecyclerView.LayoutManager] for our [RecyclerView] (a `LinearLayoutManager` instance)
     */
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    /**
     * Adapter for our [RecyclerView]
     */
    lateinit var mAdapter: StringArrayAdapter

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * `onCreate`, then we set our content view to our layout file R.layout.activity_shakespeare_recycler.
     * Next we initialize our [RecyclerView.LayoutManager] field [mLayoutManager] with an instance of
     * [LinearLayoutManager]. We locate the view with id R.id.shakespeare_recyclerview in order to
     * initialize our [RecyclerView] field [mRecyclerView] to it. We create a new instance of
     * [StringArrayAdapter] using the `SONNETS` field of our [Shakespeare] class (the `String[]` array
     * containing Shakespeare's sonnets) as its dataset, and [mLayoutManager] as its `LayoutManager`
     * and initialize our [StringArrayAdapter] field [mAdapter] to it. Finally we set the adapter of
     * our [RecyclerView] field [mRecyclerView] to [mAdapter], and set the layout manager for it to
     * our [RecyclerView.LayoutManager] field [mLayoutManager].
     *
     * @param savedInstanceState we do not override onSaveInstanceState so ignore this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shakespeare_recycler)

        mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView = findViewById(R.id.shakespeare_recyclerview)
        mAdapter = StringArrayAdapter(Shakespeare.SONNETS, mLayoutManager)
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        Log.i(TAG, "Verses read: " + Shakespeare.SONNETS.size)
    }

    companion object {
        /**
         * TAG for logging
         */
        const val TAG = "ShakespeareRecycler"
    }
}
