package com.example.android.markovchain;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BibleActivity extends AppCompatActivity {

    public final String TAG = "BibleActivity";

    public ListView mlistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);
        mlistView = (ListView) findViewById(android.R.id.list);
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        Adapter
//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<>(this,
//                        android.R.layout.simple_list_item_1,
//                        tempStr);
//        mlistView.setAdapter(itemsAdapter);

    }
}
