package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

/**
 * This Activity lets you read Shakespeare's sonnets in a RecyclerView.
 */
public class ShakespeareRecycler extends Activity {
    /**
     * TAG for logging
     */
    public final String TAG = "ShakespeareRecycler";
    /**
     * The {@code RecyclerView} in our layout
     */
    RecyclerView mRecyclerView;
    /**
     * {@code LayoutManager} for our {@code RecyclerView} (a {@code LinearLayout} instance)
     */
    RecyclerView.LayoutManager mLayoutManager;
    /**
     * Adapter for our {@code RecyclerView}
     */
    protected StringArrayAdapter mAdapter;

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file R.layout.activity_shakespeare_recycler.
     * Next we initialize our field {@code mLayoutManage} with an instance of {@code LinearLayoutManager}.
     * We locate the view with id R.id.shakespeare_recyclerview in order to initialize our field
     * {@code RecyclerView mRecyclerView} to it. We create a new instance of {@code StringArrayAdapter}
     * using the {@code SONNETS} field of our {@code Shakespeare} class (the {@code String[]} array
     * containing Shakespeare's sonnets), and {@code mLayoutManager} as arguments to the constructor
     * and initialize our field {@code mAdapter} to it. Finally we set the adapter of our field
     * {@code RecyclerView mRecyclerView} to {@code mAdapter}, and set the layout manager for it to
     * our field {@code RecyclerView.LayoutManager mLayoutManager}.
     *
     * @param savedInstanceState we do not override onSaveInstanceState so ignore this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shakespeare_recycler);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = findViewById(R.id.shakespeare_recyclerview);
        mAdapter = new StringArrayAdapter(Shakespeare.SONNETS, mLayoutManager);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.i(TAG, "Verses read: " + Shakespeare.SONNETS.length);
    }
}
