package com.example.android.common;

import android.os.SystemClock;

public class BenchMark {

    private long mStartTime;

    public BenchMark() {
        this.Start();
    }

    public long Start() {
        return mStartTime = SystemClock.elapsedRealtime();
    }

    public long Stop() {
        return SystemClock.elapsedRealtime() - mStartTime;
    }

}
