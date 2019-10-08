package com.example.android.markovchain

import android.util.Log
import android.view.View

import java.io.*
import java.util.*
import kotlin.math.abs

/**
 * This class is inspired by the Markov chain example source code that was written for the book
 * "The Practice of Programming". It consists of methods to build and access a table which is
 * indexed by two words that occur one after the other in a text of literature, and when those
 * two words are used to access the table an array of all the third words that occur after the
 * first two words is returned.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Markov {
    /**
     * Our instance's Markov `Chain` instance
     */
    lateinit var chain: Chain
    /**
     * Optional callback class instance to invoke when class is ready to be used
     */
    private var doneListener: DoneListener? = null
    /**
     * Optional View for above DoneListener to use for Toast context
     */
    private var view: View? = null

    /**
     * Sets the fields `DoneListener doneListener` and `View view` for this instance of
     * `Markov`.
     *
     * @param doneListener DoneListener for thread calling us, we will call the `onDone(view)`
     * method of `doneListener` on the UI thread when one of our long
     * running processes finishes (either `build` or `loadStateTable`)
     * @param view         `View` that will be passed to `onDone`, it can then be used
     * for `Context` in any `onDoneDo` override of `doneListener`
     */
    fun setDoneListener(doneListener: DoneListener, view: View) {
        this.view = view
        this.doneListener = doneListener
    }

    /**
     * This method initializes this instance of `Markov` by reading an existing pre-built Markov
     * chain from a `BufferedReader`. It is currently called only from `BibleMarkovFragment`
     * in order to read the state table for the King James Bible using a `BufferedReader` created
     * from an `InputStreamReader` which is created from an `InputStream` for the file
     * R.raw.king_james_state_table, but any kind of `BufferedReader` will work. We initialize
     * our field `Chain chain` with a new instance then call its `loadStateTable` method
     * to have it read our parameter `reader` and parse each line to fill its `stateTable`
     * hash table field with the information contained in `reader`.
     *
     * @param reader BufferedReader to read an existing Markov chain from
     */
    fun load(reader: BufferedReader) {
        chain = Chain()
        chain.loadStateTable(reader)
    }

    /**
     * This method initializes this instance of `Markov` by reading text from a `Reader`
     * and then building a Markov chain from that text. It is currently called only with a `StringReader`
     * from `ShakespeareMarkovRecycler` but any kind of `Reader` could be used. We initialize
     * our field `Chain chain` with a new instance then call its `build` method to have
     * it read our parameter `reader` and parse each line to fill its `stateTable` hash
     * table field with the state table calculated from the text of `reader`.
     *
     * @param reader `Reader` of raw text to read in order to construct the Markov `Chain`
     * our instance will use.
     * @throws IOException If an I/O error occurs
     */
    @Throws(IOException::class)
    fun make(reader: Reader) {
        chain = Chain()
        chain.build(reader)
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table. Simply a convenience function to access the `line()` method of our instance's
     * `Chain chain` instance. UNUSED apparently
     *
     * @return The next sentence generated from the Markov chain.
     */
    fun line(): String {
        return chain.line()
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table, with its parameter used to store statistics about how many other random sentences could
     * have been generated at this point instead of the one returned. Simply a convenience function
     * to access the `line(MarkovStats)` method of our instance's `Chain chain` instance.
     *
     * @param possibles a `MarkovStats` instance to contain info about how many possible random
     * lines could have been generated at this point instead of the one returned.
     * @return The next sentence generated from the Markov chain.
     */
    fun line(possibles: MarkovStats): String {
        return chain.line(possibles)
    }

    /**
     * This class is used to contain, build and interact with the Markov chain state table maintained
     * in its `Hashtable<Prefix, String[]> stateTable` field.
     */
    inner class Chain {
        /**
         * `Hashtable` which is accessed by a `Prefix` key constructed from the last two
         * words used to construct a sentence, and whose value is an array of words which have been
         * known to follow those two words in the original source text.
         */
        internal var stateTable = Hashtable<Prefix, Array<String>>()
        /**
         * `Prefix` that we are currently working with, the initial prefix is two NONWORD "words"
         */
        internal var prefix = Prefix(NONWORD)
        /**
         * Used to pick a random word from suffix array to follow up the Prefix
         */
        internal var rand = Random()
        /**
         * Used in `init()` method to decide if the `Prefix prefix` needs to be reset to
         * (NONWORD,NONWORD) (The first line indicator).
         */
        internal var firstLine = true
        /**
         * set to true once our `Chain` is loaded with data.
         */
        var loaded = false

        /**
         * Build State table from `Reader quotes` input stream. First we check if our boolean
         * field `loaded` is true and if it is true, we return without doing anything. Otherwise
         * we create a `StreamTokenizer st` from the parameter `quotes`, specify that all
         * characters shall be treated as ordinary characters by calling the `resetSyntax()`
         * method of `st`, specify that all characters shall be treated as word characters by
         * the tokenizer (a word consists of a word character followed by zero or more word or number
         * characters), and finally specify that all characters between 0 and the the space character
         * are to be treated as whitespace characters. Having initialized our `StreamTokenizer`
         * to our liking we read and parse the entire `Reader quotes` into words (stopping when
         * the word is of type StreamTokenizer.TT_EOF (the end of the stream)), and "add" each word to
         * our state table by calling our `add` method to add the word parsed into the `sval`
         * field of `st` to the end of the current `Prefix prefix` array of suffixes, then
         * updating `prefix` to use the new word as the second word of the "prefix" and the old
         * second word as the first word. When TT_EOF is reached we add a NONWORD as the last word of
         * our state table, set `loaded` to "true" and if our caller has registered an `OnDoneListener`
         * by calling our `setOnDoneListener` method we call the callback `onDone` of that
         * `OnDoneListener doneListener` passing it the view passed to `setOnDoneListener`.
         *
         * @param quotes Reader which is read and parsed into words which are then added to the state table
         * @throws IOException If an I/O error occurs
         */
        @Throws(IOException::class)
        internal fun build(quotes: Reader) {
            if (loaded) return

            val st = StreamTokenizer(quotes)
            st.resetSyntax()                     // remove default rules
            st.wordChars(0, Character.MAX_VALUE.toInt()) // turn on all chars
            st.whitespaceChars(0, ' '.toInt())           // except up to blank

            var wordsRead = 0 // for debugging only
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                add(st.sval)
                if (st.sval == NONWORD) { // for debugging only
                    Log.i(TAG, "NONWORD occurs in input")
                }

                wordsRead++ // for debugging only
            }
            Log.i(TAG, "Words read: $wordsRead") // for debugging only
            add(NONWORD)
            loaded = true
            if (doneListener != null) {
                doneListener!!.onDone(view!!)
            }

        }

        /**
         * Reads in a Markov chain state table which has been prepared offline and loads it into our
         * `Hashtable<Prefix, String[]> stateTable`. First we check to see if it has already
         * been `loaded` and if so return having done nothing. Otherwise we declare `String line`,
         * allocate two strings for the `String[] pref` field of `Prefix prefix` and set
         * them to the starting point of [NONWORD, NONWORD]. Then wrapped in a try block intended
         * to catch and log IOException we read our `BufferedReader reader` parameter line by
         * line until there are no more lines to read, splitting each line read using space as our
         * delimiter into `String words[]`. We use the first two words of `words[]` as
         * the `pref` field of our field `Prefix prefix`, and the entire array of
         * `words[]` as the suffix (wastes two entries in the suffix for the sake of speed) and
         * `put()` the parsed line into our `Hashtable<Prefix, String[]> stateTable`.
         * When done reading in the Markov chain state table we set loaded to true, and if our caller
         * has registered an `OnDoneListener` by calling our `setOnDoneListener` method
         * we call the callback `onDone` of that `OnDoneListener doneListener` passing
         * it the `View` passed to `setOnDoneListener`.
         *
         * @param reader BufferedReader for a pre-parsed Markov chain state table
         */
        internal fun loadStateTable(reader: BufferedReader) {
            if (loaded) return

            var line: String?
            prefix.pref = arrayOf(NONWORD, NONWORD)
            try {
                line = reader.readLine()
                while (line != null) {
                    val words = line.split(" ".toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()
                    prefix.pref[0] = words[0]
                    prefix.pref[1] = words[1]
                    stateTable[Prefix(prefix)] = words
                    line = reader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            loaded = true
            if (doneListener != null) {
                doneListener!!.onDone(view!!)
            }
        }

        /**
         * Chain add: add word to suffix list of `Prefix prefix`, and update `prefix`.
         * First we retrieve the suffix array `String[] suf` for the current `Prefix prefix`
         * from our state table `Hashtable<Prefix, String[]> stateTable`, and if it is null (no
         * prior occurrence of `prefix` encountered) we allocate a `String[3]` array for
         * `suf`, set `suf[0]` to the first word of the current prefix, `suf[1]`
         * to the second word of prefix, and set `suf[2]` to our argument `String word`,
         * and then we create a new `Prefix` from the current `Prefix prefix` and store
         * the `suf` array into our `stateTable` using `prefix` as the key. On the
         * other hand if `suf` is not null we need to add the new word to the end of the
         * `String suf[]` array, and to do this we first create a `String[] newSuf` that
         * is one String longer than the current `suf`, copy the current `suf` to `newSuf`,
         * place our argument `String word` as the last entry in `newSuf` and then use
         * `newSuf` to replace the current entry in our `stateTable` for the `Prefix prefix`.
         * Finally we update the current `prefix` by setting the first word of `prefix`
         * (`prefix.pref[0]`) to the second word (`prefix.pref[1]`), and the second word
         * of `prefix` (`prefix.pref[1]`) to the argument `String word` passed to
         * us by our caller.
         *
         * @param word Word to be added to the suffix list of the current Prefix prefix
         */
        internal fun add(word: String) {

            var suf = stateTable[prefix]
            if (suf == null) {
                suf = arrayOf(prefix.pref[0], prefix.pref[1], word)
                stateTable[Prefix(prefix)] = suf
            } else {
                val newSuf = arrayOf(*suf, word)
                stateTable[prefix] = newSuf
            }
            prefix.pref[0] = prefix.pref[1]
            prefix.pref[1] = word
        }

        /**
         * If `firstLine` is true (we are processing the first line of original text) we initialize
         * `Prefix prefix` with a new instance of `Prefix` pointing to the first entry in the
         * Markov chain state table: ["%", "%"], and set `firstLine` to false.
         */
        internal fun init() {
            if (firstLine) {
                prefix = Prefix(NONWORD)
                firstLine = false
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
        internal fun notEnd(word: String): Boolean {
            return !(word.contains(".") || word.contains("?") || word.contains("!"))
        }

        /**
         * This method is used to capitalize the first word of a sentence. It does this by isolating
         * the char at position 0 (first char of the line), feeding it to the `toUpperCase` method
         * of `Character` and concatenating that capitalized char with the rest of the original
         * line starting at position 1 (second char of line.)
         *
         * @param line String which needs the first letter capitalized (Beginning word of sentence).
         * @return String which consists of the capitalization of the first char concatenated with
         * the rest of the line
         */
        private fun capitalize(line: String): String {
            return Character.toUpperCase(line[0]) + line.substring(1)
        }

        /**
         * Uses the Markov chain state table to generate a single random sentence. First we do some
         * initialization: create `StringBuilder builder` with an initial capacity of 120
         * characters, initialize our variable `String suf` to the empty string, declare
         * `int r` to hold our random number, and call `init` to make sure `Prefix prefix`
         * is already initialized. Then while our `String suf` is not an end of sentence word
         * in the original text, we loop first fetching the suffix `String[] s` array of the
         * current `Prefix prefix` from the state table, performing a sanity check to make sure
         * there is an entry for `prefix` (Returning the string "Error!" as our line if there
         * is none). Then we choose a random word from the `String[]` array `s` and set
         * `String suf` to it. If `suf` is a NONWORD (NONWORD is stored as a suffix entry
         * when the current two word `Prefix prefix` occurs as the last two words in the original
         * text) we reset the current `Prefix prefix` to the beginning of the state table
         * ([NONWORD, NONWORD]) thus starting another pass through the table to look for a new value
         * of `suf`. If `suf` is not NONWORD, we append `suf` to the end of `builder`
         * followed by a space character, set the first word of `prefix` (`prefix.pref[0]`)
         * to the second word (`prefix.pref[1]`) and set the second word (`prefix.pref[1]`)
         * to `suf` and loop around to chose to choose another random word. Finally when we have
         * reached a random word which was at the end of a sentence in the original text, we convert
         * our `StringBuilder builder` to a String, capitalize the first word of that generated
         * String and return it to the caller.
         *
         * @return String to use as the next sentence of the generated nonsense.
         */
        fun line(): String {
            val builder = StringBuilder(120)
            var suf = ""
            var r: Int

            init()
            while (notEnd(suf)) {
                val s = stateTable[prefix]
                if (s == null) {
                    Log.d(TAG, "internal error: no state")
                    return "Error!"
                }
                r = abs(rand.nextInt()) % (s.size - 2)
                suf = s[r + 2]

                if (suf == NONWORD) {
                    Log.i(TAG, "Suffix at $r is NONWORD")
                    Log.i(TAG, "Size of Vector s is: " + s.size)
                    prefix = Prefix(NONWORD)
                } else {
                    builder.append(suf).append(" ")
                    prefix.pref[0] = prefix.pref[1]
                    prefix.pref[1] = suf
                }
            }
            return capitalize(builder.toString())
        }

        /**
         * Uses the Markov chain state table to generate a single random sentence and updates its
         * parameter `MarkovStats possibles` with statistics about that random sentence. First
         * we do some initialization: create `StringBuilder builder` with an initial capacity
         * of 120 characters, initialize our variable `String suf` to the empty string, declare
         * `int r` to hold our random number, and call `init` to make sure `Prefix prefix`
         * is already initialized. Then while our `String suf` is not an end of sentence word
         * in the original text, we loop first fetching the suffix `String[] s` array of the
         * current `Prefix prefix` from the state table, performing a sanity check to make sure
         * there is an entry for `prefix` (Returning the string "Error!" as our line if there
         * is none). We initialize our variable `int suffixes` with the length of `s` minus
         * 2 and add `suffixes` to our parameter `MarkovStats possibles`. Then we set `r`
         * to a random position in `s` and set `suf` to that random word. If `suf`
         * is a NONWORD (NONWORD is stored as a suffix entry when the current two word `Prefix prefix`
         * occurs as the last two words in the original text) we reset the current `Prefix prefix`
         * to the beginning of the state table ([NONWORD, NONWORD]) thus starting another pass through
         * the table to look for a new value of `suf`. If `suf` is not NONWORD, we append
         * `suf` to the end of `builder` followed by a space character, set the first word
         * of `prefix` (`prefix.pref[0]`) to the second word (`prefix.pref[1]`) and
         * set the second word (`prefix.pref[1]`) to `suf` and loop around to chose to
         * choose another random word. Finally when we have reached a random word which was at the
         * end of a sentence in the original text, we convert our `StringBuilder builder` to a
         * String, capitalize the first word of that generated String and return it to the caller.
         *
         * @param possibles `MarkovStats` instance to update with statistics about the random
         * sentence we return.
         * @return String to use as the next sentence of the generated nonsense, and an updated parameter
         * `MarkovStats possibles`
         */
        fun line(possibles: MarkovStats): String {
            val builder = StringBuilder(120)
            var suf = ""
            var r: Int

            init()
            while (notEnd(suf)) {
                val s = stateTable[prefix]
                if (s == null) {
                    Log.d(TAG, "internal error: no state")
                    return "Error!"
                }
                val suffixes = s.size - 2
                possibles.add(suffixes)
                r = abs(rand.nextInt()) % suffixes
                suf = s[r + 2]

                if (suf == NONWORD) {
                    Log.i(TAG, "Suffix at $r is NONWORD")
                    Log.i(TAG, "Size of Vector s is: " + s.size)
                    prefix = Prefix(NONWORD)
                } else {
                    builder.append(suf).append(" ")
                    prefix.pref[0] = prefix.pref[1]
                    prefix.pref[1] = suf
                }
            }
            return capitalize(builder.toString())
        }
    }

    /**
     * This class holds two words that appear next to each other in the original text and is used
     * as the key to access a `String[]` array of words that were found to follow those two
     * words in the original text which is stored as the value in the Markov chain state table
     * `Hashtable<Prefix, String[]> stateTable`.
     */
    inner class Prefix {
        /**
         * Two adjacent words from original text
         */
        var pref: Array<String>

        /**
         * `Prefix` constructor: duplicate existing prefix. We first allocate a `String[]`
         * array of size 2 for our field `String[] pref`, then we copy the contents of our parameter's
         * fields to this instances field.
         *
         * @param p Prefix to duplicate
         */
        internal constructor(p: Prefix) {
            pref = arrayOf(p.pref[0], p.pref[1])
        }

        /**
         * `Prefix` constructor: Both words of this `Prefix` are set to our parameter
         * `String str` (currently only used when creating a "start of state table" key from
         * 2 NONWORD's). We first allocate a `String[]` array of size 2 for our field
         * `String[] pref`, then we copy our parameter `str` to both of its entries.
         *
         * @param str String which will be used for both words of this Prefix
         */
        internal constructor(str: String) {
            pref = arrayOf(str, str)
        }

        /**
         * Prefix hashCode: generate hash from both prefix words. We use the recommended algorithm
         * of adding the `hashCode()` of one field to 31 times the `hashCode()` of the
         * other field.
         *
         * @return Hash code value used by the system when the Markov chain state table
         * `Hashtable<Prefix, String[]> stateTable` is accessed.
         */
        override fun hashCode(): Int {
            return MULTIPLIER * pref[0].hashCode() + pref[1].hashCode()
        }

        /**
         * Indicates whether some other `Object o` is "equal to" this `Prefix` Object.
         * First we make sure the `Object o` is an instance of `Prefix`, and if not we
         * immediately return false. Then we cast our `Object o` parameter to a `Prefix`
         * to initialize `Prefix p` and return true iff the `pref[0]` and `pref[1]`
         * fields of `p` are both equal to this instances `pref[0]` and `pref[1]`
         * fields respectively.
         *
         * @param other the reference object with which to compare.
         * @return `true` if this object is the same as the o argument; `false` otherwise.
         */
        override fun equals(other: Any?): Boolean {
            if (other !is Prefix) {
                return false
            }
            val p = other as Prefix?
            return p!!.pref[0] == this.pref[0] && p.pref[1] == this.pref[1]
        }
    }

    companion object {
        /**
         * Used for log.i calls
         */
        internal const val TAG = "Markov"
        /**
         * "word" that can't appear
         */
        internal const val NONWORD = "%"
        /**
         * Constant used by `hashCode()`
         */
        internal const val MULTIPLIER = 31
    }

}
