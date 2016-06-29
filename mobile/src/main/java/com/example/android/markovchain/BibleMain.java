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
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public static boolean doneReading = false;
    public static ConditionVariable mDoneReading = new ConditionVariable();
    protected BibleAdapter mAdapter;
    ArrayList<String> stringList = new ArrayList<>();
    static ArrayList<String> bookChapterVerse = new ArrayList<>();
    public static BibleDialog bibleDialog;
    public static String dialogTitle;
    public static String dialogText;
    public static int dialogVerse;

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(android.net.Uri , String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     *
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
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

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     *
     * <p>Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its {@link #onPause} method is called.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        int lastFirstVisiblePosition = ((LinearLayoutManager)mRecyclerView
                .getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        saveVerseNumber(lastFirstVisiblePosition, LAST_VERSE_VIEWED);
        super.onStop();
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     *
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        int lastFirstVisiblePosition = restoreVerseNumber(0, LAST_VERSE_VIEWED);
        BibleAdapter.moveToVerse(mRecyclerView, lastFirstVisiblePosition);
        super.onResume();
    }

    /**
     * Save the currently viewed verse to shared preferences file
     *
     * @param verse verse number
     * @param key   key to store it under (presently only "LAST_VERSE_VIEWED")
     */
    public static void saveVerseNumber(int verse, String key) {
        SharedPreferences pref = bibleContext.getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, verse);
        editor.apply();
    }

    /**
     * Retrieve the last viewed verse from shared preferences file
     *
     * @param verse verse number default value
     * @param key   key it was stored under (presently only "LAST_VERSE_VIEWED")
     * @return      verse number stored in shared preferences, or the default value passed it
     */
    public int restoreVerseNumber(int verse, String key) {
        SharedPreferences pref = getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        return pref.getInt(key, verse);
    }

    /**
     * Finds the verse index of a given standard Bible citation, using the fallback citation
     * in case there are no exact matches for the given citation
     *
     * @param citation Bible citation we are looking for
     * @param fallback a fallback citation to use if that citation is not foud
     *
     * @return Index of the verse we are interested in
     */
    public static int findFromCitation(String citation, String fallback) {
        int fallBackIndex = 0;
        for (int i = 0; i < bookChapterVerse.size(); i++) {
            if (citation.equals(bookChapterVerse.get(i))) {
                return i;
            }
            if (fallback.equals(bookChapterVerse.get(i))) {
                fallBackIndex = i;
            }
        }
        return fallBackIndex;
    }

    /**
     *  Returns the book number index for a citation which uses the name instead of the number
     *
     * @param citation Standard Bible citation
     * @return Index of the book name
     */
    public static int indexFromCitation(String citation) {
        String bookLook = citation.substring(0, citation.indexOf(":"));
        int indexOf = 0;
        for (String book : BibleAdapter.books) {
            if (bookLook.equals(book)) {
                return indexOf;
            }
            indexOf++;
        }
        return 0;
    }

    /**
     * Convenience function for starting a BibleDialog fragment
     *
     * @param label citation for verse contained in text
     * @param text  text of current verse
     */
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
            case BibleDialog.CHOICE_NONE:
                break;
            case BibleDialog.CHOICE_RANDOM_VERSE:
                BibleAdapter.moveToRandom(v);
                bibleDialog.refresh(dialogTitle, dialogText);
                break;
            case BibleDialog.CHOICE_GOOGLE:
                showDialog(BibleSearch.newInstance(dialogTitle, dialogText));
                break;
            case BibleDialog.CHOICE_BOOKMARK:
                showDialog(BibleBookmark.newInstance(dialogTitle, dialogText));
                break; // TODO: add a set of strings to shared preferences or sql file
            case BibleDialog.CHOICE_GO_TO_VERSE:
                showDialog(BibleChoose.newInstance(dialogTitle, dialogText));
                break;
            case BibleDialog.CHOICE_READ_ALOUD:
                showDialog(BibleSpeak.newInstance(dialogTitle, dialogText));
                break;
            default:
                break;
        }
    }

    /**
     * Reads the raw file king_james_text_and_verse.txt, separating it into citations
     * (bookChapterVerse) and verse text (stringList)
     */
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
