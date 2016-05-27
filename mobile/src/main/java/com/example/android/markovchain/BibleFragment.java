package com.example.android.markovchain;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BibleFragment extends AppCompatActivity {

    public final String TAG = "BibleRecycler";
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected BibleAdapter mAdapter;
    ArrayList<String> stringList = new ArrayList<>();
    ArrayList<String> bookChapterVerse = new ArrayList<>();
    BibleDialog bibleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
        setContentView(R.layout.activity_bible_fragment);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.bible_recyclerview);
        mAdapter = new BibleAdapter(stringList, bookChapterVerse, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void showDialog(String label, String text) {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        bibleDialog = BibleDialog.newInstance(label, text);
        bibleDialog.show(ft, "dialog");
    }

    private void dismissDiaglog() {
        bibleDialog.dismiss();
    }

    public void handleAction(View v, int spinIndex) {
        if (spinIndex == 1) {
            BibleAdapter.moveToRandom(v);
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

    public static class BibleDialog extends DialogFragment {
        String mLabel;
        String mText;

        /**
         * Create and iniitialize a BibleDialog DialogFragment
         *
         * @param label Label to use
         * @param text  Text to use
         * @return Initialized BibleDialog
         */
        static BibleDialog newInstance(String label, String text) {
            BibleDialog f = new BibleDialog();

            Bundle args = new Bundle();
            args.putString("label", label);
            args.putString("text", text);

            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mLabel = getArguments().getString("label");
            mText = getArguments().getString("text");

            setStyle(DialogFragment.STYLE_NORMAL, 0);
        }

        public String[] spinChoices = {"Dismiss", "Random verse", "Google"};
        public String spinChosen = "";
        public int spinIndex = 0;
        public AdapterView.OnItemSelectedListener spinSelected = new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinIndex = position;
                spinChosen = spinChoices[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dialog, container, false);
            View tv = v.findViewById(R.id.label);
            String dialogLabel = mLabel;
            ((TextView)tv).setText(dialogLabel);

            tv = v.findViewById(R.id.text);
            ((TextView)tv).setText(mText);


            Spinner spin = (Spinner) v.findViewById(R.id.spinner);

            ArrayAdapter<String> spinnerArrayAdapter =
                    new ArrayAdapter<>(v.getContext(),
                            android.R.layout.simple_spinner_item,
                            spinChoices);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(spinnerArrayAdapter);
            spin.setOnItemSelectedListener(spinSelected);

            // Watch for button clicks.
            Button button = (Button)v.findViewById(R.id.show);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // When button is clicked, call up to owning activity.
                    ((BibleFragment) getActivity()).handleAction(v, spinIndex);
                    ((BibleFragment)getActivity()).dismissDiaglog();
                }
            });

            return v;
        }


        public int show(FragmentTransaction ft, String dialog) {
            return super.show(ft, dialog);
        }
    }
}
