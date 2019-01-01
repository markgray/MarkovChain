package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.example.android.common.Shakespeare;
import com.example.android.common.StringArrayAdapter;

/**
 * This Activity lets you read Shakespeare's sonnets in a RecyclerView.
 */
public class ShakespeareRecycler extends Activity {

    public final String TAG = "ShakespeareRecycler"; // TAG for logging
    RecyclerView mRecyclerView; // The RecyclerView in our layout
    RecyclerView.LayoutManager mLayoutManager; // LayoutManager for our RecyclerView (LinearLayout)
    protected StringArrayAdapter mAdapter; // Adapter for our RecyclerView

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, then we set our content view to our layout file R.layout.activity_shakespeare_recylcler.
     * Next we initialize our field RecyclerView.LayoutManager mLayoutManage with an instance of
     * LinearLayoutManager. We locate the RecyclerView in our layout (R.id.shakespeare_recyclerview)
     * and initialize our field RecyclerView mRecyclerView with it. We create a new instance of
     * StringArrayAdapter using Shakespeare.SONNETS (the String[] array of Shakespeare's sonnets),
     * and mLayoutManager as arguments to the constructor and initialize our field mAdapter to it.
     * Finally we set the adapter for the RecyclerView mRecyclerView to StringArrayAdapter mAdapter,
     * and set the layout manager for it to RecyclerView.LayoutManager mLayoutManager.
     *
     * @param savedInstanceState we do not override onSaveInstanceState so ignore this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_recylcler);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.shakespeare_recyclerview);
        mAdapter = new StringArrayAdapter(Shakespeare.SONNETS, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.i(TAG, "Verses read: " + Shakespeare.SONNETS.length);
    }
}
