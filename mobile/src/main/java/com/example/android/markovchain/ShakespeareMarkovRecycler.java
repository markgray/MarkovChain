package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.common.DoneListener;
import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;
import com.example.android.common.ShakespeareSmall;

import java.io.IOException;
import java.io.StringReader;

/**
 * This Activity displays the random nonsense spouted by Markov when Shakespeare's sonnets are used
 * to create the Markov chain state table that Markov uses.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class ShakespeareMarkovRecycler extends Activity {
    public final String TAG = "ShakespeareMarkovR..."; // TAG used for logging
    /**
     * Markov instance used to generate nonsense text
     */
    protected Markov mMarkov = new Markov();
    /**
     * RecyclerView in our layout file
     */
    RecyclerView mRecyclerView;
    /**
     * LayoutManager used by our RecyclerView
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * RecyclerView.Adapter<MarkovAdapter.ViewHolder> MarkovAdapter used by our RecyclerView
     */
    protected MarkovAdapter mAdapter;

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, then we cause our layout file R.layout.activity_shakespeare_markov_recycler to be
     * inflated and set it to be our content view. Next we initialize our field mLayoutManager with
     * an instance of LinearLayoutManager and we initialize our field mRecyclerView by locating the
     * RecyclerView in our layout R.id.shakespeare_markov_recycler_view. Then we initialize the
     * Markov state table of Markov mMarkov by calling initMarkovState. We initialize our field
     * MarkovAdapter mAdapter with an instance of MarkovAdapter which uses Markov mMarkov as its
     * Markov instance, and RecyclerView.LayoutManager mLayoutManager as its LayoutManager. Finally
     * we set the adapter of our RecyclerView mRecyclerView to our field MarkovAdapter mAdapter, and
     * the LayoutManager to our field mLayoutManager.
     *
     * @param savedInstanceState since we do not override onSaveInstanceState we do not use this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_markov_recycler);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.shakespeare_markov_recycler_view);

        initMarkovState();

        mAdapter = new MarkovAdapter(this, mMarkov);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Initialize the Markov state table of field Markov mMarkov using the String[] array containing
     * Shakespeare's sonnets with all lowercase letters: ShakespeareSmall.SONNETS. First we create an
     * instance of StringBuilder stringBuilder and use it to append all the sonnets in the String[]
     * array ShakespeareSmall.SONNETS into one very long String. We set the DoneListener of our field
     * Markov mMarkov to an anonymous class which simply Toast's a different Toast then the default
     * implementation and "clicks" the RecyclerView mRecyclerView.
     * TODO: refactor our mMarkov.make to use same technique as BibleMarkovFragment
     * Then wrapped in a try block we initialize the state table in our Markov mMarkov by using
     * mMarkov.make on a StringReader created from the String value of StringBuilder stringBuilder.
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
