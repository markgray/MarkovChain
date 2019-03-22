package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.common.StringListAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This {@code Activity} loads text files from the raw resources of the app in the background, and
 * displays them in a {@code RecyclerView}.
 */
public class TranscendActivity extends Activity {
    /**
     * {@code RecyclerView} used to display our books
     */
    RecyclerView transcendRecyleView;
    /**
     * {@code StringListAdapter} we use for our {@code RecyclerView transcendRecyleView}
     */
    StringListAdapter transcendAdapter;
    /**
     * {@code LayoutManager} for our {@code RecyclerView} (a {@code LinearLayout} instance)
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * {@code TextView} used to display "Waiting for data to load…" message while waiting
     */
    TextView transcendWaiting;
    /**
     * {@code LinearLayout} that we add our book selection {@code Button}s to.
     */
    LinearLayout transcendBooks;
    /**
     * {@code ScrollView} that holds the {@code LinearLayout transcendBooks}
     */
    ScrollView transcendBooksScrollView;

    /**
     * List of the resource ids for the transcendental Books
     */
    public static final int[] resourceIDS = {
            R.raw.emerson_conduct_of_life, R.raw.emerson_essays_second_series, R.raw.emerson_poems,
            R.raw.emerson_representative_men, R.raw.thoreau_excursions, R.raw.bulfinch,
            R.raw.nietzshe
    };

    /**
     * List of the titles for the transcendental Books (used to label the selection buttons)
     */
    public static final String[] titles = {
            "Emerson The Conduct of Life", "Emerson Essays, Second Series", "Emerson Poems",
            "Emerson Representative Men", "Thoreau Excursions", "Bulfinch’s Mythology",
            "Nietzshe's Philosophy"
    };

    /**
     * Called when the activity is starting. First we call our super's implementation of {@code onCreate},
     * then we set our content view to our layout file R.layout.activity_transcend. We initialize our
     * field {@code RecyclerView.LayoutManager mLayoutManager} with a new {@code LinearLayoutManager}
     * instance, initialize our field {@code LinearLayout transcendBooks} by finding the view with
     * id R.id.transcend_books, initialize our field {@code ScrollView transcendBooksScrollView} by
     * finding the view with id R.id.transcend_books_scrollView, initialize our field
     * {@code RecyclerView transcendRecyleView} by finding the view with id R.id.transcend_recycle_view,
     * and initialize our field {@code TextView transcendWaiting} by finding the view with id
     * R.id.transcend_waiting. Then we loop over {@code int i} for all the resource id's in the array
     * {@code int[] resourceIDS} calling our method {@code addButton} to add a button to our field
     * {@code transcendBooks} with the label {@code titles[i]} which will load and display the raw
     * text file whose resource id is {@code resourceIDS[i]} when the button is clicked.
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcend);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        transcendBooks = findViewById(R.id.transcend_books);
        transcendBooksScrollView = findViewById(R.id.transcend_books_scrollView);
        transcendRecyleView = findViewById(R.id.transcend_recycle_view);
        transcendWaiting = findViewById(R.id.transcend_waiting);
        for (int i = 0; i < resourceIDS.length; i++) {
            addButton(resourceIDS[i], titles[i], transcendBooks);
        }
    }

    /**
     * Adds a {@code Button} to its parameter {@code ViewGroup parent} whose label is given by its
     * parameter {@code String description} and whose {@code OnClickListener} sets the visibility of
     * the {@code ScrollView transcendBooksScrollView} that holds our Books selection UI to GONE and
     * calls our method {@code loadResourceTextFile} to have it load and display the resource file
     * with id {@code int resourceID} in the background.
     *
     * @param resourceID  resource ID that our button's {@code OnClickListener} should call the method
     *                    {@code loadResourceTextFile} to load in the background.
     * @param description Label for our {@code Button}
     * @param parent      {@code ViewGroup} we should add our {@code Button} to.
     */
    public void addButton(final int resourceID, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setText(description);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the {@code Button} is clicked. We just set the visibility of our field
             * {@code ScrollView transcendBooksScrollView} to GONE (disappears our Books selection
             * buttons), set the visibility of our field {@code TextView transcendWaiting} to VISIBLE
             * and call our method {@code loadResourceTextFile} to load and display the file whose
             * resource ID is that given by the {@code addButton} method's parameter {@code resourceID}.
             *
             * @param v {@code View} that was clicked.
             */
            @Override
            public void onClick(View v) {
                transcendBooks.setVisibility(View.GONE);
                transcendBooksScrollView.setVisibility(View.GONE);
                transcendWaiting.setVisibility(View.VISIBLE);
                loadResourceTextFile(resourceID);
            }
        });
        parent.addView(button);
    }

    /**
     * Causes the utf8 text file with resource ID {@code int resourceID} to be read in by a background
     * task, and then displays the {@code List<String> results} the task returns in our field
     * {@code RecyclerView transcendRecyleView}.
     *
     * @param resourceID resource ID of the raw file we are to read in the background and then display
     *                   in {@code RecyclerView transcendRecyleView} once the background task is done.
     */
    private void loadResourceTextFile(int resourceID) {
        @SuppressLint("StaticFieldLeak")
        TranscendDataTask mtranscendDataTask = new TranscendDataTask(getApplicationContext()) {
            /**
             * Runs on the UI thread after {@link #doInBackground}. The parameter
             * {@code List<String> results} is the value returned by {@link #doInBackground}.
             * We initialize our field {@code StringListAdapter transcendAdapter} with a new instance
             * which will use our parameter {@code List<String> results} as its data set, and our field
             * {@code RecyclerView.LayoutManager mLayoutManager} as its {@code LayoutManager}, set the
             * adapter of {@code RecyclerView transcendRecyleView} to {@code transcendAdapter} and set
             * the {@code LayoutManager} that {@code transcendRecyleView} will use to be our field
             * {@code mLayoutManager}. Finally we set the visibility of our field {@code TextView transcendWaiting}
             * to GONE, and set the visibility of {@code transcendRecyleView} to VISIBLE.
             *
             * @param results The result of the operation computed by {@link #doInBackground}.
             */
            @Override
            protected void onPostExecute(List<String> results) {
                transcendAdapter = new StringListAdapter(results, mLayoutManager);
                transcendRecyleView.setAdapter(transcendAdapter);
                transcendRecyleView.setLayoutManager(mLayoutManager);
                transcendWaiting.setVisibility(View.GONE);
                transcendRecyleView.setVisibility(View.VISIBLE);
            }
        };
        mtranscendDataTask.execute(resourceID);
    }

}
