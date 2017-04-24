package com.example.android.markovchain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClockTrisect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_trisect);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        addText("Hello World", linearLayout);
        Button button = addButton("Push to start clock", linearLayout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText("Well?", linearLayout);
            }
        });
    }

    /**
     * Adds a TextView containing the String text to the ViewGroup parent. First we create a TextView
     * text, then we set the text of <b>TextView text</b> to the String text, and finally we
     * add the TextView text to the ViewGroup parent (our vertical LinearLayout).
     *
     * @param text text to display in the TextView we add to ViewGroup parent
     * @param parent ViewGroup to add our TextView to
     */
    public void addText(String text, final ViewGroup parent) {
        TextView mText = new TextView(this);
        mText.setText(text);
        parent.addView(mText);
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
     * @param description text for the Button
     * @param parent the LinearLayout we are adding the Button to using <b>ViewGroup.addView</b>
     */
    public Button addButton(String description, ViewGroup parent) {
        Button button = new Button(this);
        button.setText(description);
        parent.addView(button);
        return button;
    }
}
