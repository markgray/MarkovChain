package com.example.android.markovchain;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class FragmentVersionSkeleton extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenthost);

        // Gets an instance of the support library FragmentManager
        FragmentManager localFragmentManager = getSupportFragmentManager();
        /*
         * Adds the back stack change listener defined in this Activity as the listener for the
         * FragmentManager. See the method onBackStackChanged().
         */
        localFragmentManager.addOnBackStackChangedListener(this);
    }

    /**
     * Called whenever the contents of the back stack change.
     */
    @Override
    public void onBackStackChanged() {
        
    }
}
