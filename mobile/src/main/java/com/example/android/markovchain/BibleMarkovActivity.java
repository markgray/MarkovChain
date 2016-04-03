package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.common.Markov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.util.ArrayList;
import java.util.Random;

public class BibleMarkovActivity extends ListActivity {
    public final String TAG = "BibleMarkovActivity";
//    public ArrayList<String> stringList = new ArrayList<>();
    public ListView mlistView;
    protected String[] tempStr;
    Random rand = new Random();
    public Markov markov = new Markov();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_markov);
        mlistView = (ListView) findViewById(android.R.id.list);
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_state_table);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            markov.startUp(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Size of generated text:" + markov.mOutput.size());
//        TODO: Write string list adapter

        tempStr = new String[(markov.mOutput.size())];
        int tempStrNext = 0;
        StringBuilder stringBuilder = new StringBuilder(400);
        for (int i = 0; i < markov.mOutput.size(); i++) {
            tempStr[i] = "<empty>";
            String word = markov.mOutput.get(i);
            stringBuilder.append(word).append(" ");
            if (word.contains(".") || word.contains("?") || word.contains("!")) {
                tempStr[tempStrNext++] = stringBuilder.toString();
                stringBuilder.setLength(0);
            }
        }
        tempStr[tempStrNext] = stringBuilder.toString();

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        tempStr);
        mlistView.setAdapter(itemsAdapter);
        final int finalTempStrNext = tempStrNext;
        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int selection = Math.abs(rand.nextInt()) % finalTempStrNext;
                mlistView.setSelection(selection);
                Toast.makeText(getApplicationContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
                return true;
            }
        });


    }

}
