package com.example.android.common;

import android.os.SystemClock;

public class BenchMark {

    private long mStartTime;

    /**
     * Create a new BenchMark instance initialized to current time
     */
    public BenchMark() {
        this.start();
    }

    /**
     * Set timer to current timee
     *
     * @return current time
     */
    public long start() {
        return mStartTime = SystemClock.elapsedRealtime();
    }

    /**
     * Slight misnomer, clock is not stopped, but the elapsed time in milliseconds since the creation
     * or start() of the clock is returned.
     *
     * @return Elapsed time in milliseconds since start() was called
     */
    public long stop() {
        return SystemClock.elapsedRealtime() - mStartTime;
    }

}
