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
import com.example.android.common.Shakespeare;

import java.io.IOException;
import java.io.StringReader;

public class ShakespeareMarkovRecycler extends Activity {
    public final String TAG = "ShakespeareMarkovR...";
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
        for (String quotes: Shakespeare.SONNETS) {
            stringBuilder.append(quotes);
        }

        // TODO: do this as thread
        mMarkov.setDoneListener(new DoneListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
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
        mAdapter = new MarkovAdapter(mMarkov, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
