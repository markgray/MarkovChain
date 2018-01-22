package com.example.android.markovchain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class WhatIsMan extends AppCompatActivity {
    RecyclerView whatIsMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_man);

        whatIsMan = (RecyclerView) findViewById(R.id.what_recyclerview);

    }

}
