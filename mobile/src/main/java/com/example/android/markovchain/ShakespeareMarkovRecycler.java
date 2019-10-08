package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.StringReader;

/**
 * This {@code Activity} displays the random nonsense spouted by {@code Markov} when Shakespeare's
 * sonnets are used to create the Markov chain state table that {@code Markov} uses.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class ShakespeareMarkovRecycler extends FragmentActivity {
    /**
     * TAG used for logging
     */
    public final String TAG = "ShakespeareMarkovR...";
    /**
     * {@code Markov} instance used to generate nonsense text
     */
    protected Markov mMarkov = new Markov();
    /**
     * {@code RecyclerView} in our layout file
     */
    RecyclerView mRecyclerView;
    /**
     * {@code LayoutManager} used by our {@code RecyclerView}
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * {@code MarkovAdapter} used by our RecyclerView (a {@code RecyclerView.Adapter<MarkovAdapter.ViewHolder>})
     */
    protected MarkovAdapter mAdapter;

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * {@code onCreate}, then we cause our layout file R.layout.activity_shakespeare_markov_recycler
     * to be inflated and set to be our content view. Next we initialize our field {@code mLayoutManager}
     * with an instance of {@code LinearLayoutManager} and we initialize our field {@code mRecyclerView}
     * by finding the {@code RecyclerView} with id R.id.shakespeare_markov_recycler_view. Then we
     * call our method {@code initMarkovState()} to initialize the Markov state table of {@code mMarkov}
     * by calling its {@code make} method. We initialize our field {@code MarkovAdapter mAdapter} with
     * a new instance of {@code MarkovAdapter} which uses {@code Markov mMarkov} as its {@code Markov}
     * instance. Then we set the adapter of our {@code mRecyclerView} to our field {@code mAdapter},
     * and its {@code LayoutManager} to our field {@code mLayoutManager}.
     *
     * @param savedInstanceState since we do not override onSaveInstanceState we do not use this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_markov_recycler);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = findViewById(R.id.shakespeare_markov_recycler_view);

        initMarkovState();

        mAdapter = new MarkovAdapter(getSupportFragmentManager(), mMarkov);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Initialize the Markov state table of field {@code Markov mMarkov} using the {@code String[]}
     * array containing Shakespeare's sonnets with all lowercase letters: the {@code SONNETS} field
     * of {@code ShakespeareSmall}. First we initialize our variable {@code StringBuilder stringBuilder}
     * with a new and loop over all the {@code String quotes} in the {@code SONNETS} field of
     * {@code ShakespeareSmall} appending each {@code String quotes} to {@code stringBuilder}. We set
     * the {@code DoneListener} of our field {@code Markov mMarkov} to an anonymous class which simply
     * Toast's a different Toast than the default implementation and then calls the {@code callOnClick}
     * method of {@code mRecyclerView} (which will be the {@code View view} passed to {@code onDoneDo})
     * to "click" the {@code RecyclerView}.
     * TODO: refactor our mMarkov.make to use same technique as BibleMarkovFragment
     * Then wrapped in a try block intended to catch and log IOException we initialize the state table
     * in our {@code Markov mMarkov} by using its {@code make} method on a {@code StringReader} created
     * from the {@code String} value of {@code StringBuilder stringBuilder}.
     */
    private void initMarkovState() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String quotes : ShakespeareSmall.SONNETS) {
            stringBuilder.append(quotes);
        }

        // TODO: do this as thread
        mMarkov.setDoneListener(new DoneListener() {
            @Override
            public void onDoneDo(final View view) {
                Toast.makeText(view.getContext(), "I am done OVERRIDE.", Toast.LENGTH_LONG).show();
                view.callOnClick();
            }
        }, mRecyclerView);

        try {
            Log.i(TAG, "making chain");
            mMarkov.make(new StringReader(stringBuilder.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
