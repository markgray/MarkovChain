package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.common.Shakespeare;

import java.util.Random;

public class ShakespeareActivity extends ListActivity {

    public final String TAG = "SonnetsActivity";
    public ListView mlistView;
    Random rand = new Random();

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
        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int selection = Math.abs(rand.nextInt()) % Shakespeare.DIALOGUE.length;
                mlistView.setSelection(selection);
                Toast.makeText(getApplicationContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }
}
