package com.example.android.markovchain;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.common.Shakespeare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends WearableActivity {

    /**
     * Date format used to update the time displayed in <b>TextView mClockView</b>
     */
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);

    private Random rand; // Random instance for choosing sonnets from Shakespeare.SONNETS[]
    private BoxInsetLayout mContainerView; // R.id.container main View in our layout
    private TextView mTextView; // TextView we write our sonnets to
    private TextView mClockView;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setOnLongClickListener(listener);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mClockView = (TextView) findViewById(R.id.clock);
        rand = new Random();

        setRandomText();
    }

    View.OnLongClickListener listener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            setRandomText();
            mScrollView.scrollTo(0, 0);
            return true;
        }
    };

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            //noinspection deprecation
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            //noinspection deprecation
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            //noinspection deprecation
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }

    private void setRandomText() {
        int selection = Math.abs(rand.nextInt()) % Shakespeare.SONNETS.length;
        mTextView.setText(Shakespeare.SONNETS[selection]);
    }
}
