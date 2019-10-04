package com.example.android.markovchain;

import android.util.Log;
import android.view.View;

import java.io.*;
import java.util.*;

/**
 * This class is inspired by the Markov chain example source code that was written for the book
 * "The Practice of Programming". It consists of methods to build and access a table which is
 * indexed by two words that occur one after the other in a text of literature, and when those
 * two words are used to access the table an array of all the third words that occur after the
 * first two words is returned.
 */
@SuppressWarnings("WeakerAccess")
public class Markov {
    /**
     * Used for log.i calls
     */
    static final String TAG = "Markov";
    /**
     * Our instance's Markov {@code Chain} instance
     */
    public Chain chain;
    /**
     * Optional callback class instance to invoke when class is ready to be used
     */
    private DoneListener doneListener;
    /**
     * Optional View for above DoneListener to use for Toast context
     */
    private View view;

    /**
     * Sets the fields {@code DoneListener doneListener} and {@code View view} for this instance of
     * {@code Markov}.
     *
     * @param doneListener DoneListener for thread calling us, we will call the {@code onDone(view)}
     *                     method of {@code doneListener} on the UI thread when one of our long
     *                     running processes finishes (either {@code build} or {@code loadStateTable})
     * @param view         {@code View} that will be passed to {@code onDone}, it can then be used
     *                     for {@code Context} in any {@code onDoneDo} override of {@code doneListener}
     */
    public void setDoneListener(DoneListener doneListener, View view) {
        this.view = view;
        this.doneListener = doneListener;
    }

    /**
     * This method initializes this instance of {@code Markov} by reading an existing pre-built Markov
     * chain from a {@code BufferedReader}. It is currently called only from {@code BibleMarkovFragment}
     * in order to read the state table for the King James Bible using a {@code BufferedReader} created
     * from an {@code InputStreamReader} which is created from an {@code InputStream} for the file
     * R.raw.king_james_state_table, but any kind of {@code BufferedReader} will work. We initialize
     * our field {@code Chain chain} with a new instance then call its {@code loadStateTable} method
     * to have it read our parameter {@code reader} and parse each line to fill its {@code stateTable}
     * hash table field with the information contained in {@code reader}.
     *
     * @param reader BufferedReader to read an existing Markov chain from
     */
    public void load (BufferedReader reader) {
        chain = new Chain();
        chain.loadStateTable(reader);
    }

    /**
     * This method initializes this instance of {@code Markov} by reading text from a {@code Reader}
     * and then building a Markov chain from that text. It is currently called only with a {@code StringReader}
     * from {@code ShakespeareMarkovRecycler} but any kind of {@code Reader} could be used. We initialize
     * our field {@code Chain chain} with a new instance then call its {@code build} method to have
     * it read our parameter {@code reader} and parse each line to fill its {@code stateTable} hash
     * table field with the state table calculated from the text of {@code reader}.
     *
     * @param reader {@code Reader} of raw text to read in order to construct the Markov {@code Chain}
     *               our instance will use.
     * @throws IOException If an I/O error occurs
     */
    public void make (Reader reader) throws IOException {
        chain = new Chain();
        chain.build(reader);
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table. Simply a convenience function to access the {@code line()} method of our instance's
     * {@code Chain chain} instance. UNUSED apparently
     *
     * @return The next sentence generated from the Markov chain.
     */
    public String line() {
        return chain.line();
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table, with its parameter used to store statistics about how many other random sentences could
     * have been generated at this point instead of the one returned. Simply a convenience function
     * to access the {@code line(MarkovStats)} method of our instance's {@code Chain chain} instance.
     *
     * @param possibles a {@code MarkovStats} instance to contain info about how many possible random
     *                  lines could have been generated at this point instead of the one returned.
     * @return The next sentence generated from the Markov chain.
     */
    public String line(MarkovStats possibles) {
        return chain.line(possibles);
    }

    /**
     * This class is used to contain, build and interact with the Markov chain state table maintained
     * in its {@code Hashtable<Prefix, String[]> stateTable} field.
     */
    public class Chain {
        /**
         * "word" that can't appear
         */
        static final String NONWORD = "%";
        /**
         * {@code Hashtable} which is accessed by a {@code Prefix} key constructed from the last two
         * words used to construct a sentence, and whose value is an array of words which have been
         * known to follow those two words in the original source text.
         */
        Hashtable<Prefix, String[]> stateTable = new Hashtable<>();
        /**
         * {@code Prefix} that we are currently working with, the initial prefix is two NONWORD "words"
         */
        Prefix prefix = new Prefix(NONWORD);
        /**
         * Used to pick a random word from suffix array to follow up the Prefix
         */
        Random rand = new Random();
        /**
         * Used in {@code init()} method to decide if the {@code Prefix prefix} needs to be reset to
         * (NONWORD,NONWORD) (The first line indicator).
         */
        boolean firstLine = true;
        /**
         * set to true once our {@code Chain} is loaded with data.
         */
        public boolean loaded = false;

        /**
         * Build State table from {@code Reader quotes} input stream. First we check if our boolean
         * field {@code loaded} is true and if it is true, we return without doing anything. Otherwise
         * we create a {@code StreamTokenizer st} from the parameter {@code quotes}, specify that all
         * characters shall be treated as ordinary characters by calling the {@code resetSyntax()}
         * method of {@code st}, specify that all characters shall be treated as word characters by
         * the tokenizer (a word consists of a word character followed by zero or more word or number
         * characters), and finally specify that all characters between 0 and the the space character
         * are to be treated as whitespace characters. Having initialized our {@code StreamTokenizer}
         * to our liking we read and parse the entire {@code Reader quotes} into words (stopping when
         * the word is of type StreamTokenizer.TT_EOF (the end of the stream)), and "add" each word to
         * our state table by calling our {@code add} method to add the word parsed into the {@code sval}
         * field of {@code st} to the end of the current {@code Prefix prefix} array of suffixes, then
         * updating {@code prefix} to use the new word as the second word of the "prefix" and the old
         * second word as the first word. When TT_EOF is reached we add a NONWORD as the last word of
         * our state table, set {@code loaded} to "true" and if our caller has registered an {@code OnDoneListener}
         * by calling our {@code setOnDoneListener} method we call the callback {@code onDone} of that
         * {@code OnDoneListener doneListener} passing it the view passed to {@code setOnDoneListener}.
         *
         * @param quotes Reader which is read and parsed into words which are then added to the state table
         * @throws IOException If an I/O error occurs
         */
        void build(Reader quotes) throws IOException {
            if (loaded) return;

            StreamTokenizer st = new StreamTokenizer(quotes);
            st.resetSyntax();                     // remove default rules
            st.wordChars(0, Character.MAX_VALUE); // turn on all chars
            st.whitespaceChars(0, ' ');           // except up to blank

            int wordsRead = 0; // for debugging only
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                add(st.sval);
                if (st.sval.equals(NONWORD)){ // for debugging only
                    Log.i(TAG, "NONWORD occurs in input");
                }

                wordsRead++; // for debugging only
            }
            Log.i(TAG, "Words read: " + wordsRead); // for debugging only
            add(NONWORD);
            loaded = true;
            if (doneListener != null) {
                doneListener.onDone(view);
            }

        }

        /**
         * Reads in a Markov chain state table which has been prepared offline and loads it into our
         * {@code Hashtable<Prefix, String[]> stateTable}. First we check to see if it has already
         * been {@code loaded} and if so return having done nothing. Otherwise we declare {@code String line},
         * allocate two strings for the {@code String[] pref} field of {@code Prefix prefix} and set
         * them to the starting point of [NONWORD, NONWORD]. Then wrapped in a try block intended
         * to catch and log IOException we read our {@code BufferedReader reader} parameter line by
         * line until there are no more lines to read, splitting each line read using space as our
         * delimiter into {@code String words[]}. We use the first two words of {@code words[]} as
         * the {@code pref} field of our field {@code Prefix prefix}, and the entire array of
         * {@code words[]} as the suffix (wastes two entries in the suffix for the sake of speed) and
         * {@code put()} the parsed line into our {@code Hashtable<Prefix, String[]> stateTable}.
         * When done reading in the Markov chain state table we set loaded to true, and if our caller
         * has registered an {@code OnDoneListener} by calling our {@code setOnDoneListener} method
         * we call the callback {@code onDone} of that {@code OnDoneListener doneListener} passing
         * it the {@code View} passed to {@code setOnDoneListener}.
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
                    String[] words = line.split(" ");
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

        /**
         * Chain add: add word to suffix list of {@code Prefix prefix}, and update {@code prefix}.
         * First we retrieve the suffix array {@code String[] suf} for the current {@code Prefix prefix}
         * from our state table {@code Hashtable<Prefix, String[]> stateTable}, and if it is null (no
         * prior occurrence of {@code prefix} encountered) we allocate a {@code String[3]} array for
         * {@code suf}, set {@code suf[0]} to the first word of the current prefix, {@code suf[1]}
         * to the second word of prefix, and set {@code suf[2]} to our argument {@code String word},
         * and then we create a new {@code Prefix} from the current {@code Prefix prefix} and store
         * the {@code suf} array into our {@code stateTable} using {@code prefix} as the key. On the
         * other hand if {@code suf} is not null we need to add the new word to the end of the
         * {@code String suf[]} array, and to do this we first create a {@code String[] newSuf} that
         * is one String longer than the current {@code suf}, copy the current {@code suf} to {@code newSuf},
         * place our argument {@code String word} as the last entry in {@code newSuf} and then use
         * {@code newSuf} to replace the current entry in our {@code stateTable} for the {@code Prefix prefix}.
         * Finally we update the current {@code prefix} by setting the first word of {@code prefix}
         * ({@code prefix.pref[0]}) to the second word ({@code prefix.pref[1]}), and the second word
         * of {@code prefix} ({@code prefix.pref[1]}) to the argument {@code String word} passed to
         * us by our caller.
         *
         * @param word Word to be added to the suffix list of the current Prefix prefix
         */
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

        /**
         * If {@code firstLine} is true (we are processing the first line of original text) we initialize
         * {@code Prefix prefix} with a new instance of {@code Prefix} pointing to the first entry in the
         * Markov chain state table: ["%", "%"], and set {@code firstLine} to false.
         */
        void init() {
            if (firstLine) {
                prefix = new Prefix(NONWORD);
                firstLine = false;
            }
        }

        /**
         * This method is used to check if the word passed it was not an end of sentence word in the
         * original text (does not contain ".", "?", or "!") and should just be added to the line
         * being formed (returns true). If it does contain ".", "?", or "!" it returns false and
         * the caller should start a new line after adding the word to the line being formed.
         *
         * @param word Word to check for end of sentence punctuation.
         * @return true if String word is not an end of sentence word, false if it is.
         */
        boolean notEnd(String word) {
            return !(word.contains(".") || word.contains("?") || word.contains("!"));
        }

        /**
         * This method is used to capitalize the first word of a sentence. It does this by isolating
         * the char at position 0 (first char of the line), feeding it to the {@code toUpperCase} method
         * of {@code Character} and concatenating that capitalized char with the rest of the original
         * line starting at position 1 (second char of line.)
         *
         * @param line String which needs the first letter capitalized (Beginning word of sentence).
         * @return String which consists of the capitalization of the first char concatenated with
         *         the rest of the line
         */
        private String capitalize(final String line) {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }

        /**
         * Uses the Markov chain state table to generate a single random sentence. First we do some
         * initialization: create {@code StringBuilder builder} with an initial capacity of 120
         * characters, initialize our variable {@code String suf} to the empty string, declare
         * {@code int r} to hold our random number, and call {@code init} to make sure {@code Prefix prefix}
         * is already initialized. Then while our {@code String suf} is not an end of sentence word
         * in the original text, we loop first fetching the suffix {@code String[] s} array of the
         * current {@code Prefix prefix} from the state table, performing a sanity check to make sure
         * there is an entry for {@code prefix} (Returning the string "Error!" as our line if there
         * is none). Then we choose a random word from the {@code String[]} array {@code s} and set
         * {@code String suf} to it. If {@code suf} is a NONWORD (NONWORD is stored as a suffix entry
         * when the current two word {@code Prefix prefix} occurs as the last two words in the original
         * text) we reset the current {@code Prefix prefix} to the beginning of the state table
         * ([NONWORD, NONWORD]) thus starting another pass through the table to look for a new value
         * of {@code suf}. If {@code suf} is not NONWORD, we append {@code suf} to the end of {@code builder}
         * followed by a space character, set the first word of {@code prefix} ({@code prefix.pref[0]})
         * to the second word ({@code prefix.pref[1]}) and set the second word ({@code prefix.pref[1]})
         * to {@code suf} and loop around to chose to choose another random word. Finally when we have
         * reached a random word which was at the end of a sentence in the original text, we convert
         * our {@code StringBuilder builder} to a String, capitalize the first word of that generated
         * String and return it to the caller.
         *
         * @return String to use as the next sentence of the generated nonsense.
         */
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
                } else {
                    builder.append(suf).append(" ");
                    prefix.pref[0] = prefix.pref[1];
                    prefix.pref[1] = suf;
                }
            }
            return capitalize(builder.toString());
        }

        /**
         * Uses the Markov chain state table to generate a single random sentence and updates its
         * parameter {@code MarkovStats possibles} with statistics about that random sentence. First
         * we do some initialization: create {@code StringBuilder builder} with an initial capacity
         * of 120 characters, initialize our variable {@code String suf} to the empty string, declare
         * {@code int r} to hold our random number, and call {@code init} to make sure {@code Prefix prefix}
         * is already initialized. Then while our {@code String suf} is not an end of sentence word
         * in the original text, we loop first fetching the suffix {@code String[] s} array of the
         * current {@code Prefix prefix} from the state table, performing a sanity check to make sure
         * there is an entry for {@code prefix} (Returning the string "Error!" as our line if there
         * is none). We initialize our variable {@code int suffixes} with the length of {@code s} minus
         * 2 and add {@code suffixes} to our parameter {@code MarkovStats possibles}. Then we set {@code r}
         * to a random position in {@code s} and set {@code suf} to that random word. If {@code suf}
         * is a NONWORD (NONWORD is stored as a suffix entry when the current two word {@code Prefix prefix}
         * occurs as the last two words in the original text) we reset the current {@code Prefix prefix}
         * to the beginning of the state table ([NONWORD, NONWORD]) thus starting another pass through
         * the table to look for a new value of {@code suf}. If {@code suf} is not NONWORD, we append
         * {@code suf} to the end of {@code builder} followed by a space character, set the first word
         * of {@code prefix} ({@code prefix.pref[0]}) to the second word ({@code prefix.pref[1]}) and
         * set the second word ({@code prefix.pref[1]}) to {@code suf} and loop around to chose to
         * choose another random word. Finally when we have reached a random word which was at the
         * end of a sentence in the original text, we convert our {@code StringBuilder builder} to a
         * String, capitalize the first word of that generated String and return it to the caller.
         *
         * @param possibles {@code MarkovStats} instance to update with statistics about the random
         *                  sentence we return.
         * @return String to use as the next sentence of the generated nonsense, and an updated parameter
         * {@code MarkovStats possibles}
         */
        public String line(MarkovStats possibles) {
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
                int suffixes = s.length - 2;
                possibles.add(suffixes);
                r = Math.abs(rand.nextInt()) % suffixes;
                suf = s[r + 2];

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.length);
                    prefix = new Prefix(NONWORD);
                } else {
                    builder.append(suf).append(" ");
                    prefix.pref[0] = prefix.pref[1];
                    prefix.pref[1] = suf;
                }
            }
            return capitalize(builder.toString());
        }

    }

    /**
     * This class holds two words that appear next to each other in the original text and is used
     * as the key to access a {@code String[]} array of words that were found to follow those two
     * words in the original text which is stored as the value in the Markov chain state table
     * {@code Hashtable<Prefix, String[]> stateTable}.
     */
    public class Prefix {
        /**
         * Two adjacent words from original text
         */
        public String[] pref;
        /**
         * Constant used by {@code hashCode()}
         */
        static final int MULTIPLIER = 31;

        /**
         * {@code Prefix} constructor: duplicate existing prefix. We first allocate a {@code String[]}
         * array of size 2 for our field {@code String[] pref}, then we copy the contents of our parameter's
         * fields to this instances field.
         *
         * @param p Prefix to duplicate
         */
        Prefix(Prefix p) {
            pref = new String[2];
            pref[0] = p.pref[0];
            pref[1] = p.pref[1];
        }

        /**
         * {@code Prefix} constructor: Both words of this {@code Prefix} are set to our parameter
         * {@code String str} (currently only used when creating a "start of state table" key from
         * 2 NONWORD's). We first allocate a {@code String[]} array of size 2 for our field
         * {@code String[] pref}, then we copy our parameter {@code str} to both of its entries.
         *
         * @param str String which will be used for both words of this Prefix
         */
        Prefix(String str) {
            pref =  new String[2];
            pref[0] = str;
            pref[1] = str;
        }

        /**
         * Prefix hashCode: generate hash from both prefix words. We use the recommended algorithm
         * of adding the {@code hashCode()} of one field to 31 times the {@code hashCode()} of the
         * other field.
         *
         * @return Hash code value used by the system when the Markov chain state table
         * {@code Hashtable<Prefix, String[]> stateTable} is accessed.
         */
        @Override
        public int hashCode() {
            return MULTIPLIER * pref[0].hashCode() + pref[1].hashCode();
        }

        /**
         * Indicates whether some other {@code Object o} is "equal to" this {@code Prefix} Object.
         * First we make sure the {@code Object o} is an instance of {@code Prefix}, and if not we
         * immediately return false. Then we cast our {@code Object o} parameter to a {@code Prefix}
         * to initialize {@code Prefix p} and return true iff the {@code pref[0]} and {@code pref[1]}
         * fields of {@code p} are both equal to this instances {@code pref[0]} and {@code pref[1]}
         * fields respectively.
         *
         * @param o the reference object with which to compare.
         * @return {@code true} if this object is the same as the o argument; {@code false} otherwise.
         */
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
