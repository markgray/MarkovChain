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
     * Our instance's Markov [Chain] instance
     */
    lateinit var mChain: Chain
    /**
     * Optional callback class instance to invoke when class is ready to be used
     */
    private var mDoneListener: DoneListener? = null
    /**
     * Optional View for above [DoneListener] to use for Toast context
     */
    private var mDoneListenerView: View? = null

    /**
     * Sets the [DoneListener] field [mDoneListener] and the [View] field [mDoneListenerView] for
     * this instance of [Markov].
     *
     * @param doneListener [DoneListener] for thread calling us, we will call the `onDone` method of
     * [doneListener] on the UI thread when one of our long running processes finishes (either `build`
     * or `loadStateTable`)
     * @param view [View] that will be passed to `onDone`, it can then be used for `Context` in any
     * `onDoneDo` override of `mDoneListener`
     */
    fun setDoneListener(doneListener: DoneListener, view: View) {
        this.mDoneListenerView = view
        this.mDoneListener = doneListener
    }

    /**
     * This method initializes this instance of [Markov] by reading an existing pre-built Markov
     * [Chain] from a [BufferedReader]. It is currently called only from `BibleMarkovFragment`
     * in order to read the state table for the King James Bible using a `BufferedReader` created
     * from an `InputStreamReader` which is created from an `InputStream` for the file
     * R.raw.king_james_state_table, but any kind of `BufferedReader` will work. We initialize
     * our [Chain] field [mChain] with a new instance then call its `loadStateTable` method
     * to have it read our parameter [reader] and parse each line to fill its `stateTable`
     * hash table field with the information contained in [reader].
     *
     * @param reader BufferedReader to read an existing Markov chain from
     */
    fun load(reader: BufferedReader) {
        mChain = Chain()
        mChain.loadStateTable(reader)
    }

    /**
     * This method initializes this instance of [Markov] by reading text from a [Reader] and then
     * building a Markov [Chain] from that text. It is currently called only with a [StringReader]
     * from `ShakespeareMarkovRecycler` but any kind of [Reader] could be used. We initialize
     * our [Chain] field [mChain] with a new instance then call its `build` method to have
     * it read our parameter [reader] and parse each line to fill its `stateTable` hash
     * table field with the state table calculated from the text of [reader].
     *
     * @param reader [Reader] of raw text to read in order to construct the Markov [Chain] our
     * instance will use.
     * @throws IOException If an I/O error occurs
     */
    @Throws(IOException::class)
    fun make(reader: Reader) {
        mChain = Chain()
        mChain.build(reader)
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table. Simply a convenience function to access the `line()` method of our instance's
     * [Chain] field [mChain]. UNUSED apparently
     *
     * @return The next sentence generated by the Markov [Chain] field [mChain].
     */
    fun line(): String {
        return mChain.line()
    }

    /**
     * Called to retrieve the next sentence that is randomly generated from this instance's state
     * table, with its parameter used to store statistics about how many other random sentences could
     * have been generated at this point instead of the one returned. Simply a convenience function
     * to access the `line(MarkovStats)` method of our instance's [Chain] field [mChain].
     *
     * @param possibles a `MarkovStats` instance to contain info about how many possible random
     * lines could have been generated at this point instead of the one returned.
     * @return The next sentence generated from the Markov mChain.
     */
    fun line(possibles: MarkovStats): String {
        return mChain.line(possibles)
    }

    /**
     * This class is used to contain, build and interact with the state table of the Markov [Chain]
     * contained in its `Hashtable<Prefix, String[]>` [stateTable] field.
     */
    inner class Chain {
        /**
         * [Hashtable] which is accessed by a [Prefix] key constructed from two adjacent words that
         * were used in a sentence, and whose value is an array of words which have been known to
         * follow those two words in the original source text.
         */
        internal var stateTable = Hashtable<Prefix, Array<String>>()
        /**
         * [Prefix] that we are currently working with, the initial prefix is two [NONWORD] "words"
         */
        internal var prefix = Prefix(NONWORD)
        /**
         * Used to pick a random word from suffix array to follow up the Prefix
         */
        internal var rand = Random()
        /**
         * Used in `init()` method to decide if the [Prefix] field [prefix] needs to be reset to
         * (NONWORD,NONWORD) (The first line indicator).
         */
        internal var firstLine = true
        /**
         * set to true once our [Chain] state table is loaded with data.
         */
        var loaded = false

        /**
         * Build State table from our [Reader] parameter [quotes] input stream. First we check if our
         * [Boolean] field [loaded] is *true* and if it is *true*, we return without doing anything.
         * Otherwise we create a [StreamTokenizer] variable `val st` from the parameter [quotes],
         * specify that all characters shall be treated as ordinary characters by calling the
         * `resetSyntax()` method of `st`, specify that all characters shall be treated as word
         * characters by the tokenizer (a word consists of a word character followed by zero or more
         * word or number characters), and finally specify that all characters between 0 and the
         * space character are to be treated as whitespace characters. Having initialized our
         * [StreamTokenizer] `st` to our liking we read and parse the entire contents of [quotes]
         * into words (stopping when the word is of type StreamTokenizer.TT_EOF (the end of the
         * stream)), and "add" each word to our state table by calling our `add` method which add
         * the word parsed into the `sval` field of `st` to the end of the array of suffixes of the
         * current `Prefix prefix`, then update `prefix` to use the new word as the second word of
         * the "prefix" and the old second word as the first word. When TT_EOF is reached we add a
         * NONWORD as the last word of our state table, set `loaded` to *true* and if our caller has
         * registered an `OnDoneListener` by calling our `setOnDoneListener` method we call the
         * callback `onDone` of our `OnDoneListener` field [mDoneListener] passing it the
         * [mDoneListenerView] that was also passed to `setOnDoneListener`.
         *
         * @param quotes [Reader] which is read and parsed into words which are then added to the
         * state table.
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
            Log.i(TAG, "Size of HashTable: ${ stateTable.size } ")
            add(NONWORD)
            loaded = true
            if (mDoneListener != null) {
                mDoneListener!!.onDone(mDoneListenerView!!)
            }

        }

        /**
         * Reads in a Markov chain state table which has been prepared offline and loads it into our
         * `Hashtable<Prefix, String[]>` field [stateTable]. First we check to see if it has already
         * been `loaded` and if so return having done nothing. Otherwise we declare our [String]
         * variable `var line`, allocate two strings for the `String[] pref` field of our [Prefix]
         * field [prefix] and set them to the starting point of [NONWORD, NONWORD]. Then wrapped in
         * a try block intended to catch and log IOException we read our [BufferedReader] parameter
         * [reader] line by line until there are no more lines to read, splitting each line read
         * using space as our delimiter into the array of strings `val words`. We use the first two
         * words of `words[]` as the `pref` field of our field `Prefix prefix`, and the entire array
         * of `words` as the suffix (wastes two entries in the suffix for the sake of speed) and
         * `put()` the parsed line into our `Hashtable<Prefix, String[]>` field [stateTable]. When
         * done reading in the Markov [Chain] state table we set loaded to true, and if our caller
         * has registered an `OnDoneListener` by calling our `setOnDoneListener` method we call the
         * callback `onDone` of that `OnDoneListener mDoneListener` passing it the `View` passed to
         * `setOnDoneListener` that is in our [mDoneListenerView] field.
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
            Log.i(TAG, "Size of HashTable: ${ stateTable.size } ")
            if (mDoneListener != null) {
                mDoneListener!!.onDone(mDoneListenerView!!)
            }
        }

        /**
         * Chain add: add word to suffix list of our [Prefix] field [prefix], and update [prefix].
         * First we retrieve the suffix array of strings of the current [Prefix] field [prefix]
         * from our state table `Hashtable<Prefix, String[]>` field [stateTable]  to initialize our
         * variable `var suf`, and if it is *null* (no prior occurrence of [prefix] encountered) we
         * set `suf` to an array of 3 strings consisting of the first word of the current prefix,
         * the second word of prefix, and our [String] argument [word] as the third string. Then we
         * construct a new [Prefix] from the current [Prefix] field [prefix] and store the `suf`
         * array into our [stateTable] using this new [Prefix] as the key. On the other hand if
         * `suf` is not null we need to add the new word to the end of the `suf` array, and to do
         * this we first create a string array `var newSuf` that consists of the "spread" of the
         * current `suf` and our [String] parameter [word] (the "*" spread operator converts `suf`
         * into the *varargs* argument which the [arrayOf] method expects). We then use `newSuf` to
         * replace the current entry in our `stateTable` for the [Prefix] field [prefix] key.
         * In either case we update the current [prefix] by setting the first word of [prefix]
         * (`prefix.pref[0]`) to the second word (`prefix.pref[1]`), and the second word of [prefix]
         * (`prefix.pref[1]`) to the [String] argument [word] passed to us by our caller.
         *
         * @param word Word to be added to the suffix list of the current [Prefix] field [prefix].
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
         * If our [firstLine] field is true (we are processing the first line of original text) we
         * initialize our [Prefix] field [prefix] with a new instance of [Prefix] pointing to the
         * first entry in the Markov chain state table: `["%", "%"]`, and set [firstLine] to false.
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
         * of [Character] and concatenating that capitalized char with the rest of the original
         * line starting at position 1 (second char of line.)
         *
         * @param line [String] which needs the first letter capitalized (Beginning word of sentence).
         * @return [String] which consists of the capitalization of the first char concatenated with
         * the rest of the line
         */
        private fun capitalize(line: String): String {
            return Character.toUpperCase(line[0]) + line.substring(1)
        }

        /**
         * Uses the Markov chain state table to generate a single random sentence. First we do some
         * initialization: construct a [StringBuilder] variable `val builder` with an initial capacity
         * of 120 characters, initialize our [String] variable `var suf` to the empty string, declare
         * an [Int] variable `var r` to hold our random number, and call our [init] method to make
         * sure our [Prefix] field [prefix] is already initialized. Then while our [String] `suf` is
         * not an end of sentence word in the original text, we loop, first fetching the suffix
         * string array `val s` of the current [Prefix] field [prefix] from the state table, performing
         * a sanity check to make sure there was an entry for [prefix] (Returning the string "Error!"
         * as our line if there is none). Then we choose a random word from the string array `s` and
         * set `suf` to it. If `suf` is a NONWORD (NONWORD is stored as a suffix entry when the current
         * two word [Prefix] field [prefix] occurs as the last two words in the original text) we
         * reset the current [Prefix] field [prefix] to the beginning of the state table (which is
         * the entry for [NONWORD, NONWORD]) thus starting another pass through the table to look for
         * a new value of `suf`. If `suf` is not NONWORD, we append `suf` to the end of `builder`
         * followed by a space character, set the first word of [prefix] (`prefix.pref[0]`) to the
         * second word (`prefix.pref[1]`) and set the second word (`prefix.pref[1]`) to `suf` then
         * loop around to chose to choose another random word. Finally when we have reached a random
         * word which was at the end of a sentence in the original text, we convert our [StringBuilder]
         * variable `builder` to a [String], capitalize the first word of that generated [String] and
         * return it to the caller.
         *
         * @return [String] to use as the next sentence of the generated nonsense.
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
         * [MarkovStats] parameter [possibles] with statistics about that random sentence. First
         * we do some initialization: create the variable [StringBuilder] `val builder` with an
         * initial capacity of 120 characters, initialize our [String] variable `var suf` to the
         * empty string, declare an [Int] `var r` to hold our random number, and call [init] to make
         * sure our [Prefix] field [prefix] is already initialized. Then while our [String] `suf` is
         * not an end of sentence word in the original text, we loop, first fetching the suffix
         * [String] array `val s` of the current [Prefix] field [prefix] from the state table,
         * performing a sanity check to make sure there is an entry for [prefix] (Returning the
         * string "Error!" as our line if there is none). We initialize our [Int] variable
         * `val suffixes` with the length of `s` minus 2 and add `suffixes` to our [MarkovStats]
         * parameter [possibles]. Then we set `r` to a random position in `s` and set `suf` to that
         * random word. If `suf` is a NONWORD (NONWORD is stored as a suffix entry when the current
         * two word [Prefix] field [prefix] occurs as the last two words in the original text) we
         * reset the current [Prefix] field [prefix] to the beginning of the state table (the entry
         * for `[NONWORD, NONWORD]`) thus starting another pass through the table to look for a new
         * value of `suf`. If `suf` is not NONWORD, we append `suf` to the end of `builder` followed
         * by a space character, set the first word of `prefix` (`prefix.pref[0]`) to the second word
         * (`prefix.pref[1]`) and set the second word (`prefix.pref[1]`) to `suf` and loop around to
         * chose to choose another random word. Finally when we have reached a random word which was
         * at the end of a sentence in the original text, we convert our [StringBuilder] `builder`
         * to a String, capitalize the first word of that generated String and return it to the caller.
         *
         * @param possibles [MarkovStats] instance to update with statistics about the random
         * sentence we return.
         * @return [String] to use as the next sentence of the generated nonsense, and an updated
         * [MarkovStats] parameter [possibles]
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
         * [Prefix] constructor: duplicate existing prefix. We set our field [pref] to an array of
         * strings holding the two words of our [Prefix] parameter [p].
         *
         * @param p Prefix to duplicate
         */
        internal constructor(p: Prefix) {
            pref = arrayOf(p.pref[0], p.pref[1])
        }

        /**
         * [Prefix] constructor: Both words of this [Prefix] are set to our [String] parameter
         * [str] (currently only used when creating a "start of state table" key from 2 NONWORD's).
         * We set our field [pref] to an array of strings holding two copies of our [String]
         * parameter [str].
         *
         * @param str [String] which will be used for both words of this [Prefix]
         */
        internal constructor(str: String) {
            pref = arrayOf(str, str)
        }

        /**
         * [Prefix] hashCode: generate hash from both prefix words. We use the recommended algorithm
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
         * Indicates whether some [other] object of type [Any] is "equal to" this [Prefix] object.
         * First we make sure the [other] is an instance of [Prefix], and if not we immediately
         * return false. Then we cast our [other] parameter to a [Prefix] to initialize our [Prefix]
         * variable `val p` and return true iff the `pref[0]` and `pref[1]` fields of `p` are both
         * equal to this instances `pref[0]` and `pref[1]` fields respectively.
         *
         * @param other the reference object with which to compare.
         * @return *true* if this object is the same as the [other] argument; *false* otherwise.
         */
        override fun equals(other: Any?): Boolean {
            if (other !is Prefix) {
                return false
            }
            val p = other as Prefix?
            return p!!.pref[0] == this.pref[0] && p.pref[1] == this.pref[1]
        }
    }

    /**
     * Our static constants.
     */
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