package com.example.android.markovchain.transcend

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.markovchain.R
import com.example.android.markovchain.util.StringListAdapter

/**
 * This `Activity` loads text files from the raw resources of the app in the background, and
 * displays them in a `RecyclerView`.
 */
class TranscendActivity : AppCompatActivity() {
    /**
     * [RecyclerView] used to display our books
     */
    lateinit var transcendRecyleView: RecyclerView

    /**
     * [StringListAdapter] we use to feed our [RecyclerView] field [transcendRecyleView]
     */
    lateinit var transcendAdapter: StringListAdapter

    /**
     * [RecyclerView.LayoutManager] for our `RecyclerView` (a [LinearLayout] instance)
     */
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    /**
     * [TextView] used to display "Waiting for data to load…" message while waiting
     */
    lateinit var transcendWaiting: TextView

    /**
     * [LinearLayout] that we add our book selection [Button]'s to.
     */
    private lateinit var transcendBooks: LinearLayout

    /**
     * [ScrollView] that holds the [LinearLayout] field [transcendBooks]
     */
    private lateinit var transcendBooksScrollView: ScrollView

    /**
     * Called when the activity is starting. First we call [enableEdgeToEdge] to enable edge to
     * edge display, then we call our super's implementation of `onCreate`, and set our content
     * view to our layout file R.`layout.activity_transcend`. We initialize our
     * [RecyclerView.LayoutManager] field [mLayoutManager] with a new [LinearLayoutManager] instance,
     * initialize our [LinearLayout] field [transcendBooks] by finding the view with the id
     * R.id.transcend_books, initialize our [ScrollView] field [transcendBooksScrollView] by
     * finding the view with id R.id.transcend_books_scrollView, initialize our [RecyclerView] field
     * [transcendRecyleView] by finding the view with the id R.id.transcend_recycle_view, and
     * initialize our [TextView] field [transcendWaiting] by finding the view with the id
     * R.id.transcend_waiting. Then we loop over the [Int] variable `i` for all the resource id's in
     * the [Int] array [resourceIDS] calling our method [addButton] to add a [Button] to our field
     * [transcendBooks] whose label is the `i`'th entry in the string array [titles], and which will
     * when clicked load and display the raw text file whose resource id is the `i`'th entry in the
     * [Int] array [resourceIDS].
     *
     * @param savedInstanceState we do not override `onSaveInstanceState` so do not use
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
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
     * Adds a [Button] to its [ViewGroup] parameter [parent] whose label is given by its
     * [String] parameter [description] and whose `OnClickListener` sets the visibility of
     * the [LinearLayout] field [transcendBooks] to GONE, sets the visibility of the [ScrollView]
     * field [transcendBooksScrollView] to GONE, sets the visibility of the [TextView] field
     * [transcendWaiting] to VISIBLE and calls our method [loadResourceTextFile] to have it load
     * the resource file with the resource id given us in our parameter [resourceID] in the background
     * and then display it in our [RecyclerView].
     *
     * @param resourceID  resource ID that our button's `OnClickListener` should ask the method
     *  [loadResourceTextFile] to load in the background.
     * @param description Label for our [Button]
     * @param parent      [ViewGroup] we should add our `Button` to.
     */
    private fun addButton(resourceID: Int, description: String, parent: ViewGroup) {
        val button = Button(this)
        button.text = description
        /**
         * Called when the `Button` is clicked. We just set the visibility of our fields
         * `LinearLayout transcendBooks` and `ScrollView transcendBooksScrollView` to GONE
         * (disappears our Books selection buttons), set the visibility of our field
         * `TextView transcendWaiting` to VISIBLE (displays our "Waiting for data to load…" message)
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
     * task, and then displays the `List<String> results` the task returns in our [RecyclerView]
     * field [transcendRecyleView].
     *
     * @param resourceID resource ID of the raw file we are to read in the background and then display
     * in the [RecyclerView] field [transcendRecyleView] once the background task is done.
     */
    private fun loadResourceTextFile(resourceID: Int) {
        val mtranscendDataTask = object : TranscendDataTask(applicationContext) {
            /**
             * Runs on the UI thread after [doInBackground]. The parameter `List<String> results`
             * is the value returned by [doInBackground].
             * We initialize our field `StringListAdapter transcendAdapter` with a new instance
             * which will use our parameter `List<String> results` as its data set, and our field
             * `RecyclerView.LayoutManager mLayoutManager` as its `LayoutManager`, set the
             * adapter of `RecyclerView transcendRecyleView` to `transcendAdapter` and set
             * the `LayoutManager` that `transcendRecyleView` will use to be our field
             * `mLayoutManager`. Finally we set the visibility of our field `TextView transcendWaiting`
             * to GONE, and set the visibility of `transcendRecyleView` to VISIBLE.
             *
             * @param result The result of the operation computed by [.doInBackground].
             */
            override fun onPostExecute(result: List<String>?) {
                transcendAdapter = StringListAdapter(result ?: return, mLayoutManager)
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
        val resourceIDS: IntArray = intArrayOf(
            R.raw.emerson_conduct_of_life,
            R.raw.emerson_essays_second_series,
            R.raw.emerson_poems,
            R.raw.emerson_representative_men,
            R.raw.thoreau_excursions,
            R.raw.bulfinch,
            R.raw.nietzshe
        )

        /**
         * List of the titles for the transcendental Books (used to label the selection buttons)
         */
        val titles: Array<String> = arrayOf(
            "Emerson The Conduct of Life",
            "Emerson Essays, Second Series",
            "Emerson Poems",
            "Emerson Representative Men",
            "Thoreau Excursions",
            "Bulfinch’s Mythology",
            "Nietzshe's Philosophy"
        )
    }

}
