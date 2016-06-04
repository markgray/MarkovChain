package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.common.Shakespeare;
import com.example.android.common.StringArrayAdapter;

public class ShakespeareRecylcler extends Activity {

    public final String TAG = "ShakespeareRecycler";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected StringArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_recylcler);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.shakespeare_recyclerview);
        mAdapter = new StringArrayAdapter(Shakespeare.SONNETS, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.i(TAG, "Verses read: " + Shakespeare.SONNETS.length);
    }
}
