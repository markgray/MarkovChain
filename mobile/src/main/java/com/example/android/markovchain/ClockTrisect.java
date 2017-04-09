package com.example.android.markovchain;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClockTrisect extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_trisect);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        addText("Hello World", linearLayout);
    }

    /**
     * Adds a TextView containing the String text to the ViewGroup parent. First we create a TextView
     * text, then we set the text of <b>TextView text</b> to the String text, and finally we
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
