package com.example.android.markovchain;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class DoMarkov extends Activity {

    public TextView mTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_markov);
        mTextview = (TextView) findViewById(R.id.textview_for_nonsense);
    }

}
