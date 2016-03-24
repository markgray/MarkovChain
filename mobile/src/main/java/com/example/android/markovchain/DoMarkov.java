package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class DoMarkov extends ListActivity {

    public ListView mlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_markov);
        mlistView = (ListView) findViewById(android.R.id.list);
    }

}
