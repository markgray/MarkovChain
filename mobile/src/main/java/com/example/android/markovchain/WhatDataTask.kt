package com.example.android.markovchain

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * An [AsyncTask] which loads the Html file with the resource ID specified by the parameter
 * passed to the method [doInBackground] by our `execute` method in the background and returns
 * a [Spanned] string object containing the rendered Html file to the caller.
 */
open class WhatDataTask
/**
 * Our constructor, we just save our [Context] parameter `context` in our field [mContext].
 *
 * Parameter: [Context] to use to access resources of this application
 */
internal constructor(
        /**
         * [Context] to use to access resources from our application (in our case this is the
         * "context of the single, global Application object of the current process" obtained from the
         * `getApplicationContext` method of the `WhatIsMan` activity and then passed to our
         * constructor).
         */
        @field:SuppressLint("StaticFieldLeak")
        var mContext: Context) : AsyncTask<Int, String, Spanned>() {

    /**
     * Loads a Html file from our resources on a background thread and returns a [Spanned] string
     * created from the contents of the file we load. The parameter is the resource ID of the file
     * passed to [execute] by the caller of this task. First we initialize our [StringBuilder]
     * variable `var builder` to null, declare our [String] variable `var line`, and initialize
     * our [Int] variable `var sizeOfInputStream` to 0. We initialize our `InputStream` variable
     * `val inputStream` by fetching a `Resources` instance for the application's package as
     * returned by the `getResources` method of our field [mContext], and using that `Resources`
     * instance open a data stream for reading the raw resource with resource ID `resourceId[0]`.
     * Next we initialize our [BufferedReader] variable `val reader` with a buffering character
     * input stream that uses a default sized input buffer to read from an `InputStreamReader`,
     * constructed to read bytes from `inputStream` and decode them into characters using the
     * platform's default charset.
     *
     * Having set everything up, wrapped in a try block intended to catch and log IOException, we set
     * our [Int] variable `sizeOfInputStream` to an estimate of the number of bytes that can be read
     * from `inputStream` and allocate an initial capacity of 80 more than that value for
     * `builder`. We then loop setting `line` to the `String` returned by the `readLine` method of
     * `reader` until it returns *null*, appending each `line` read to `builder`. When done reading
     * the entire file into `builder` we close `reader`.
     *
     * Upon exiting from the try block we return the [Spanned] string created by the [Html.fromHtml]
     * method from the string value of `builder`.
     *
     * @param resourceId the resource ID of the Html file we are to load.
     * @return A [Spanned] string created from the contents of the file we load.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground(vararg resourceId: Int?): Spanned {
        var builder: StringBuilder? = null
        var line: String?
        var sizeOfInputStream = 0
        val inputStream = mContext
                .resources
                .openRawResource(resourceId[0]!!)

        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            sizeOfInputStream = inputStream.available() // Get the size of the stream
            builder = StringBuilder(sizeOfInputStream + 80)
            line = reader.readLine()
            while (line != null) {
                builder.append(line)
                line = reader.readLine()
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        assert(builder != null)
        Log.i(TAG, "sizeOfInputStream: " + sizeOfInputStream + " Size of builder: " + builder!!.capacity())
        return Html.fromHtml(builder.toString(), 0)
    }

    /**
     * Our static constant.
     */
    companion object {
        /**
         * TAG used for logging
         */
        internal const val TAG = "WhatDataTask"
    }
}
