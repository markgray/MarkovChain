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
import com.example.android.common.Shakespeare;

import java.io.IOException;
import java.util.Random;

public class ShakespeareActivity extends ListActivity {

    public final String TAG = "ShakespeareActivity";

    public ListView mlistView;
    public Markov markov = new Markov();
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shakespeare);
        mlistView = (ListView) findViewById(android.R.id.list);
        try {
            markov.startUp(Shakespeare.DIALOGUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Size of generated text:" + markov.mOutput.size());
        final String[] tempStr = new String[(markov.mOutput.size())];
        int tempStrNext = 0;
        StringBuilder stringBuilder = new StringBuilder(400);
        for (int i = 0; i < markov.mOutput.size(); i++) {
            tempStr[i] = "<empty>";
            String word = markov.mOutput.get(i);
            stringBuilder.append(word).append(" ");
            if (word.contains(".") || word.contains("?") || word.contains("!")) {
                stringBuilder.append("\n");
                tempStr[tempStrNext++] = stringBuilder.toString();
                stringBuilder = new StringBuilder(400);
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
