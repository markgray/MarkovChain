package com.example.android.markovchain.shakespearemarkov

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R
import com.example.android.markovchain.util.DoneListener
import com.example.android.markovchain.util.Markov
import com.example.android.markovchain.util.MarkovAdapter
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.io.StringReader

/**
 * This `Activity` displays the random nonsense spouted by `Markov` when Shakespeare's
 * sonnets are used to create the Markov mChain state table that `Markov` uses.
 */
class ShakespeareMarkovActivity : FragmentActivity() {
    /**
     * [Markov] instance used to generate nonsense text
     */
    private var mMarkov = Markov()

    /**
     * [RecyclerView] in our layout file where our text is displayed.
     */
    private var mRecyclerView: RecyclerView? = null

    /**
     * [RecyclerView.LayoutManager] used by our [RecyclerView] as its `LayoutManager`
     */
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    /**
     * [MarkovAdapter] used by our [RecyclerView] field [mRecyclerView] to supply data for it to
     * display (a `RecyclerView.Adapter<MarkovAdapter.ViewHolder>`)
     */
    private lateinit var mAdapter: MarkovAdapter

    /**
     * First we call [enableEdgeToEdge] to enable edge to edge display, then we call our super's
     * implementation of `onCreate`, and cause our layout file
     * `R.layout.activity_shakespeare_markov_recycler`
     * to be inflated and set to be our content view.
     *
     * We initialize our [FrameLayout] variable `rootView` to the view with ID
     * `R.id.shakespeare_markovchain_root` then call [ViewCompat.setOnApplyWindowInsetsListener] to
     * take over the policy for applying window insets to `rootView`, with the `listener` argument
     * a lambda that accepts the [View] passed the lambda in variable `v` and the
     * [WindowInsetsCompat] passed the lambda in variable `windowInsets`. It initializes its
     * [Insets] variable `systemBars` to the [WindowInsetsCompat.getInsets] of `windowInsets` with
     * [WindowInsetsCompat.Type.systemBars] as the argument. It then gets the insets for the IME
     * (keyboard) using [WindowInsetsCompat.Type.ime]. It then updates the layout parameters of `v`
     * to be a [ViewGroup.MarginLayoutParams] with the left margin set to `systemBars.left`, the
     * right margin set to `systemBars.right`, the top margin set to `systemBars.top`, and the
     * bottom margin set to the maximum of the system bars bottom inset and the IME bottom inset.
     * Finally it returns [WindowInsetsCompat.CONSUMED] to the caller (so that the window insets
     * will not keep passing down to descendant views).
     *
     * Next we initialize our field [mLayoutManager] with an instance of [LinearLayoutManager] and
     * we initialize our field [mRecyclerView] by finding the [RecyclerView] with id
     * R.id.shakespeare_markov_recycler_view. Then we call our method [initMarkovState] to
     * initialize the Markov state table of our [Markov] field [mMarkov] by calling its `make`
     * method. We initialize our [MarkovAdapter] field [mAdapter] with a new instance of
     * [MarkovAdapter] which uses our [Markov] field [mMarkov] as its [Markov] instance. Then we
     * set the adapter of our [mRecyclerView] field to our field [mAdapter], and its `LayoutManager`
     * to our field [mLayoutManager].
     *
     * @param savedInstanceState since we do not override onSaveInstanceState we do not use this
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shakespeare_markov_recycler)
        val rootView = findViewById<FrameLayout>(R.id.shakespeare_markovchain_root)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v: View, windowInsets: WindowInsetsCompat ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

            // Apply the insets as a margin to the view.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = systemBars.left
                rightMargin = systemBars.right
                topMargin = systemBars.top
                bottomMargin = systemBars.bottom.coerceAtLeast(ime.bottom)
            }
            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
        mLayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView = findViewById(R.id.shakespeare_markov_recycler_view)

        initMarkovState()

        mAdapter = MarkovAdapter(supportFragmentManager, mMarkov)
        // Set CustomAdapter as the adapter for RecyclerView.
        (mRecyclerView ?: return).adapter = mAdapter
        (mRecyclerView ?: return).layoutManager = mLayoutManager
        Snackbar.make(
            mRecyclerView ?: return,
            "Long click a verse to hear it read",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null).show()
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
        }, mRecyclerView ?: return)

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
        const val TAG: String = "ShakespeareMarkovR..."
    }
}
