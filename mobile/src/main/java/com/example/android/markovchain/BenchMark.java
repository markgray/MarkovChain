package com.example.android.markovchain;

import android.os.SystemClock;

/**
 * This class is used to benchmark areas of code, and includes {@code start()} and {@code stop()}
 * methods to start and stop the benchmark run ({@code start()} also returns the current system time
 * in milliseconds and {@code stop()} returns the elapsed time since creation of the instance or time
 * since {@code start()} was last called.)
 */
public class BenchMark {

    /**
     * Milliseconds since boot when instance was created or {@code start()} called.
     */
    private long mStartTime;

    /**
     * Create a new {@code BenchMark} instance initialized to current time. We simply call our method
     * {@code start()} to initialize our field {@code long mStartTime} to the time in milliseconds
     * since boot.
     */
    public BenchMark() {
        this.start();
    }

    /**
     * Set timer field {@code long mStartTime} to current time in milliseconds since boot, including
     * time spent in sleep, and return that value to the caller.
     *
     * @return current time in milliseconds since boot, including time spent in sleep.
     */
    public long start() {
        return mStartTime = SystemClock.elapsedRealtime();
    }

    /**
     * Slight misnomer, clock is not stopped, but the elapsed time in milliseconds since the creation
     * of this instance or its {@code start()} method called is returned.
     *
     * @return Elapsed time in milliseconds since {@code start()} was called (or instance constructed)
     */
    public long stop() {
        return SystemClock.elapsedRealtime() - mStartTime;
    }

}
