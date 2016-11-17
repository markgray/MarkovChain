package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This is the main activity of our Bible Text reading function.
 */
public class BibleMain extends Activity {

    public final String TAG = "BibleMain"; // TAG for logging
    public static final String LAST_VERSE_VIEWED = "LAST_VERSE_VIEWED"; // Key for shared preference to save verse number
    private static final String CLASS = BibleMain.class.getSimpleName(); // Used to access shared preference file
    @SuppressLint("StaticFieldLeak")
    public static Context bibleContext; // Application Context for BibleMain to use when necessary
    RecyclerView mRecyclerView; // Reference to the RecyclerView in our layout
    RecyclerView.LayoutManager mLayoutManager; // LayoutManager for our RecyclerView
    public static boolean doneReading = false; // Flag used to signal that we are done reading in the Bible
    public static ConditionVariable mDoneReading = new ConditionVariable(); // Used to block until reading is finished
    protected BibleAdapter mAdapter; // The Adapter used to fill our RecyclerView
    ArrayList<String> stringList = new ArrayList<>(); // List of Bible verses
    static ArrayList<String> bookChapterVerse = new ArrayList<>(); // List of citations corresponding to each stringList verse
    @SuppressLint("StaticFieldLeak")
    public static BibleDialog bibleDialog; // Contains reference to the BibleDialog launched by long clicking a verse
    public static String dialogTitle; // Canonical Bible citation used in bibleDialog
    public static String dialogText;  // Verse text used in bibleDialog
    public static int dialogVerse;    // Verse number used in bibleDialog
    public static TextToSpeech textToSpeech; // TextToSpeech instance used by BibleSpeak

    /**
     * Called when the activity is starting. First we call through to the super class's
     * implementation of this method. We set the field mDoneReading to false so that our
     * UI thread knows to wait until our text file is read into memory before trying to
     * access the data. We Reset the ConditionVariable mDoneReading to the closed state
     * so that any threads that call block() will block until someone calls open. We next
     * set the field Context bibleContext to the context of the single, global Application
     * object of the current process. This is because we will later need a Context whose
     * lifecycle is separate from the current context, that is tied to the lifetime of
     * the process rather than the current component. We next call the method initDataSet
     * which will spawn a background thread to read in the data file we will be using
     * (R.raw.king_james_text_and_verse) and create the data structures needed by our
     * activity. Then we set the content View to our layout file (R.layout.activity_bible_fragment),
     * locate the RecyclerView in the layout (R.id.bible_recyclerview) and save it in the
     * field RecyclerView mRecyclerView. Create BibleAdapter mAdapter from the List containing the
     * text (ArrayList<String> stringList), the List containing chapter and verse annotation for
     * each paragraph (ArrayList<String> bookChapterVerse), and the layout manager to use for the
     * RecyclerView.Adapter created (RecyclerView.LayoutManager mLayoutManager -- an instance of
     * LinearLayoutManager created above). We now set the adapter of our RecyclerView mRecyclerView
     * to the BibleAdapter mAdapter we just created, and the layout manager to be used to the
     * RecyclerView.LayoutManager mLayoutManager.
     *
     * @param savedInstanceState always null since onSaveInstanceState is not overridden
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doneReading = false;
        mDoneReading.close();
        bibleContext = getApplicationContext();
        initDataSet();
        setContentView(R.layout.activity_bible_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_recyclerview);
        mAdapter = new BibleAdapter(stringList, bookChapterVerse, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background,
     * but has not (yet) been killed.  The counterpart to onResume. We fetch the verse number
     * of the first completely visible verse by calling findFirstCompletelyVisibleItemPosition,
     * then call saveVerseNumber to save the verse number to our shared preference file under
     * the index LAST_VERSE_VIEWED. Finally we call through to our super's implementation of
     * onPause.
     */
    @Override
    protected void onPause() {
        int lastFirstVisiblePosition = ((LinearLayoutManager)mRecyclerView
                .getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        saveVerseNumber(lastFirstVisiblePosition, LAST_VERSE_VIEWED);
        super.onPause();
    }

    /**
     * Called after onRestoreInstanceState, onRestart, or onPause, for your activity to start
     * interacting with the user. This is a good place to begin animations, open exclusive-access
     * devices (such as the camera), etc.
     *
     * Our first action is to retrieve the last verse viewed number from our shared preference file
     * by calling restoreVerseNumber, then we use this number to move our BibleAdapter to this verse.
     * Finally we call through to our super's implementation of onResume.
     */
    @Override
    protected void onResume() {
        int lastFirstVisiblePosition = restoreVerseNumber(0, LAST_VERSE_VIEWED);
        BibleAdapter.moveToVerse(mRecyclerView, lastFirstVisiblePosition);
        super.onResume();
    }

    /**
     * Perform any final cleanup before an activity is destroyed. If textToSpeech is not null we
     * have been using text to speech in BibleSpeak so we interrupt the current utterance and discard
     * other utterances in the queue by calling TextToSpeech.stop(), release the resources used
     * by the TextToSpeech engine by calling TextToSpeech.shutdown() and then set textToSpeech to
     * null (unnecessary since we are in onDestroy, but better safe than sorry!). Finally we call
     * through to our super's implementation of of onDestroy().
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy has been called");
        // Don't forget to shutdown!
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onDestroy();
    }

    /**
     * Save the currently viewed verse (or any other int) to shared preferences file under the
     * key "key". First we retrieve a reference to the preferences file for the Class BibleMain and
     * save it in SharedPreferences pref. We create an SharedPreferences.Editor editor from pref,
     * use this Editor to save as an int value our parameter verse using the key "key", and finally
     * commit our changes from our Editor to SharedPreferences pref. (Only memory copy is written to,
     * an asynchronous commit is started to write to disk.)
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
     * Retrieve the last viewed verse from shared preferences file. First we retrieve a reference
     * to the preferences file for the Class BibleMain and save it in SharedPreferences pref, then
     * we retrieve and return the int value stored in the preference file under the key "key".
     *
     * @param verse verse number default value
     * @param key   key it was stored under (presently only "LAST_VERSE_VIEWED")
     *
     * @return      verse number stored in shared preferences, or the default value passed it
     */
    public int restoreVerseNumber(int verse, String key) {
        SharedPreferences pref = getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        return pref.getInt(key, verse);
    }

    /**
     * Finds the verse index of a given standard Bible citation, using the fallback citation
     * in case there are no exact matches for the given citation. We start by initializing
     * fallBackIndex to 0 so that if neither citation nor fallback is found by exact match
     * we will return 0 (very beginning of Bible). Then we search through our entire list of
     * citations ArrayList<String> bookChapterVerse checking whether "citation" matches (in
     * which case we immediately return the index for the verse in question) or "fallback"
     * matches (in which case we set fallBackIndex to the index for the verse "fallback"
     * matches). Finally if an exact match for citation has not been found in our bookChapterVerse
     * list we return fallBackIndex.
     *
     * @param citation Bible citation we are looking for
     * @param fallback a fallback citation to use if that citation is not found
     *
     * @return Index of the verse we are interested in
     */
    public static int findFromCitation(String citation, String fallback) {
        int fallBackIndex = 0;
        for (int i = 0; i < bookChapterVerse.size(); i++) {
            final String candidateVerse = bookChapterVerse.get(i);
            if (citation.equals(candidateVerse)) {
                return i;
            }
            if (fallback.equals(candidateVerse)) {
                fallBackIndex = i;
            }
        }
        return fallBackIndex;
    }

    /**
     *  Returns the book number index for a citation which uses the name instead of the number.
     *  First we strip off the book name from the citation (all characters up to the first ":")
     *  into the variable String bookLook. Then we "foreach loop" through all the book names in
     *  the String[] BibleAdapter.books incrementing the int indexOf (starts at zero) every time
     *  the book from BibleAdapter.books fails to match our target bookLook, if they do match we
     *  immediately return indexOf as our result. If none of the books in BibleAdapter.books
     *  match we return 0.
     *
     * @param citation Standard Bible citation
     *
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
     * Convenience function for starting a BibleDialog fragment. First we set our fields String dialogTitle
     * and String dialogText to our arguments for later use, then we call the factory method
     * BibleDialog.newInstance to create a BibleDialog bibleDialog and save it in our static field
     * for later use. Finally we call our overloaded method showDialog(DialogFragment) to pop up
     * the instance of BibleDialog we just created.
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

    /**
     * Shows the DialogFragment passed to it. First we fetch a reference to the FragmentManager used
     * for interacting with fragments associated with this activity and use it to start a series of
     * Fragment edit operations using our variable FragmentTransaction ft which is initialized to an
     * instance of FragmentTransaction using FragmentManager.beginTransaction(). Next we use the
     * FragmentManager to search for any existing Fragment's with the tag "dialog" (the tag used for
     * all the DialogFragment's we display), and if one is found (Fragment prev not equal null) we
     * use the FragmentTransaction ft to remove it. We add FragmentTransaction ft to the BackStack
     * so that the back Button will remember this transaction once we commit the transaction then we
     * call  DialogFragment.show to display the dialog, adding the fragment using FragmentTransaction
     * ft and then committing the transaction.
     *
     * @param dialogFragment DialogFragment subclass which already has had setArguments
     *                       called to attach a Bundle of arguments
     *
     * @return Tag for the fragment, as per FragmentTransaction.add.
     */
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

    /**
     * Convenience function to dismiss the main BibleDialog DialogFragment. This is called only
     * from the OnClickListener for the "DISMISS" Button in BibleDialog, and exists due to errors
     * encountered calling DialogFragment.dismiss() directly from that callback. We simply use our
     * reference to the BibleDialog DialogFragment instance to call DialogFragment.dismiss().
     */
    public void dismissDialog() {
        bibleDialog.dismiss();
    }

    /**
     * Handle whichever action was chosen in the Spinner contained in the BibleDialog fragment.
     * This is called from BibleDialog both in the onItemSelected callback for the Spinner, and in
     * the onClick callback for the "REPEAT" Button. We switch based on the parameter spinIndex
     * which is the position of the Spinner view which was selected:
     *
     *      CHOICE_NONE Do nothing
     *      CHOICE_RANDOM_VERSE Instruct the BibleAdapter to move to a random verse by calling
     *          moveToRandom (the View v is passed so that it can toast the citation for the
     *          verse moved to), then instruct the bibleDialog to refresh its display of the
     *          citation and verse chosen.
     *      CHOICE_GOOGLE Launch an instance of DialogFragment BibleSearch using the current
     *          dialogTitle (citation) and dialogText (verse).
     *      CHOICE_BOOKMARK Launch an instance of DialogFragment BibleBookmark using the current
     *          dialogTitle (citation) and dialogText (verse).
     *      CHOICE_GO_TO_VERSE Launch an instance of DialogFragment BibleChoose using the current
     *          dialogTitle (citation) and dialogText (verse).
     *      CHOICE_READ_ALOUD Launch an instance of DialogFragment BibleSpeak using the current
     *          dialogTitle (citation) and dialogText (verse).
     *
     * If it is none of the above, then we do nothing, although this could only happen as the result
     * of a programming error.
     *
     * @param v Just a view passed to give context for creating a Toast when called for
     * @param spinIndex Spinner index chosen
     */
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
     * (bookChapterVerse) and verse text (stringList). First we open InputStream inputStream
     * to read our copy of the Bible (R.raw.king_james_text_and_verse) from our raw resource
     * directory. Then we create BufferedReader reader from an instance of InputStreamReader
     * created from inputStream (InputStreamReader does the character conversion needed to convert
     * the byte stream coming from the InputStream inputStream to characters, and BufferedReader
     * allows us to use readLine on the characters coming from the InputStreamReader). Then we
     * set the doneReading flag to false so that other code does not try to access our data until
     * it is ready to be used. Then we create the Thread mThread to read in the data from
     * <b>reader</b> and start that thread running.
     */
    private void initDataSet() {
        final InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_text_and_verse);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        doneReading = false;

        /**
         * This is the thread that will do our work. First it calls <b>close()</b> on the
         * ConditionVariable mDoneReading so that other Thread's may block on mDoneReading until
         * we call <b>open()</b>. Then it proceeds to read and process every line in reader
         * until readLine() returns null (EOF). The processing consists of separating the text
         * into the citation for each verse (first line of a paragraph) which it adds to the field
         * ArrayList<String> bookChapterVerse, and the lines of the text of the verse (all the
         * lines until an empty line is encountered) which it appends to the StringBuilder builder.
         * When the empty line terminating the verse is found (<b>line.length() == 0</b>), it
         * converts StringBuilder build to a String and adds it to ArrayList<String> stringList,
         * then it replaces the StringBuilder builder with a new instance of StringBuilder and
         * breaks from the verse building loop in order to start reading in the next citation and
         * verse. Once it is done reading all the lines in BufferedReader reader (reader.readLine()
         * returns null) it closes BufferedReader reader (which also closes the InputStreamReader
         * and InputStream it was reading from (I hope)), opens the ConditionVariable mDoneReading
         * and sets doneReading to true.
         */
        final Thread mThread = new Thread() {
            String line;
            StringBuilder builder = new StringBuilder();
            @Override
            public void run() {
                mDoneReading.close();
                try {
                    while ((line = reader.readLine()) != null) {
                        bookChapterVerse.add(line);
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                            if (line.length() == 0) {
                                stringList.add(builder.toString());
                                builder = new StringBuilder();
                                break;
                            } else {
                                builder.append(" ");
                            }
                        }
                    }
                    Log.i(TAG, "Verses read: " + stringList.size());
                    reader.close();
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
