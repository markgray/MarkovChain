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
 * <b>LinearLayout</b> wrapped in a <b>ScrollView</b> and Button's and TextView's are
 * added to the LinearLayout using java code.
 */
public class MainActivity extends Activity {

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, then we set our content view to our layout file R.layout.activity_main. We locate
     * the <b>LinearLayout linearLayout</b> in our layout (R.id.linear_layout) and use the
     * reference <b>linearLayout</b> to add Button's using method <b>addButton</b> and
     * a TextView using our method <b>addText</b>. The Button's are intended to launch the
     * other Activity's in our app, and the TextView merely separates the "featured" Activity's from
     * experimental ones added to the end of the LinearLayout.
     *
     * @param savedInstanceState we do not override onSaveInstanceState so do not use this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.linear_layout);
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
     * Adds a <b>Button</b> to the <b>ViewGroup parent</b> designed to launch a different
     * Activity when it is clicked. First we create a <b>Button button</b>, then we set its
     * text to the parameter <b>String description</b>, and we set its OnClickListener to an
     * anonymous class which will (when the <b>Button</b> is clicked) create an Intent to
     * launch the <b>Activity</b> given in our parameter <b>Class destination</b> and
     * start that <b>Activity</b>. Finally it adds <b>Button button</b> to the
     * <b>ViewGroup parent</b> (our vertical <b>LinearLayout</b>)
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
