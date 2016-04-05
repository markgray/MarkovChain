package com.example.android.markovchain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.common.Markov;
import com.example.android.common.MarkovAdapter;
import com.example.android.common.Shakespeare;
import com.example.android.common.StringArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ShakespeareMarkovRecycler extends AppCompatActivity {
    public final String TAG = "ShakespeareMarkovRecycler";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected MarkovAdapter mAdapter;
    protected Markov mMarkov = new Markov();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_markov_recycler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.shakespeare_markov_recyclerview);
        StringBuilder stringBuilder = new StringBuilder();
        for (String quotes: Shakespeare.DIALOGUE) {
            stringBuilder.append(quotes);
        }
        try {
            mMarkov.make(new StringReader(stringBuilder.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAdapter = new MarkovAdapter(mMarkov, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }
}
