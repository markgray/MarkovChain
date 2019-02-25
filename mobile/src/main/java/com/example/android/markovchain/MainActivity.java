package com.example.android.markovchain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the main Activity which launches all the other Activity's when their "launching Button"
 * is clicked. Its layout file (R.layout.activity_main) consists only of a vertical
 * {@code LinearLayout} wrapped in a {@code ScrollView} and {@code Button}'s and {@code TextView}'s
 * are added to the {@code LinearLayout} using java code.
 */
public class MainActivity extends Activity {

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * {@code onCreate}, then we set our content view to our layout file R.layout.activity_main. We
     * initialize our variable {@code ViewGroup linearLayout} by finding the view in our content view
     * with id R.id.linear_layout. We then use our {@code addButton} method to add buttons that will
     * launch each of our activities to {@code linearLayout} as well as using our method {@code addText}
     * to add a {@code TextView} to separate the "featured" Activity's from experimental ones added
     * to the end of {@code linearLayout}.
     *
     * @param savedInstanceState we do not override {@code onSaveInstanceState} so do not use this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup linearLayout = findViewById(R.id.linear_layout);
        addButton(ShakespeareRecycler.class, "Shakespeare", linearLayout);
        addButton(ShakespeareMarkovRecycler.class, "Shakespeare Markov", linearLayout);
        addButton(BibleMain.class, "Bible", linearLayout);
        addButton(BibleMarkovFragment.class, "Bible Markov", linearLayout);
        addButton(ClockTrisect.class, "Clock Trisect", linearLayout);
        addButton(WhatIsMan.class, "What is man?", linearLayout);
        addText("Obsolete activities", linearLayout);
        addButton(FragmentVersionSkeleton.class, "Fragment Skeleton", linearLayout);
        addButton(TestBenchMark.class, "Test BenchMark", linearLayout);
    }

    /**
     * Constructs and adds a {@code Button} to the {@code ViewGroup parent} designed to launch a
     * different Activity when it is clicked. First we initialize our variable {@code Button button}
     * with a new instance, then we set its text to the parameter {@code String description}, and we
     * set its {@code OnClickListener} to an anonymous class which will (when the {@code Button} is
     * clicked) create an {@code Intent} to launch the {@code Activity} whose {@code Class} is given
     * in our parameter {@code Class destination} and start that {@code Activity}. Finally we add
     * {@code Button button} to the parameter {@code ViewGroup parent} (the {@code LinearLayout} in
     * our layout file in our case).
     *
     * @param destination Activity Class to be started by an Intent we create and start
     *        when the Button is clicked
     * @param description text for the Button
     * @param parent the LinearLayout we are adding the Button to using <b>ViewGroup.addView</b>
     */
    public void addButton(final Class destination, String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setText(description);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, destination);
                startActivity(intent);
            }
        });
        parent.addView(button);
    }

    /**
     * Adds a TextView containing the String text to the ViewGroup parent. First we create a TextView
     * text, then we set the text of <b>TextView text</b> to the String text, and finally we
     * add the TextView text to the ViewGroup parent (our vertical LinearLayout).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param parent ViewGroup to add our TextView to
     */
    @SuppressWarnings("SameParameterValue")
    public void addText(String text, ViewGroup parent) {
        TextView mText = new TextView(this);
        mText.setText(text);
        parent.addView(mText);
    }
}
