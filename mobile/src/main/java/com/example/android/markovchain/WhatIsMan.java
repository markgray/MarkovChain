package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

public class WhatIsMan extends Activity {
    TextView whatTextView;
    TextView whatWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_is_man);
        whatTextView = findViewById(R.id.what_textView);
        whatWaiting = findViewById(R.id.what_waiting);
        initDataSet();
    }

    private void initDataSet() {
        @SuppressLint("StaticFieldLeak")
        WhatDataTask mWhatDataTask = new WhatDataTask(getApplicationContext()) {
            /**
             * <p>Runs on the UI thread after {@link #doInBackground}. The
             * specified result is the value returned by {@link #doInBackground}.</p>
             *
             * <p>This method won't be invoked if the task was cancelled.</p>
             *
             * @param s The result of the operation computed by {@link #doInBackground}.
             * @see #onPreExecute
             * @see #doInBackground
             * @see #onCancelled(Object)
             */
            @Override
            protected void onPostExecute(Spanned s) {
                whatTextView.setText(s);
                whatWaiting.setVisibility(View.GONE);
                whatTextView.setVisibility(View.VISIBLE);
            }
        };
        mWhatDataTask.execute(R.raw.chapter1);
    }

}
