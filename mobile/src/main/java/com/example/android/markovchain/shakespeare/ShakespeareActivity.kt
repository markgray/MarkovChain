package com.example.android.markovchain.shakespeare

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R
import com.example.android.markovchain.util.StringArrayAdapter
import com.google.android.material.snackbar.Snackbar

/**
 * This Activity lets you read Shakespeare's sonnets in a RecyclerView.
 */
class ShakespeareActivity : FragmentActivity() {
    /**
     * The [RecyclerView] in our layout which display the text of Shakespeare's sonnets
     */
    private lateinit var mRecyclerView: RecyclerView

    /**
     * [RecyclerView.LayoutManager] for our [RecyclerView] (a `LinearLayoutManager` instance)
     */
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    /**
     * Adapter for our [RecyclerView]
     */
    private lateinit var mAdapter: StringArrayAdapter

    /**
     * First we call [enableEdgeToEdge] to enable edge to edge display, then we call our super's
     * implementation of `onCreate`, and set our content view to our layout file
     * `R.layout.activity_shakespeare_recycler`.
     * Next we initialize our [RecyclerView.LayoutManager] field [mLayoutManager] with an instance of
     * [LinearLayoutManager]. We locate the view with id R.id.shakespeare_recyclerview in order to
     * initialize our [RecyclerView] field [mRecyclerView] to it. We create a new instance of
     * [StringArrayAdapter] using the `SONNETS` field of our [ShakespeareSonnets] class (the `String[]` array
     * containing Shakespeare's sonnets) as its dataset, and [mLayoutManager] as its `LayoutManager`
     * and initialize our [StringArrayAdapter] field [mAdapter] to it. Finally we set the adapter of
     * our [RecyclerView] field [mRecyclerView] to [mAdapter], and set the layout manager for it to
     * our [RecyclerView.LayoutManager] field [mLayoutManager].
     *
     * @param savedInstanceState we do not override onSaveInstanceState so ignore this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shakespeare_recycler)

        mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView = findViewById(R.id.shakespeare_recyclerview)
        mAdapter = StringArrayAdapter(supportFragmentManager, ShakespeareSonnets.SONNETS, mLayoutManager)
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        Log.i(TAG, "Verses read: " + ShakespeareSonnets.SONNETS.size)
        Snackbar.make(mRecyclerView, "Long click a verse to hear it read", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    companion object {
        /**
         * TAG for logging
         */
        const val TAG: String = "ShakespeareRecycler"
    }
}
