package com.example.android.common;

import android.os.SystemClock;

@SuppressWarnings("WeakerAccess")
public class BenchMark {

    private long mStartTime; // Milliseconds since boot when instance created or start() called.

    /**
     * Create a new BenchMark instance initialized to current time. We simply call our method start()
     * to initialize our field long mStartTime to the time in milliseconds since boot.
     */
    public BenchMark() {
        this.start();
    }

    /**
     * Set timer (long mStartTime) to current time in milliseconds since boot, including time spent
     * in sleep, and return that value to caller.
     *
     * @return current time in milliseconds since boot, including time spent in sleep.
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
