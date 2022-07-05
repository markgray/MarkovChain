package com.example.android.markovchain.clocktrisect

import com.example.android.markovchain.util.CoroutinesAsyncTask

/**
 * The first time this background task is run it will cycle through all the seconds in a day, and if
 * the `badness` field of the best [ClockDataItem] for a particular minute is less than
 * 12 it will publish this [ClockDataItem] to the UI thread, and if not it will remove it from
 * future consideration for fractional seconds. When done it will return the [ClockDataItem]
 * that has the best `badness` field. In subsequent runs with fractional increments for the
 * seconds it only looks at minutes which survived the previous pass.
 */
open class ClockDataTask
/**
 * Our zero parameter constructor, we just call our super's constructor.
 */
() : CoroutinesAsyncTask<Array<ClockDataItem?>, ClockDataItem, ClockDataItem>() {
    /**
     * The hour we are currently working on
     */
    internal var h: Int = 0

    /**
     * The minute we are currently working on
     */
    internal var m: Int = 0

    /**
     * The second we are currently working on
     */
    internal var s: Double = 0.0

    /**
     * The fraction of a second we are to increment by
     */
    private var increment = 1.0

    /**
     * The `ClockDataItem` whose badness we are currently considering.
     */
    private var trialClock = ClockDataItem(0, 0, 0.0)

    /**
     * Our constructor which allows the value of our increment to be specified as its parameter. We
     * call our zero parameter constructor then set our field `double increment` to our parameter
     * `double inc`.
     *
     * @param inc the time increment in seconds we are to use to search with
     */
    constructor(inc: Double) : this() {
        increment = inc
    }

    /**
     * We override this method to perform a computation on a background thread. The parameters is a
     * [ClockDataItem] array containing the best trisection of the clock for every minute of
     * the half day so far passed to [execute] by the caller of this task, and entries are
     * updated here or deleted if they are too bad to be improved by another iteration (a badness
     * greater than 12 degrees cannot be improved by further fine adjustment of the second hand).
     * We call [publishProgress] to publish updates on the UI thread when a minute is found
     * to have a badness that is less than 12 (the [onProgressUpdate] override method gets called
     * with the [ClockDataItem] we pass to [publishProgress]).
     *
     * We retrieve a reference to the [Array] of [ClockDataItem] from our parameter [params] to
     * initialize our `Array<ClockDataItem?>` variable `val params`, and initialize our [Int] variable
     * `var indexToMinute` to 0 (it will point to the [ClockDataItem] of the minute whose seconds we
     * are searching for a better trisection). Then we loop over our field [h] for the 12 hours of
     * our clock, and in an inner loop loop over our field [m] for the 60 minutes in each hour:
     *  - We copy a reference to the next minute's [ClockDataItem] in the `indexToMinute` entry in
     *  `params` to our [ClockDataItem] variable `val nextClockDataItem`.
     *  - If the `nextClockDataItem` is null, we  skip it just incrementing `indexToMinute`.
     *  - Otherwise we set our field [s] to 0.0 when [increment] is 1.0 or else to the `timeSecond`
     *  field of `nextClockDataItem` minus 10 times [increment] (which is the time just before
     *  `timeSecond` from the previous search), and initialize our [Double] variable `val endSecond`
     *  to 60.0 when [increment] is 1.0 or else to the `timeSecond` field of `nextClockDataItem` plus
     *  10 times [increment] (which is the time just after `timeSecond` from the previous search).
     *  - We then loop while [s] is less than `endSecond`, setting the time in our [ClockDataItem]
     *  field [trialClock] to [h] hour, [m] minute and [s] second and if the `badness` field of
     *  [trialClock] is less than the current `badness` field of `nextClockDataItem` we clone
     *  `trialClock` into `nextClockDataItem`. In either case we then add [increment] to [s] and loop
     *  around for the next value of [s].
     *  - When done considering all of the time in the section of the minute we were searching, if
     *  the `badness` field of `nextClockDataItem` is less than 12.0 we call [publishProgress] to
     *  have the [onProgressUpdate] override output the string value of `nextClockDataItem`.
     *  - If on the other hand the `badness` is greater than or equal to 12.0 we set the [ClockDataItem]
     *  in the `indexToMinute` entry in `params` to null so it will no longer be considered (a second
     *  spans a 6 degree arc so no further fine adjustment of the second can possibly correct for
     *  this).
     *  - We then increment `indexToMinute` and loop around to explore the next minute in the
     *  [ClockDataItem] array `params`.
     *
     * When done with all the minutes in `params` we initialize our [ClockDataItem] variable
     * `val bestClock` with a new instance for the time 12:00:00 (a very bad `badness` needless to
     * say) then loop through all the [ClockDataItem] `clockDataItem` in `params` and `clockDataItem`
     * is not null we check whether its `badness` field is less than the `badness` field of
     * `bestClock` and if so we clone `clockDataItem` into `bestClock`. When done searching `params`
     * we return `bestClock` to the caller to have our [onPostExecute] override output it to the user.
     *
     * @param params a `ClockDataItem[]` array containing the best trisection for every minute
     * as calculated by previous running of `ClockDataTask`
     * @return a `ClockDataItem` representing the best trisection of the clock
     */
    override fun doInBackground(vararg params: Array<ClockDataItem?>?): ClockDataItem {
        val paramsLocal = params[0]!!
        var indexToMinute = 0
        h = 0
        while (h < 12) {
            m = 0
            while (m < 60) {
                val nextClockDataItem: ClockDataItem? = paramsLocal[indexToMinute]
                if (nextClockDataItem != null) {
                    s = when (increment) {
                        1.0 -> 0.0
                        else -> nextClockDataItem.timeSecond - 10.0 * increment
                    }
                    val endSecond: Double = when (increment) {
                        1.0 -> 60.0
                        else -> nextClockDataItem.timeSecond + 10.0 * increment
                    }
                    while (s < endSecond) {
                        trialClock[h, m] = s
                        if (trialClock.badness < nextClockDataItem.badness) {
                            nextClockDataItem.clone(trialClock)
                        }
                        s += increment
                    }
                    if (nextClockDataItem.badness < 12.0) {
                        publishProgress(nextClockDataItem)
                    } else {
                        paramsLocal[indexToMinute] = null
                    }
                    indexToMinute++
                } else {
                    indexToMinute++
                }
                m++
            }
            h++
        }

        val bestClock = ClockDataItem(0, 0, 0.0)
        for (clockDataItem in paramsLocal) {
            if (clockDataItem != null) {
                if (clockDataItem.badness < bestClock.badness) {
                    bestClock.clone(clockDataItem)
                }
            }
        }

        return bestClock
    }

    /**
     * Initializes our results. First we initialize our [ClockDataItem] array variable `val minuteBestClock`
     * by allocating room for 720 *null* [ClockDataItem] objects, and initialize `int indexToMinute`
     * to 0. We loop over [Int] `i` for all 12 hours, and in an inner loop we then loop over [Int] `j`
     * for the sixty minutes in an hour initializing the `indexToMinute` entry in `minuteBestClock`
     * with a new instance whose hour is `i`, whose minute is `j` and whose second is 0. We then
     * increment `indexToMinute` and loop back for the next minute. When done we return `minuteBestClock`
     * to the caller.
     *
     * @return an `ClockDataItem[]` array that has an entry for every minute
     */
    fun init(): Array<ClockDataItem?> {
        val minuteBestClock = arrayOfNulls<ClockDataItem>(720)
        var indexToMinute = 0
        for (i in 0..11) {
            for (j in 0..59) {
                minuteBestClock[indexToMinute] = ClockDataItem(i, j, 0.0)
                indexToMinute++
            }
        }
        return minuteBestClock
    }

}
