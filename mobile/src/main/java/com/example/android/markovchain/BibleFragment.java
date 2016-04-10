package com.example.android.markovchain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.common.StringListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BibleFragment extends AppCompatActivity {

    public final String TAG = "BibleRecycler";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected StringListAdapter mAdapter;
    ArrayList<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
        setContentView(R.layout.activity_bible_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_recyclerview);
        mAdapter = new StringListAdapter(stringList, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initDataset() {
        String line;
        StringBuilder builder = new StringBuilder();
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                if (line.length() == 0) {
                    stringList.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(" ");
                }
            }
            Log.i(TAG, "Verses read: " + stringList.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
