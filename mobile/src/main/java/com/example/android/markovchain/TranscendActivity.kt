package com.example.android.markovchain

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This `Activity` loads text files from the raw resources of the app in the background, and
 * displays them in a `RecyclerView`.
 */
@Suppress("MemberVisibilityCanBePrivate")
class TranscendActivity : Activity() {
    /**
     * `RecyclerView` used to display our books
     */
    lateinit var transcendRecyleView: RecyclerView
    /**
     * `StringListAdapter` we use for our `RecyclerView transcendRecyleView`
     */
    lateinit var transcendAdapter: StringListAdapter
    /**
     * `LayoutManager` for our `RecyclerView` (a `LinearLayout` instance)
     */
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    /**
     * `TextView` used to display "Waiting for data to load…" message while waiting
     */
    lateinit var transcendWaiting: TextView
    /**
     * `LinearLayout` that we add our book selection `Button`s to.
     */
    lateinit var transcendBooks: LinearLayout
    /**
     * `ScrollView` that holds the `LinearLayout transcendBooks`
     */
    lateinit var transcendBooksScrollView: ScrollView

    /**
     * Called when the activity is starting. First we call our super's implementation of `onCreate`,
     * then we set our content view to our layout file R.layout.activity_transcend. We initialize our
     * field `RecyclerView.LayoutManager mLayoutManager` with a new `LinearLayoutManager`
     * instance, initialize our field `LinearLayout transcendBooks` by finding the view with
     * id R.id.transcend_books, initialize our field `ScrollView transcendBooksScrollView` by
     * finding the view with id R.id.transcend_books_scrollView, initialize our field
     * `RecyclerView transcendRecyleView` by finding the view with id R.id.transcend_recycle_view,
     * and initialize our field `TextView transcendWaiting` by finding the view with id
     * R.id.transcend_waiting. Then we loop over `int i` for all the resource id's in the array
     * `int[] resourceIDS` calling our method `addButton` to add a button to our field
     * `transcendBooks` with the label `titles[ i ]` which will load and display the raw
     * text file whose resource id is `resourceIDS[ i]` when the button is clicked.
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcend)

        mLayoutManager = LinearLayoutManager(applicationContext)
        transcendBooks = findViewById(R.id.transcend_books)
        transcendBooksScrollView = findViewById(R.id.transcend_books_scrollView)
        transcendRecyleView = findViewById(R.id.transcend_recycle_view)
        transcendWaiting = findViewById(R.id.transcend_waiting)
        for (i in resourceIDS.indices) {
            addButton(resourceIDS[i], titles[i], transcendBooks)
        }
    }

    /**
     * Adds a `Button` to its parameter `ViewGroup parent` whose label is given by its
     * parameter `String description` and whose `OnClickListener` sets the visibility of
     * the `ScrollView transcendBooksScrollView` that holds our Books selection UI to GONE and
     * calls our method `loadResourceTextFile` to have it load and display the resource file
     * with id `int resourceID` in the background.
     *
     * @param resourceID  resource ID that our button's `OnClickListener` should call the method
     * `loadResourceTextFile` to load in the background.
     * @param description Label for our `Button`
     * @param parent      `ViewGroup` we should add our `Button` to.
     */
    fun addButton(resourceID: Int, description: String, parent: ViewGroup) {
        val button = Button(this)
        button.text = description
        /**
         * Called when the `Button` is clicked. We just set the visibility of our field
         * `ScrollView transcendBooksScrollView` to GONE (disappears our Books selection
         * buttons), set the visibility of our field `TextView transcendWaiting` to VISIBLE
         * and call our method `loadResourceTextFile` to load and display the file whose
         * resource ID is that given by the `addButton` method's parameter `resourceID`.
         *
         * Parameter: `View` that was clicked.
         */
        button.setOnClickListener {
            transcendBooks.visibility = View.GONE
            transcendBooksScrollView.visibility = View.GONE
            transcendWaiting.visibility = View.VISIBLE
            loadResourceTextFile(resourceID)
        }
        parent.addView(button)
    }

    /**
     * Causes the utf8 text file with resource ID `int resourceID` to be read in by a background
     * task, and then displays the `List<String> results` the task returns in our field
     * `RecyclerView transcendRecyleView`.
     *
     * @param resourceID resource ID of the raw file we are to read in the background and then display
     * in `RecyclerView transcendRecyleView` once the background task is done.
     */
    private fun loadResourceTextFile(resourceID: Int) {
        @SuppressLint("StaticFieldLeak")
        val mtranscendDataTask = object : TranscendDataTask(applicationContext) {
            /**
             * Runs on the UI thread after [.doInBackground]. The parameter
             * `List<String> results` is the value returned by [.doInBackground].
             * We initialize our field `StringListAdapter transcendAdapter` with a new instance
             * which will use our parameter `List<String> results` as its data set, and our field
             * `RecyclerView.LayoutManager mLayoutManager` as its `LayoutManager`, set the
             * adapter of `RecyclerView transcendRecyleView` to `transcendAdapter` and set
             * the `LayoutManager` that `transcendRecyleView` will use to be our field
             * `mLayoutManager`. Finally we set the visibility of our field `TextView transcendWaiting`
             * to GONE, and set the visibility of `transcendRecyleView` to VISIBLE.
             *
             * @param results The result of the operation computed by [.doInBackground].
             */
            override fun onPostExecute(results: List<String>) {
                transcendAdapter = StringListAdapter(results, mLayoutManager)
                transcendRecyleView.adapter = transcendAdapter
                transcendRecyleView.layoutManager = mLayoutManager
                transcendWaiting.visibility = View.GONE
                transcendRecyleView.visibility = View.VISIBLE
            }
        }
        mtranscendDataTask.execute(resourceID)
    }

    companion object {

        /**
         * List of the resource ids for the transcendental Books
         */
        val resourceIDS = intArrayOf(R.raw.emerson_conduct_of_life, R.raw.emerson_essays_second_series, R.raw.emerson_poems, R.raw.emerson_representative_men, R.raw.thoreau_excursions, R.raw.bulfinch, R.raw.nietzshe)

        /**
         * List of the titles for the transcendental Books (used to label the selection buttons)
         */
        val titles = arrayOf("Emerson The Conduct of Life", "Emerson Essays, Second Series", "Emerson Poems", "Emerson Representative Men", "Thoreau Excursions", "Bulfinch’s Mythology", "Nietzshe's Philosophy")
    }

}
