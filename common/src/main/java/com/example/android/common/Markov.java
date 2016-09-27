package com.example.android.common;

import android.util.Log;
import android.view.View;

import java.io.*;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Markov {
    static final String TAG = "Markov";
    public Chain chain;
    private DoneListener doneListener;
    private View view;

    /**
     * Sets the DoneListener doneListener and View view for this instance of Markov.
     *
     * @param doneListener DoneListener for thread calling us, we will call doneListener.onDone(view)
     *                     on the UI thread when long running process finishes.
     * @param view View that will be passed to onDone(View), used for context
     */
    public void setDoneListener(DoneListener doneListener, View view) {
        this.view = view;
        this.doneListener = doneListener;
    }

    /**
     * This method initializes this instance of Markov by reading an existing pre-built Markov chain
     * from a BufferedReader. It is currently called only from BibleMarkovFragment in order to read
     * the state table for the King James Bible using a BufferedReader created from an
     * InputStreamReader which is created from an InputStream for the file R.raw.king_james_state_table,
     * but any kind of BufferedReader will work.
     *
     * @param reader BufferedReader to read a existing Markov chain into
     * @throws IOException
     */
    public void load (BufferedReader reader) throws IOException {
        chain = new Chain();
        chain.loadStateTable(reader);
    }

    /**
     * This method initializes this instance of Markov by reading text from a reader and then
     * building a Markov chain from that text. It is currently called only with a StringReader from
     * ShakespeareMarkovRecycler but any kind of Reader could be used.
     *
     * @param reader Reader of raw text to read in order to construct  construct the Markov chain
     *               our instance will use.
     * @throws IOException
     */
    public void make (Reader reader) throws IOException {
        chain = new Chain();
        chain.build(reader);
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instances's
     * state table. Simply a convenience function to access the Chain.line() method of our
     * instance's Chain chain instance.
     *
     * @return The next sentence generated from the Markov chain.
     */
    public String line() {
        return chain.line();
    }

    /**
     * This class is used to contain, build and interact with the Markov chain state table
     * maintained in Hashtable<Prefix, String[]> stateTable
     */
    public class Chain {
        static final String NONWORD = "%"; // "word" that can't appear
        Hashtable<Prefix, String[]> stateTable = new Hashtable<>(); // key = Prefix, value = suffix Array
        Prefix prefix = new Prefix(NONWORD); // initial prefix
        Random rand = new Random(); // Used to pick a random word from suffix array to follow up the Prefix
        boolean firstLine = true; // Used in method init() to decide if the Prefix prefix needs to be reset to NONWORD (TODO: necessary?)
        public boolean loaded = false; // set once chain is loaded

        /**
         * Build State table from Reader input stream. First we check the boolean field "loaded" and
         * if it is true, we return without doing anything. We create a StreamTokenizer st from the
         * Reader quotes parameter, specify that all characters shall be treated as ordinary characters
         * by calling StreamTokenizer.resetSyntax(), specify that all characters shall be treated as
         * word characters by the tokenizer (a word consists of a word character followed by zero or
         * more word or number characters), and finally specify that all characters between 0 and the
         * the space character are to be treated as whitespace characters. Having initialized our
         * StreamTokenizer to our liking we read and parse the entire Reader quotes into words
         * (stopping when the word is of ttype StreamTokenizer.TT_EOF (the end of the stream)), and
         * "add" each word to our state table by adding the word to the end of our current Prefix
         * String[] array of suffixes, and updating our Prefix prefix using the new word as the second
         * word of "prefix" and the old second word as the first word. When TT_EOF is reached we add
         * a NONWORD as the last word of our state table, set loaded to "true" and if our caller has
         * registered an OnDoneListener by calling Markov.setOnDoneListener we call the callback
         * onDone of that OnDoneListener passing it the view passed to setOnDoneListener.
         *
         * @param quotes Reader which is read and parsed into words which are then added to the state table
         * @throws IOException
         */
        void build(Reader quotes) throws IOException {
            if (loaded) return;
            StreamTokenizer st = new StreamTokenizer(quotes);
            int wordsRead = 0;

            st.resetSyntax();                     // remove default rules
            st.wordChars(0, Character.MAX_VALUE); // turn on all chars
            st.whitespaceChars(0, ' ');           // except up to blank
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                add(st.sval);
                if (st.sval.equals(NONWORD)){ // TODO: this should be an asserts
                    Log.i(TAG, "NONWORD occurs in input");
                }

                wordsRead++;
            }
            Log.i(TAG, "Words read: " + wordsRead);
            add(NONWORD);
            loaded = true;
            if (doneListener != null) {
                doneListener.onDone(view);
            }

        }

        /**
         * Reads in a Markov chain state table which has been prepared offline and loads it into our
         * Hashtable<Prefix, String[]> stateTable. First we check to make sure it has not already
         * been loaded and if so return having done nothing. Then we initialize Prefix prefix to
         * the starting point of [NONWORD, NONWORD]. Then we read our BufferedReader reader line by
         * line until there are no more lines to read, splitting each line read using space as our
         * delimiter into String words[]. We use the first two words of words[] as our Prefix, and
         * the entire array of words[] as the suffix (wastes two entries in suffix for the sake of
         * speed) and put() the parsed line into our Hashtable<Prefix, String[]> stateTable. This
         * read loop is surrounded by a try block to catch IOException. When done reading in,the
         * Markov chain state table we set loaded to true, and if our caller has registered an
         * OnDoneListener by calling Markov.setOnDoneListener we call the callback onDone of that
         * OnDoneListener passing it the view passed to setOnDoneListener.
         *
         * @param reader BufferedReader for a pre-parsed Markov chain state table
         */
        void loadStateTable(BufferedReader reader) {
            if (loaded) return;
            String line;
            prefix.pref = new String[2];
            prefix.pref[0] = NONWORD;
            prefix.pref[1] = NONWORD;
            try {
                while ((line = reader.readLine()) != null) {
                    String words[] = line.split(" ");
                    prefix.pref[0] = words[0];
                    prefix.pref[1] = words[1];
                    stateTable.put(new Prefix(prefix), words);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            loaded = true;
            if (doneListener != null) {
                doneListener.onDone(view);
            }
        }

        // Chain add: add word to suffix list, update prefix
        void add(String word) {

            String[] suf = stateTable.get(prefix);
            if (suf == null) {
                suf = new String[3];
                suf[0] = prefix.pref[0];
                suf[1] = prefix.pref[1];
                suf[2] = word;
                stateTable.put(new Prefix(prefix), suf);
            } else {
                String[] newSuf = new String[suf.length + 1];
                System.arraycopy(suf, 0, newSuf, 0, suf.length);
                newSuf[suf.length] = word;
                stateTable.put(prefix, newSuf);
            }
            prefix.pref[0] = prefix.pref[1];
            prefix.pref[1] = word;
        }

        void init() {
            if (firstLine) {
                prefix = new Prefix(NONWORD);
                firstLine = false;
            }
        }

        boolean notEnd(String word) {
            return !(word.contains(".") || word.contains("?") || word.contains("!"));
        }

        private String capitalize(final String line) {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }

        public String line() {
            StringBuilder builder = new StringBuilder(120);
            String suf = "";
            int r;

            init();
            while (notEnd(suf)) {
                String[] s = stateTable.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                    return "Error!";
                }
                r = Math.abs(rand.nextInt()) % (s.length - 2);
                suf = s[r + 2];

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.length);
                    prefix = new Prefix(NONWORD);
                }
                builder.append(suf).append(" ");
                prefix.pref[0] = prefix.pref[1];
                prefix.pref[1] = suf;
            }
            return capitalize(builder.toString());
        }

    }

    public class Prefix {
        public String[] pref;    // NPREF adjacent words from input
        static final int MULTIPLIER = 31;    // for hashCode()

        // Prefix constructor: duplicate existing prefix
        Prefix(Prefix p) {
            pref = new String[2];
            pref[0] = p.pref[0];
            pref[1] = p.pref[1];
        }

        // Prefix constructor: n copies of str
        Prefix(String str) {
            pref =  new String[2];
            pref[0] = str;
            pref[1] = str;
        }

        // Prefix hashCode: generate hash from all prefix words
        @Override
        public int hashCode() {
            int h = 0;

            for (int i = 0; i < 2; i++)
                h = MULTIPLIER * h + pref[i].hashCode();
            return h;
        }

        // Prefix equals: compare two prefixes for equal words
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Prefix)) {
                return false;
            }
            Prefix p = (Prefix) o;
            return (p.pref[0].equals(this.pref[0]) && p.pref[1].equals(this.pref[1]));
        }

    }

}
