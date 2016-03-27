package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.common.Shakespeare;

public class Sonnets extends ListActivity {

    public final String TAG = "SonnetsActivity";
    public ListView mlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonnets);
        mlistView = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        Shakespeare.DIALOGUE);
        mlistView.setAdapter(itemsAdapter);
    }
}
