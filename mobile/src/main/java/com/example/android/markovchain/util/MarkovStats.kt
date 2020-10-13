package com.example.android.markovchain.util

import java.text.NumberFormat
import java.util.ArrayList
import java.util.Locale

/**
 * Class used to store and report stats of Markov generated verses
 */
class MarkovStats {
    /**
     * `List` of number of possible suffixes for each two word `Prefix` involved in
     * generating the `Markov` generated line we are associated with.
     */
    var variations: ArrayList<Int> = ArrayList()

    /**
     * Adds its parameter to our field `ArrayList<Integer> variations`.
     *
     * @param suffixes number of possible suffix words for the present two word prefix
     * @return "this" to allow chaining (ya never know)
     */
    fun add(suffixes: Int): MarkovStats {
        variations.add(suffixes)
        return this
    }

    /**
     * Creates a String representation of the data in this instance. First we initialize our
     * [StringBuilder] variable `val returnString` with a new instance whose initial contents are
     * the string "1", and initialize our [Long] variable `var totalVariations` to 1. Then for each
     * of the `choices` in our `ArrayList<Integer>` field [variations] we append the string "x" to
     * `returnString` followed by the string value of `choices` and multiply `totalVariations` by
     * `choices`. When done with the choices in [variations] we initialize our [String] variable
     * `val formattedResult` with a US locale formatted string version of `totalVariations`, then
     * return the string formed by concatenating the string value of `returnString` followed by the
     * string " = ", followed by `formattedResult` to the caller.
     *
     * @return formatted String for displaying the data we contain
     */
    override fun toString(): String {
        val returnString = StringBuilder("1")
        var totalVariations: Long = 1
        for (choices in variations) {
            returnString.append("x").append(choices)
            totalVariations *= choices.toLong()
        }
        val formattedResult = NumberFormat.getNumberInstance(Locale.US).format(totalVariations)
        return "$returnString = $formattedResult"
    }
}
