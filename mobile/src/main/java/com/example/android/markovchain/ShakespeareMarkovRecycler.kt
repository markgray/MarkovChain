package com.example.android.markovchain

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.io.StringReader

/**
 * This `Activity` displays the random nonsense spouted by `Markov` when Shakespeare's
 * sonnets are used to create the Markov mChain state table that `Markov` uses.
 */
@Suppress("MemberVisibilityCanBePrivate")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
class ShakespeareMarkovRecycler : FragmentActivity() {
    /**
     * [Markov] instance used to generate nonsense text
     */
    var mMarkov = Markov()
    /**
     * [RecyclerView] in our layout file where our text is displayed.
     */
    lateinit var mRecyclerView: RecyclerView
    /**
     * [RecyclerView.LayoutManager] used by our [RecyclerView] as its `LayoutManager`
     */
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    /**
     * [MarkovAdapter] used by our [RecyclerView] field [mRecyclerView] to supply data for it to
     * display (a `RecyclerView.Adapter<MarkovAdapter.ViewHolder>`)
     */
    lateinit var mAdapter: MarkovAdapter

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * `onCreate`, then we cause our layout file R.layout.activity_shakespeare_markov_recycler
     * to be inflated and set to be our content view. Next we initialize our field [mLayoutManager]
     * with an instance of [LinearLayoutManager] and we initialize our field [mRecyclerView]
     * by finding the [RecyclerView] with id R.id.shakespeare_markov_recycler_view. Then we
     * call our method [initMarkovState] to initialize the Markov state table of our [Markov] field
     * [mMarkov] by calling its `make` method. We initialize our [MarkovAdapter] field [mAdapter]
     * with a new instance of [MarkovAdapter] which uses our [Markov] field [mMarkov] as its [Markov]
     * instance. Then we set the adapter of our [mRecyclerView] field to our field [mAdapter],
     * and its `LayoutManager` to our field [mLayoutManager].
     *
     * @param savedInstanceState since we do not override onSaveInstanceState we do not use this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shakespeare_markov_recycler)

        mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView = findViewById(R.id.shakespeare_markov_recycler_view)

        initMarkovState()

        mAdapter = MarkovAdapter(supportFragmentManager, mMarkov)
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = mLayoutManager
        Snackbar.make(mRecyclerView,"Long click a verse to hear it read", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    /**
     * Initialize the Markov state table of our [Markov] field [mMarkov] using the `String[]`
     * array containing Shakespeare's sonnets with all lowercase letters: the `SONNETS` field
     * of [ShakespeareSmall]. First we initialize our [StringBuilder] variable `val stringBuilder`
     * with a new instance and loop over all the `quotes` [String]'s in the `SONNETS` field of
     * [ShakespeareSmall] appending each `quotes` to `stringBuilder`. We set the `DoneListener` of
     * our [Markov] field [mMarkov] to an anonymous class which simply Toast's a different Toast
     * than the default implementation and then calls the `callOnClick` method of [mRecyclerView]
     * (which will be the `view: View` passed to `onDoneDo`) to "click" the `RecyclerView`.
     * TODO: refactor our mMarkov.make to use same technique as BibleMarkovFragment
     * Then wrapped in a try block intended to catch and log IOException we initialize the state
     * table in our [Markov] field [mMarkov] by using its `make` method on a [StringReader] created
     * from the [String] value of our [StringBuilder] `stringBuilder`.
     */
    private fun initMarkovState() {
        val stringBuilder = StringBuilder()
        for (quotes in ShakespeareSmall.SONNETS) {
            stringBuilder.append(quotes)
        }

        // TODO: do this as thread
        mMarkov.setDoneListener(object : DoneListener() {
            override fun onDoneDo(view: View) {
                view.callOnClick()
            }
        }, mRecyclerView)

        try {
            Log.i(TAG, "making mChain")
            mMarkov.make(StringReader(stringBuilder.toString()))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        /**
         * TAG used for logging
         */
        const val TAG = "ShakespeareMarkovR..."
    }
}
