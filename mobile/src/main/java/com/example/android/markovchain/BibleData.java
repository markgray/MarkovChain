package com.example.android.markovchain;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BibleData {
    static final String TAG = "BibleData";
    ArrayList<String> stringList = new ArrayList<>();
    ArrayList<String> bookChapterVerse = new ArrayList<>();

    private void initDataset(Context context) {
        final String[] line = new String[1];
        final StringBuilder[] builder = {new StringBuilder()};
        InputStream inputStream = context.getResources().openRawResource(R.raw.king_james_text_and_verse);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        /**
         * This is the thread that will do our work.  It reads each line of text
         * and adds it to a StringBuilder until it finds an empty line which is
         * used to separate verses then converts the StringBuilder to a string
         * and adds it to list of Strings.
         */
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    while ((line[0] = reader.readLine()) != null) {
                        bookChapterVerse.add(line[0]);
                        while ((line[0] = reader.readLine()) != null) {
                            builder[0].append(line[0]);
                            if (line[0].length() == 0) {
                                stringList.add(builder[0].toString());
                                builder[0] = new StringBuilder();
                                break;
                            } else {
                                builder[0].append(" ");
                            }
                        }
                    }
                    Log.i(TAG, "Verses read: " + stringList.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        mThread.start();
    }

}
