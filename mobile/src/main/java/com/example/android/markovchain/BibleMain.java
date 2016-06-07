package com.example.android.markovchain;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BibleMain extends Activity {

    public final String TAG = "BibleMain";
    public static final String LAST_VERSE_VIEWED = "LAST_VERSE_VIEWED";
    private static final String CLASS = BibleMain.class.getSimpleName();
    public static Context bibleContext;
    public BibleData bibleData = new BibleData();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public static boolean doneReading = false;
    public static ConditionVariable mDoneReading = new ConditionVariable();
    protected BibleAdapter mAdapter;
    ArrayList<String> stringList = new ArrayList<>();
    ArrayList<String> bookChapterVerse = new ArrayList<>();
    public static BibleDialog bibleDialog;
    BibleSearch bibleSearch;
    public static String dialogTitle;
    public static String dialogText;
    public static int dialogVerse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doneReading = false;
        mDoneReading.close();
        bibleContext = getApplicationContext();
        initDataset();
        setContentView(R.layout.activity_bible_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_recyclerview);
        mAdapter = new BibleAdapter(stringList, bookChapterVerse, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStop() {
        int lastFirstVisiblePosition = ((LinearLayoutManager)mRecyclerView
                .getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        saveVerseNumber(lastFirstVisiblePosition, LAST_VERSE_VIEWED);
        super.onStop();
    }

    @Override
    protected void onResume() {
        int lastFirstVisiblePosition = getVerseNumber(0, LAST_VERSE_VIEWED);
//        ((LinearLayoutManager) mRecyclerView.getLayoutManager())
//                .scrollToPositionWithOffset(lastFirstVisiblePosition, 0);
        mAdapter.moveToVerse(mRecyclerView, lastFirstVisiblePosition);
        super.onResume();
    }

    public static void saveVerseNumber(int verse, String key) {
        SharedPreferences pref = bibleContext.getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, verse);
        editor.commit();
    }

    public int getVerseNumber(int verse, String key) {
        SharedPreferences pref = getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        return pref.getInt(key, verse);
    }


    public void showDialog(String label, String text) {
        dialogTitle = label;
        dialogText = text;

        // Create and show the dialog.
        bibleDialog = BibleDialog.newInstance(label, text);
        showDialog(bibleDialog);
    }

    public int showDialog(DialogFragment dialogFragment) {
        // dialogFragment must already have setArguments set to bundle!
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return dialogFragment.show(ft, "dialog");
    }

     public void dismissDiaglog() {
        bibleDialog.dismiss();
    }

    public void handleAction(View v, int spinIndex) {
        switch (spinIndex) {
            case BibleDialog.CHOICE_DISMISS:
                dismissDiaglog();
                break;
            case BibleDialog.CHOICE_RANDOM_VERSE:
                BibleAdapter.moveToRandom(v);
//                showDialog(BibleDialog.newInstance(dialogTitle, dialogText));
                bibleDialog.refresh(dialogTitle, dialogText);
                break;
            case BibleDialog.CHOICE_GOOGLE:
                showDialog(BibleSearch.newInstance(dialogTitle, dialogText));
                break; // Unimplemented future feature
            case BibleDialog.CHOICE_BOOKMARK:
                break; // Unimplemented future feature
            case BibleDialog.CHOICE_GO_TO_VERSE:
                showDialog(BibleChoose.newInstance(dialogTitle, dialogText));
                break; // Unimplemented future feature
            case BibleDialog.CHOICE_READ_ALOUD:
                showDialog(BibleSpeak.newInstance(dialogTitle, dialogText));
                break;
            default:
                break;
        }
    }

    private void initDataset() {
        final String[] line = new String[1];
        final StringBuilder[] builder = {new StringBuilder()};
        InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_text_and_verse);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        /**
         * This is the thread that will do our work.  It reads each line of text
         * and adds it to a StringBuilder until it finds an empty line which is
         * used to separate verses then converts the StringBuilder to a string
         * and adds it to list of Strings.
         */
        doneReading = false;
        final Thread mThread = new Thread() {
            @Override
            public void run() {
                mDoneReading.close();
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
                mDoneReading.open();
                doneReading = true;
            }
        };
        mThread.start();
    }

}
