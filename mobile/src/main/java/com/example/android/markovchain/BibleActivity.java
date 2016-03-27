package com.example.android.markovchain;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class BibleActivity extends ListActivity {

    public final String TAG = "BibleActivity";
    public ArrayList<String> tempList = new ArrayList<>();
    public ListView mlistView;
    protected String[] tempStr;
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);
        mlistView = (ListView) findViewById(android.R.id.list);
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        fillArray(reader);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1,
                        tempStr);
        mlistView.setAdapter(itemsAdapter);
        mlistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int selection = Math.abs(rand.nextInt()) % tempStr.length;
                mlistView.setSelection(selection);
                Toast.makeText(getApplicationContext(), "Moving to verse " + selection, Toast.LENGTH_LONG).show();
                return true;
            }
        });

    }

    private void fillArray(BufferedReader reader) {
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                if (line.length() == 0) {
                    tempList.add(builder.toString());
                    builder = new StringBuilder();
                } else {
                    builder.append(" ");
                }
            }
            Log.i(TAG, "Verses read: " + tempList.size());
            tempStr = new String[tempList.size()];
            for (int i = 0; i < tempStr.length; i++) {
                tempStr[i] = tempList.get(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
