package com.example.android.markovchain;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.android.common.Markov;
import com.example.android.common.Shakespeare;

import java.io.IOException;

public class DoMarkov extends ListActivity {

    public ListView mlistView;
    public Cursor mCursor;
    public Markov markov = new Markov();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_markov);
        mlistView = (ListView) findViewById(android.R.id.list);
        try {
            markov.startUp(Shakespeare.DIALOGUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] tempStr = new String[(markov.mOutput.size())];
        for (int i = 0; i < tempStr.length; i++) {
            tempStr[i] = markov.mOutput.get(i);
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        tempStr);
        mlistView.setAdapter(itemsAdapter);
    }
}
