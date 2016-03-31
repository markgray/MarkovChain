package com.example.android.markovchain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class BibleRecycler extends AppCompatActivity {
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_recycler);
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_recyclerview);
    }
}
