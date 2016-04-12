package com.example.android.markovchain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BibleMarkovFragment extends AppCompatActivity {
    public final String TAG = "BibleMarkovFragment";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected MarkovAdapter mAdapter;
    protected Markov mMarkov = new Markov();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_markov_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_markov_fragment);
        initMarkov();
        mAdapter = new MarkovAdapter(mMarkov, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void initMarkov() {
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_state_table);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        /**
         * This is the thread that will do our work.  It sits in a loop running
         * the progress up until it has reached the top, then stops and waits.
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
    }
}
