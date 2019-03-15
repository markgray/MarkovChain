package com.example.android.markovchain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@code AsyncTask} which loads the utf8 text file with the resource ID specified by the parameter
 * passed to the method {@code doInBackground} in the background and returns a list of strings that
 * correspond to each of the paragraphs of the text file to the caller.
 */
@SuppressWarnings("WeakerAccess")
public class TranscendDataTask extends AsyncTask<Integer, String, List<String>> {
    /**
     * TAG used for logging
     */
    static final String TAG = "TranscendDataTask";
    /**
     * {@code Context} to use to access resources from our application (in our case this is the
     * "context of the single, global Application object of the current process" obtained from the
     * {@code getApplicationContext} method of the {@code TranscendActivity} activity and then passed
     * to our constructor).
     */
    @SuppressLint("StaticFieldLeak")
    Context mContext;

    /**
     * Our constructor, we just save our parameter {@code Context context} in our field {@code mContext}.
     *
     * @param context {@code Context} to use to access resources of this application
     */
    TranscendDataTask(Context context) {
        mContext = context;
    }

    /**
     * We override this method to perform a computation on a background thread. The specified parameters
     * are the parameters passed to {@link #execute} by the caller of this task. We initialize our
     * variable {@code StringBuilder builder} with a new instance, declare {@code String line}, and
     * initialize our variable {@code List<String> results} with a new instance of {@code ArrayList}.
     * We use our field {@code Context mContext} to fetch a {@code Resources} instance for the application's
     * package which we then use to open a data stream for reading the raw resource with resource id
     * {@code resourceId[0]} for our variable {@code InputStream inputStream}. Then we initialize our
     * variable {@code BufferedReader reader} with a new instance constructed to use a new instance of
     * {@code InputStreamReader} created from {@code inputStream} using the default charset. Then wrapped
     * in a try block intended to catch and log IOException we loop while the {@code readLine} method
     * of {@code reader} returns a non-null value to set {@code line} to, then:
     * <ul>
     *     <li>
     *         If the length of {@code line} is 0 (an empty line):
     *         <ul>
     *             <li>
     *                if the length of {@code builder} is not 0: we append a newline character to
     *                {@code builder} (the end of a paragraph has occurred) and we add the string
     *                value of {@code builder} to {@code results} and set the length of {@code builder}
     *                to 0.
     *             </li>
     *             <li>
     *                 if the length of {@code builder} is 0: we just add the strings "\n" to {@code results}
     *             </li>
     *         </ul>
     *     </li>
     *     <li>
     *         If the length of {@code line} is NOT 0 (a non-empty line): we append {@code line} to
     *         {@code builder} followed by a space character, and if {@code line} starts with a space
     *         character (an indented line in the file) we append a newline character to {@code builder}
     *         (so that text wrapping will not be applied to this line).
     *     </li>
     * </ul>
     * When done with the loop ({@code readLine} returned null indicating EOF) we close {@code reader}
     * and return {@code results} so that the {@code onPostExecute} override will be called with it
     * as its parameter.
     *
     * @param resourceId Resource ID of the utf8 text file we are to read in
     * @return a {@code List<String>} of the file read with each paragraph in a seperate string.
     */
    @Override
    protected List<String> doInBackground(Integer... resourceId) {
        StringBuilder builder = new StringBuilder();
        String line;
        List<String> results = new ArrayList<>();
        final InputStream inputStream = mContext
                .getResources()
                .openRawResource(resourceId[0]);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {

            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    if (builder.length() != 0){
                        builder.append("\n");
                        results.add(builder.toString());
                        builder.setLength(0);
                    } else {
                        results.add("\n");
                    }
                } else {
                    builder.append(line).append(" ");
                    if (line.startsWith(" ")){
                        builder.append("\n");
                    }
                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
