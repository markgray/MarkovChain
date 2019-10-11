package com.example.android.markovchain

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * An `AsyncTask` which loads the utf8 text file with the resource ID specified by the parameter
 * passed to the method `doInBackground` in the background and returns a list of strings that
 * correspond to each of the paragraphs of the text file to the caller.
 */
open class TranscendDataTask
/**
 * Our constructor, we just save our parameter `Context context` in our field `mContext`.
 *
 * Parameter: `Context` to use to access resources of this application
 */
internal constructor(
        /**
         * `Context` to use to access resources from our application (in our case this is the
         * "context of the single, global Application object of the current process" obtained from the
         * `getApplicationContext` method of the `TranscendActivity` activity and then passed
         * to our constructor).
         */
        @field:SuppressLint("StaticFieldLeak")
        internal var mContext: Context) : AsyncTask<Int, String, List<String>>() {

    /**
     * We override this method to perform a computation on a background thread. The specified parameters
     * are the parameters passed to [.execute] by the caller of this task. We initialize our
     * variable `StringBuilder builder` with a new instance, declare `String line`, and
     * initialize our variable `List<String> results` with a new instance of `ArrayList`.
     * We use our field `Context mContext` to fetch a `Resources` instance for the application's
     * package which we then use to open a data stream for reading the raw resource with resource id
     * `resourceId[0]` for our variable `InputStream inputStream`. Then we initialize our
     * variable `BufferedReader reader` with a new instance constructed to use a new instance of
     * `InputStreamReader` created from `inputStream` using the default charset. Then wrapped
     * in a try block intended to catch and log IOException we loop while the `readLine` method
     * of `reader` returns a non-null value to set `line` to, then:
     *
     *  *
     * If the length of `line` is 0 (an empty line):
     *
     *  *
     * if the length of `builder` is not 0: we append a newline character to
     * `builder` (the end of a paragraph has occurred) and we add the string
     * value of `builder` to `results` and set the length of `builder`
     * to 0.
     *
     *  *
     * if the length of `builder` is 0: we just add the strings "\n" to `results`
     *
     *
     *
     *  *
     * If the length of `line` is NOT 0 (a non-empty line): we append `line` to
     * `builder` followed by a space character, and if `line` starts with a space
     * character (an indented line in the file) we append a newline character to `builder`
     * (so that text wrapping will not be applied to this line).
     *
     *
     * When done with the loop (`readLine` returned null indicating EOF) we close `reader`
     * and return `results` so that the `onPostExecute` override will be called with it
     * as its parameter.
     *
     * @param resourceId Resource ID of the utf8 text file we are to read in
     * @return a `List<String>` of the file read with each paragraph in a seperate string.
     */
    override fun doInBackground(vararg resourceId: Int?): List<String> {
        val builder = StringBuilder()
        var line: String?
        val results = ArrayList<String>()
        val inputStream = mContext
                .resources
                .openRawResource(resourceId[0]!!)

        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            line = reader.readLine()
            while (line != null) {
                if (line.isEmpty()) {
                    if (builder.isNotEmpty()) {
                        builder.append("\n")
                        results.add(builder.toString())
                        builder.setLength(0)
                    } else {
                        results.add("\n")
                    }
                } else {
                    builder.append(line).append(" ")
                    if (line.startsWith(" ")) {
                        builder.append("\n")
                    }
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return results
    }


    companion object {
        /**
         * TAG used for logging
         */
        internal const val TAG = "TranscendDataTask"
    }
}
