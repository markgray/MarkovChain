package com.example.android.markovchain;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.common.Shakespeare;

public class DoMarkov extends ListActivity {

    public ListView mlistView;
    public Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_markov);
        mlistView = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Shakespeare.DIALOGUE);
        mlistView.setAdapter(itemsAdapter);
    }

}
