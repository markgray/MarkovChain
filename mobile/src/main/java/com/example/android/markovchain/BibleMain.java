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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    /**
     * TAG for logging
     */
    public static final String TAG = "BibleMain";
    /**
     * Key for shared preference to save verse number
     */
    public static final String LAST_VERSE_VIEWED = "LAST_VERSE_VIEWED";
    /**
     * Used to access shared preference file
     */
    private static final String CLASS = BibleMain.class.getSimpleName();
    /**
     * Application Context for {@code BibleMain} and dialogs to use when necessary
     */
    @SuppressLint("StaticFieldLeak") // Only one BibleMain ever exists so this is benign.
    public static Context bibleContext;
    /**
     * Reference to the {@code RecyclerView} in our layout
     */
    RecyclerView mRecyclerView;
    /**
     * {@code LayoutManager} for our {@code RecyclerView}
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * Flag used to signal that we are done reading in the Bible
     */
    public static boolean doneReading = false;
    /**
     * Used to block until reading is finished
     */
    public static ConditionVariable mDoneReading = new ConditionVariable();
    /**
     * The Adapter used to fill our {@code RecyclerView}
     */
    protected BibleAdapter mAdapter;
    /**
     * List of Bible verses
     */
    ArrayList<String> stringList = new ArrayList<>();
    /**
     * List of citations corresponding to each {@code stringList} verse
     */
    static ArrayList<String> bookChapterVerse = new ArrayList<>();
    /**
     * Contains reference to the {@code BibleDialog} launched by long clicking a verse
     */
    @SuppressLint("StaticFieldLeak") // TODO: Create only one BibleDialog ever!
    public static BibleDialog bibleDialog;
    /**
     * Canonical Bible citation used as label for {@code BibleDialog} and the other dialogs
     */
    public static String dialogTitle;
    /**
     * Verse text used as text contents for {@code BibleDialog} and the other dialogs
     */
    public static String dialogText;
    /**
     * Verse number used in {@code BibleDialog} and the other dialogs
     */
    public static int dialogVerse;
    /**
     * {@code TextToSpeech} instance used by {@code BibleSpeak}
     */
    public static TextToSpeech textToSpeech;

    /**
     * Called when the activity is starting. First we call our super's implementation of {@code onCreate}.
     * We set the field {@code mDoneReading} to false so that our UI thread knows to wait until our
     * text file is read into memory before trying to access the data. We reset the ConditionVariable
     * {@code mDoneReading} to the closed state so that any threads that call {@code block()} will
     * block until someone calls {@code open()}. We next set the field {@code Context bibleContext}
     * to the context of the single, global Application object of the current process. This is because
     * we will later need a {@code Context} whose lifecycle is separate from the current context, that
     * is tied to the lifetime of the process rather than the current component (I forget why we need
     * this though??). We next call our method {@code initDataSet} which will spawn a background thread
     * to read in the data file we will be using (R.raw.king_james_text_and_verse) and create the data
     * structures needed by our activity. Then we set our content View to our layout file
     * (R.layout.activity_bible_fragment), locate the {@code RecyclerView} in the layout with the id
     * R.id.bible_recyclerview and save a reference to it in our field {@code RecyclerView mRecyclerView}.
     * We initialize our field {@code BibleAdapter mAdapter} to a new instance using the List containing
     * the text ({@code ArrayList<String> stringList}), the List containing chapter and verse annotation
     * for each paragraph ({@code ArrayList<String> bookChapterVerse}), and using the layout manager
     * {@code RecyclerView.LayoutManager mLayoutManager} (an instance of {@code LinearLayoutManager}
     * created above). We now set the adapter of {@code mRecyclerView} to {@code mAdapter}, and the
     * layout manager to be used to {@code mLayoutManager}.
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
        mRecyclerView = findViewById(R.id.bible_recyclerview);
        mAdapter = new BibleAdapter(stringList, bookChapterVerse, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into the background, but
     * has not (yet) been killed. We initialize our variable {@code int lastFirstVisiblePosition} to
     * the verse number of the first completely visible verse by calling the {@code findFirstCompletelyVisibleItemPosition}
     * method of the {@code LayoutManager} of {@code mRecyclerView} then call our method {@code saveVerseNumber}
     * to save the verse number to our shared preference file under the index LAST_VERSE_VIEWED. Finally
     * we call our super's implementation of {@code onPause}.
     */
    @Override
    protected void onPause() {
        //noinspection ConstantConditions
        int lastFirstVisiblePosition = ((LinearLayoutManager)mRecyclerView
                .getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        saveVerseNumber(lastFirstVisiblePosition, LAST_VERSE_VIEWED);
        super.onPause();
    }

    /**
     * Called after {@code onRestoreInstanceState}, {@code onRestart}, or {@code onPause}, for our
     * activity to start interacting with the user.
     * <p>
     * Our first action is to initialize our variable {@code int lastFirstVisiblePosition} to the
     * number of the last verse viewed from our shared preference file by calling {@code restoreVerseNumber},
     * then we use this number to move our {@code BibleAdapter} to this verse. Finally we call our
     * super's implementation of {@code onResume}.
     */
    @Override
    protected void onResume() {
        int lastFirstVisiblePosition = restoreVerseNumber(0, LAST_VERSE_VIEWED);
        BibleAdapter.moveToVerse(mRecyclerView, lastFirstVisiblePosition);
        super.onResume();
    }

    /**
     * Perform any final cleanup before our activity is destroyed. If {@code textToSpeech} is not null
     * we have been using text to speech in {@code BibleSpeak} so we interrupt the current utterance
     * other utterances in the queue by calling the {@code stop()} method of {@code textToSpeech},
     * release the resources used by the {@code TextToSpeech} engine by calling its {@code shutdown()}
     * method and then set textToSpeech to null (unnecessary since we are in {@code onDestroy}, but
     * better safe than sorry!). Finally we call our super's implementation of of {@code onDestroy()}.
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
     * Saves the currently viewed verse (or any other int) to shared preferences file under the
     * key "key". First we initialize our variable {@code SharedPreferences pref} to a reference to
     * the preferences file for the class {@code CLASS} ({@code BibleMain}'s class). We create an
     * {@code SharedPreferences.Editor editor} from {@code pref}, use this {@code Editor} to save as
     * an int value our parameter verse using the key {@code key}, and finally commit our changes from
     * our {@code Editor} to {@code pref}. (Only memory copy is written to, an asynchronous {@code commit}
     * is started to write to disk.)
     *
     * @param verse verse number or other int
     * @param key   key to store it under (presently only "LAST_VERSE_VIEWED")
     */
    public static void saveVerseNumber(int verse, String key) {
        SharedPreferences pref = bibleContext.getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, verse);
        editor.apply();
    }

    /**
     * Retrieve the last viewed verse from shared preferences file. First we initialize our variable
     * {@code SharedPreferences pref} to a reference to the preferences file for the class {@code CLASS}
     * ({@code BibleMain} in our case), then we retrieve and return the int value stored in {@code pref}
     * under the key {@code key} (defaulting to our parameter {@code int verse} if no value is stored
     * yet).
     *
     * @param verse verse number default value to return if no value stored yet
     * @param key   key it was stored under (presently only "LAST_VERSE_VIEWED")
     * @return      verse number stored in shared preferences, or the default value passed it
     */
    @SuppressWarnings("SameParameterValue")
    public int restoreVerseNumber(int verse, String key) {
        SharedPreferences pref = getSharedPreferences(CLASS, Context.MODE_PRIVATE);
        return pref.getInt(key, verse);
    }

    /**
     * Finds the verse index number of a given standard Bible citation {@code String citation}, or of
     * the fallback citation {@code String fallback} in case there are no exact matches for the given
     * citation. We start by initializing our variable {@code int fallBackIndex} to 0 so that if neither
     * {@code citation} nor {@code fallback} is found by exact match we will return 0 (very beginning
     * of Bible). Then we loop over {@code int i} through the entire list of citations in our list
     * {@code ArrayList<String> bookChapterVerse} checking whether the {@code String candidateVerse}
     * in position {@code i} of {@code bookChapterVerse} is equal to {@code citation} (in which case
     * we immediately return the index {@code i} for the verse in question) or whether {@code candidateVerse}
     * is equal to {@code fallback} (in which case we set {@code fallBackIndex} to the index {@code i}).
     * Finally if an exact match for {@code citation} has not been found in our {@code bookChapterVerse}
     * list we return {@code fallBackIndex} to the caller.
     *
     * @param citation Bible citation we are looking for
     * @param fallback a fallback citation to use if that citation is not found
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
     * Returns the book number index for a citation {@code String citation} which uses the name instead
     * of the number. First we strip off the book name from the citation into {@code String bookLook}
     * (all characters up to the first ":"), and initialize our variable {@code int indexOf} to 0.
     * Then we "foreach loop" through all the book names {@code String book} in the array
     * {@code String[] BibleAdapter.books} incrementing {@code indexOf} every time the {@code book}
     * from {@code books} fails to match our target {@code bookLook}, and if they do match we
     * immediately return {@code indexOf} as our result. If none of the books in {@code books} match
     * we return 0.
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
     * Convenience function for starting a {@code BibleDialog} fragment. First we set our fields
     * {@code String dialogTitle} and {@code String dialogText} to our arguments for later use, then
     * we call the factory method {@code newInstance} of {@code BibleDialog} to create a new
     * {@code BibleDialog bibleDialog} using our parameters {@code label} and {@code text} for its
     * label and text and save it in our static field {@code BibleDialog bibleDialog} for later use.
     * Finally we call our overloaded method {@code showDialog(DialogFragment)} to pop up the instance
     * of BibleDialog we just created in {@code bibleDialog}.
     * TODO: Create only one BibleDialog ever!
     *
     * @param label citation for verse contained in text
     * @param text  text of current verse
     */
    public void showDialog(String label, String text) {
        dialogTitle = label;
        dialogText = text;

        // Create and show the dialog.
        bibleDialog = BibleDialog.newInstance(label, text); // TODO: Create only one BibleDialog ever!
        showDialog(bibleDialog);
    }

    /**
     * Shows the {@code DialogFragment dialogFragment} passed to it. First we fetch a reference to
     * the {@code FragmentManager} used for interacting with fragments associated with this activity
     * and use it to start a series of {@code Fragment} edit operations using our variable
     * {@code FragmentTransaction ft}. Next we initialize our variable {@code Fragment prev} by using
     * the {@code FragmentManager} to search for any existing {@code Fragment}'s with the tag "dialog"
     * (the tag used for all the {@code DialogFragment}'s we display), and if one is found ({@code prev}
     * is not equal to null) we use {@code ft} to remove it. We add {@code ft} to the BackStack so
     * that the back Button will remember this transaction once we commit the transaction, then we
     * call the {@code show} method of our parameter {{@code dialogFragment}} to display the dialog,
     * adding the fragment using {@code ft}, with the tag "dialog" and then committing that
     * {@code FragmentTransaction}.
     *
     * @param dialogFragment DialogFragment subclass which already has had setArguments
     *                       called to attach a Bundle of arguments
     * @return identifier of the committed transaction, as per {@code FragmentTransaction.commit()}.
     */
    @SuppressWarnings("UnusedReturnValue")
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
     * Convenience function to dismiss the main {@code BibleDialog} {@code BibleDialog bibleDialog}.
     * This is called only from the {@code OnClickListener} for the "DISMISS" Button in {@code BibleDialog},
     * and exists due to errors encountered calling {@code DialogFragment.dismiss()} directly from that
     * callback. We simply use our reference to it in {@code BibleDialog bibleDialog} to call its
     * {@code dismiss()} method, then set our field {@code BibleDialog bibleDialog} in hopes it will
     * be garbage collected. TODO: Create only one BibleDialog ever!
     */
    public void dismissDialog() {
        bibleDialog.dismiss();
        bibleDialog = null;
    }

    /**
     * Handle whichever action was chosen in the {@code Spinner} contained in the {@code BibleDialog}
     * fragment. This is called from {@code BibleDialog} both in the {@code onItemSelected} callback
     * for the {@code Spinner}, and in the {@code onClick} callback for the "REPEAT" Button. We switch
     * based on the parameter {@code spinIndex} which is the position of the Spinner view which was
     * selected:
     * <ul>
     *     <li>
     *         CHOICE_NONE: Do nothing, and break.
     *     </li>
     *     <li>
     *         CHOICE_RANDOM_VERSE: Instruct the BibleAdapter to move to a random verse by calling
     *         its {@code moveToRandom} method (the {@code View v} is passed so that it can toast the
     *          verse moved to), then instruct the {@code BibleDialog bibleDialog} to refresh its
     *          display of the citation and verse that was chosen, then break.
     *     </li>
     *     <li>
     *         CHOICE_GOOGLE: Launch a new instance of the {@code BibleSearch} {@code DialogFragment}
     *         using the current {@code dialogTitle} (citation) and {@code dialogText} (verse), then
     *         break.
     *     </li>
     *     <li>
     *         CHOICE_BOOKMARK: Launch a new instance of the {@code BibleBookmark} {@code DialogFragment}
     *         using the current {@code dialogTitle} (citation) and {@code dialogText} (verse), then
     *         break.
     *     </li>
     *     <li>
     *         CHOICE_GO_TO_VERSE: Launch a new instance of the {@code BibleChoose} {@code DialogFragment}
     *         using the current {@code dialogTitle} (citation) and {@code dialogText} (verse), then
     *         break.
     *     </li>
     *     <li>
     *         CHOICE_READ_ALOUD: Launch a new instance of the {@code BibleSpeak} {@code DialogFragment}
     *         using the current {@code dialogTitle} (citation) and {@code dialogText} (verse), then
     *         break.
     *     </li>
     *     <li>
     *         default: If it is none of the above, then just break, although this could only happen
     *         as the result of a programming error so we should probably throw an exception.
     *     </li>
     * </ul>
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
     * Reads the raw file king_james_text_and_verse.txt, separating it into citations (the list in
     * {@code ArrayList<String> bookChapterVerse}), and verse text (the list {@code ArrayList<String> stringList}).
     * First we open {@code InputStream inputStream} to read our copy of the Bible (R.raw.king_james_text_and_verse)
     * from our raw resource directory. Then we create {@code BufferedReader reader} from an instance of
     * {@code InputStreamReader} created from {@code inputStream} ({@code InputStreamReader} does the
     * character conversion needed to convert the byte stream coming from the {@code inputStream} to
     * characters, and {@code BufferedReader} allows us to use {@code readLine} on the characters coming
     * from the {@code InputStreamReader}). Then we set the {@code doneReading} flag to false so that
     * other code does not try to access our data until it is ready to be used. Then we create the
     * {@code Thread mThread} to read in the data from {@code reader} and start that thread running.
     */
    private void initDataSet() {
        final InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.king_james_text_and_verse);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        doneReading = false;

        /*
         * This is the thread that will do our work. First it calls <b>close()</b> on the
         * ConditionVariable mDoneReading so that other Thread's may block on mDoneReading until
         * we call <b>open()</b>. Then it proceeds to read and process every line in reader
         * until readLine() returns null (EOF). The processing consists of separating the text
         * into the citation for each verse (first line of a paragraph) which it adds to the field
         * ArrayList<String> bookChapterVerse, and the lines of the text of the verse (all the
         * lines until an empty line is encountered) which it appends to the StringBuilder builder.
         * When the empty line terminating the verse is found (<b>line.length() == 0</b>), it
         * converts StringBuilder build to a String and adds it to ArrayList<String> stringList,
         * then it sets the length of StringBuilder builder to 0 and breaks from the verse building
         * loop in order to start reading in the next citation and verse. Once it is done reading
         * all the lines in BufferedReader reader (reader.readLine() returns null) it closes
         * BufferedReader reader (which also closes the InputStreamReader and InputStream it was
         * reading from (I hope)), opens the ConditionVariable mDoneReading and sets doneReading to true.
         */
        final Thread mThread = new Thread() {
            /**
             * Line read from {@code BufferedReader reader}
             */
            String line;
            /**
             * {@code StringBuilder} used to assemble the lines of the current verse.
             */
            StringBuilder builder = new StringBuilder();
            /**
             * Called when the {@code Thread}'s {@code start()} method is called. First we call the
             * {@code close()} method of our field {@code ConditionVariable mDoneReading} so that other
             * {@code Thread}'s may block on {@code mDoneReading} until we call its {@code open()} method.
             * Then wrapped in a try block intended to catch and log IOException we read each {@code line}
             * from {@code reader} until EOF is indicated by a null return. The structure of the file
             * is such that the first line is the citation followed by the lines of the verse, terminated
             * by an empty line, so in an outer loop we add the citation {@code line} to our list of
             * citations {@code ArrayList<String> bookChapterVerse}, then in an inner loop we append the
             * {@code line} to {@code builder} then branch on whether the length of {@code line} was
             * 0 or not:
             * <ul>
             *     <li>
             *         The length of {@code line} is 0: we add the string value of {@code builder} to our
             *         list of verses {@code ArrayList<String> stringList}, set the length of {@code builder}
             *         to 0, and  break out of the inner loop to work on the next citation and its verse.
             *     </li>
             *     <li>
             *         The length of {@code line} is NOT 0: We append a space character to {@code builder}
             *     </li>
             * </ul>
             * When we have reached the EOF we log the number of verses read, close {@code reader} and
             * exit the try block. Having read in the file we open {@code ConditionVariable mDoneReading}
             * and set our done reading flag {@code boolean doneReading} to true.
             */
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
                                builder.setLength(0);
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
