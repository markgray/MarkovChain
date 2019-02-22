package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.common.DoneListener;
import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This {@code Activity} generates random gibberish using a Markov chain state table that was generated
 * from the King James Bible offline.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class BibleMarkovFragment extends Activity {
    /**
     * TAG for logging
     */
    public final String TAG = "BibleMarkovFragment";
    /**
     * {@code ProgressBar} in our layout file that is displayed while the Markov chain state table
     * is being loaded.
     */
    ProgressBar mProgressBar;
    /**
     * {@code RecyclerView} in our layout file that displays the random gibberish verses.
     */
    RecyclerView mRecyclerView;
    /**
     * {@code LayoutManager} used for our {@code RecyclerView}
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * {@code MarkovAdapter} used as the adapter fo our {@code RecyclerView}
     */
    protected MarkovAdapter mAdapter;
    /**
     * {@code Markov} instance used to generate random text to display
     */
    protected Markov mMarkov = new Markov();

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file R.layout.activity_bible_markov_fragment.
     * We initialize our field {@code ProgressBar mProgressBar} by fining the view in our layout with id
     * R.id.bible_markov_fragment_progress, our field {@code RecyclerView mRecyclerView} by finding the
     * view with id R.id.bible_markov_fragment, and initialize our field {@code LayoutManager mLayoutManager}
     * with a new instance of {@code LinearLayoutManager}. Then we call our method {@code initMarkov()} which
     * starts a background thread to read in our offline generated Markov state table using the {@code load}
     * method of our field {@code Markov mMarkov} and we initialize our field {@code MarkovAdapter mAdapter}
     * with an instance of {@code MarkovAdapter} constructed to use {@code Markov mMarkov} as its data
     * generator and {@code RecyclerView.LayoutManager mLayoutManager} as its {@code LayoutManager}.
     * Next we set the adapter used by {@code mRecyclerView} to {@code mAdapter} and its {@code LayoutManager}
     * to {@code mLayoutManager}. We set the {@code OnClickListener} of {@code mProgressBar} to an anonymous
     * class which will switch the switch the visibility of {@code mProgressBar} to INVISIBLE, switch the
     * visibility of {@code mRecyclerView} to VISIBLE and tell {@code MarkovAdapter mAdapter} that its data
     * set has changed so that it will refresh the contents of {@code mRecyclerView} (this is done so that
     * the {@code DoneListener} for {@code mMarkov} can just call the {@code callOnClick} method of
     * {@code mProgressBar} when it is done loading the Markov state table in order to switch from the
     * {@code ProgressBar} to the {@code RecyclerView}). Finally we set the {@code DoneListener} of
     * {@code mMarkov} to an anonymous class which will call {@code callOnClick} method of {@code mProgressBar}
     * when {@code onDoneDo(mProgressBar)} is called.
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_markov_fragment);

        mProgressBar = findViewById(R.id.bible_markov_fragment_progress);
        mRecyclerView = findViewById(R.id.bible_markov_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        initMarkov();
        mAdapter = new MarkovAdapter(getFragmentManager(), mMarkov);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressBar.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when the {@code ProgressBar} is clicked or {@code View.callOnClick} is called on
             * it. We set the visibility of {@code ProgressBar mProgressBar} to INVISIBLE, set the visibility
             * of {@code RecyclerView mRecyclerView} to VISIBLE, and notify {@code MarkovAdapter mAdapter}
             * that the data set has changed so that it will fill {@code mRecyclerView} with random verses
             * generated by {@code Markov mMarkov}.
             *
             * @param view View of the ProgressBar which was clicked
             */
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });
        mMarkov.setDoneListener(new DoneListener() {
            /**
             * Called by {@code DoneListener.onDone} by {@code Markov} when it is done reading in the
             * Markov state table. We simply call {@code callOnClick} using the view passed us. (The
             * Toast is just there for debugging purposes)
             *
             * @param view a View for Context for a Toast or other possible uses.
             */
            @Override
            public void onDoneDo(final View view) {
                Toast.makeText(view.getContext(), "I am done OVERRIDE.", Toast.LENGTH_LONG).show();
                view.callOnClick();
            }
        }, mProgressBar);
    }

    /**
     * This method reads in the Markov state table contained in raw/king_james_state_table.txt
     * using a background thread. We initialize our variable {@code InputStream inputStream} by using
     * the context of the single, global Application object of the current process to fetch a {@code Resources}
     * instance for the application's package and use it to open a data stream for reading the raw resource
     * with id R.raw.king_james_state_table. Then we initialize {@code BufferedReader reader} with a new
     * instance that uses an {@code InputStreamReader} that uses the default charset when reading from
     * {@code inputStream}. Next we initialize {@code Thread mThread} with an anonymous class whose
     * {@code run} override just calls the {@code load} method of {@code Markov mMarkov} read from {@code reader}
     * in order to have {@code mMarkov} construct its state table from its contents. Finally we call the
     * {@code start} method of {@code mThread} to start the {@code Thread} running.
     */
    private void initMarkov() {

        // InputStream for reading the raw bytes from the file R.raw.king_james_state_table from
        // which an InputStreamReader is constructed to convert the bytes to characters, and finally
        // the BufferedReader which Markov.load needs to read the file line by line.
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_state_table);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        /*
         * This is the thread that will do our work. Pretty much self explanatory since Markov.load
         * does all the work. We just wrap the call in a "try" in order to catch IOException
         */
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                mMarkov.load(reader);
            }
        };
        mThread.start();
        Log.i(TAG, "We are waiting for Markov chain to load");
    }
}
