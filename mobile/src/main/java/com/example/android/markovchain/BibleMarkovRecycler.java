package com.example.android.markovchain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;
import com.example.android.common.Shakespeare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class BibleMarkovRecycler extends AppCompatActivity {
    public final String TAG = "ShakespeareMarkovRecycler";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected MarkovAdapter mAdapter;
    protected Markov mMarkov = new Markov();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_markov_recycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_markov_recyclerview);
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_state_table);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            mMarkov.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAdapter = new MarkovAdapter(mMarkov, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
