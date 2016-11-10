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
 * <code>LinearLayout</code> wrapped in a <code>ScrollView</code> and Button's and TextView's are
 * added to the LinearLayout using java code.
 */
public class MainActivity extends Activity {

    /**
     * Called when the activity is starting. First we call through to our super's implementation of
     * onCreate, then we set our content view to our layout file R.layout.activity_main. We locate
     * the <code>LinearLayout linearLayout</code> in our layout (R.id.linear_layout) and use the
     * reference <code>linearLayout</code> to add Button's using method <code>addButton</code> and
     * a TextView using our method <code>addText</code>. The Button's are intended to launch the
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
        addButton(ShakespeareRecylcler.class, "Shakespeare", linearLayout);
        addButton(ShakespeareMarkovRecycler.class, "Shakespeare Markov", linearLayout);
        addButton(BibleMain.class, "Bible", linearLayout);
        addButton(BibleMarkovFragment.class, "Bible Markov", linearLayout);
        addText("Obsolete activities", linearLayout);
        addButton(FragmentVersionSkeleton.class, "Fragment Skeleton", linearLayout);
        addButton(TestBenchMark.class, "Test BenchMark", linearLayout);
    }

    /**
     * Adds a <code>Button</code> to the <code>ViewGroup parent</code> designed to launch a different
     * Activity when it is clicked. First we create a <code>Button button</code>, then we set its
     * text to the parameter <code>String description</code>, and we set its OnClickListener to an
     * anonymous class which will (when the <code>Button</code> is clicked) create an Intent to
     * launch the <code>Activity</code> given in our parameter <code>Class destination</code> and
     * start that <code>Activity</code>. Finally it adds <code>Button button</code> to the
     * <code>ViewGroup parent</code> (our vertical <code>LinearLayout</code>)
     *
     * @param destination Activity Class to be started by an Intent we create and start
     *        when the Button is clicked
     * @param description text for the Button
     * @param parent the LinearLayout we are adding the Button to using <code>ViewGroup.addView</code>
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
     * text, then we set the text of <code>TextView text</code> to the String text, and finally we
     * add the TextView text to the ViewGroup parent (our vertical LinearLayout).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param parent ViewGroup to add our TextView to
     */
    public void addText(String text, ViewGroup parent) {
        TextView mText = new TextView(this);
        mText.setText(text);
        parent.addView(mText);
    }
}
