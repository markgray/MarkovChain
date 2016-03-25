package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.common.Markov;
import com.example.android.common.Shakespeare;

import java.io.IOException;

public class ShakespeareActivity extends ListActivity {

    public final String TAG = "ShakespeareActivity";

    public ListView mlistView;
    public Markov markov = new Markov();

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
        String[] tempStr = new String[(markov.mOutput.size())];
        int tempStrNext = 0;
        StringBuilder stringBuilder = new StringBuilder(60);
        for (int i = 0; i < markov.mOutput.size(); i++) {
            tempStr[i] = "<empty>";
            String word = markov.mOutput.get(i);
            if (stringBuilder.length() + word.length() < 60) {
                stringBuilder.append(word + " ");
            } else {
                stringBuilder.append("\n");
                tempStr[tempStrNext++] = stringBuilder.toString();
                stringBuilder.delete(0, stringBuilder.length());
                stringBuilder.append(word + " ");
            }
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        tempStr);
        mlistView.setAdapter(itemsAdapter);
    }
}
