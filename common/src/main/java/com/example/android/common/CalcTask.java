package com.example.android.common;

import android.os.AsyncTask;

/**
* Created by markgray on 2/21/15.
*/
public class CalcTask extends AsyncTask<Long, Long, Double> {
    protected Double doInBackground(Long... reps) {
        Long repeats = reps[0];
        double acc = 1.000000001;
        double div = 0.999999999;
        for (int i = 0; i < repeats ; i++) {
            acc = acc / div;
        }
        return acc;
    }

}
