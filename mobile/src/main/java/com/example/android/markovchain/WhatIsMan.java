package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

public class WhatIsMan extends Activity {
    RecyclerView whatIsMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_man);

        whatIsMan = (RecyclerView) findViewById(R.id.what_recyclerview);

    }

}
