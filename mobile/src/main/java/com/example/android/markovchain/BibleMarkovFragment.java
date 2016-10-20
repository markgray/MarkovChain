package com.example.android.markovchain;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.common.DoneListener;
import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class BibleMarkovFragment extends Activity {
    public final String TAG = "BibleMarkovFragment"; // TAG for logging
    ProgressBar mProgressBar; // ProgressBar in our layout file
    RecyclerView mRecyclerView; // RecyclerView in our layout file
    RecyclerView.LayoutManager mLayoutManager; // LayoutManager use for our RecyclerView
    protected MarkovAdapter mAdapter; // MarkovAdapter used by our RecyclerView
    protected Markov mMarkov = new Markov(); // Markov instance used to generate random text to display

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, and then we set our content view to our layout file R.layout.activity_bible_markov_fragment.
     * We initialize our field ProgressBar mProgressBar by locating the ProgressBar in our layout
     * (R.id.bible_markov_fragment_progress), our field RecyclerView mRecyclerView by locating the
     * RecyclerView in our layout, and our field our field RecyclerView.LayoutManager mLayoutManager
     * by creating a new instance of LinearLayoutManager. Then we call our method initMarkov() which
     * starts a background thread to read in our offline generated Markov state table using
     * mMarkov.load.
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_markov_fragment);

        mProgressBar = (ProgressBar) findViewById(R.id.bible_markov_fragment_progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_markov_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        initMarkov();
        mAdapter = new MarkovAdapter(mMarkov, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        });
        mMarkov.setDoneListener(new DoneListener() {
            @Override
            public void onDoneDo(final View view) {
                Toast.makeText(view.getContext(), "I am done OVERRIDE.", Toast.LENGTH_LONG).show();
                view.callOnClick();
            }
        }, mProgressBar);
    }

    /**
     * This method reads in the Markov state table contained in raw/king_james_state_table.txt
     * using a background thread.
     */
    private void initMarkov() {
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_state_table);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        /**
         * This is the thread that will do our work.
         */
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    mMarkov.load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        Log.i(TAG, "We are waiting for Markov chain to load");
    }
}
