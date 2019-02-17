package com.example.android.common;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Class used to store and report stats of Markov generated verses
 */
@SuppressWarnings("WeakerAccess")
public class MarkovStats {
    /**
     * {@code List} of number of possible suffixes for each two word {@code Prefix} involved in
     * generating the {@code Markov} generated line we are associated with.
     */
    public ArrayList<Integer> variations;

    /**
     * Basic constructor, we just allocate an {@code ArrayList} for our field {@code ArrayList<Integer> variations}
     */
    public MarkovStats() {
        variations = new ArrayList<>();
    }

    /**
     * Adds its parameter to our field {@code ArrayList<Integer> variations}.
     *
     * @param var number of possible suffix words for the present two word prefix
     * @return "this" to allow chaining (ya never know)
     */
    public MarkovStats add(Integer var) {
        variations.add(var);
        return this;
    }

    /**
     * Creates a String representation of the data in this instance. First we initialize our variable
     * {@code StringBuilder returnString} with a new instance whose initial contents are the string "1",
     * and initialize our variable {@code long totalVariations} to 1. Then for each of the {@code Integer choices}
     * in our field {@code ArrayList<Integer> variations} we append the string "x" to {@code returnString}
     * followed by the string value of {@code choices} and multiply {@code totalVariations} by {@code choices}.
     * When done with the choices in {@code variations} we initialize our variable {@code String formattedResult}
     * with a US locale formatted string version of {@code totalVariations}, then return the string formed by
     * concatenating the string value of {@code returnString} followed by the string " = ", followed by
     * {@code formattedResult} to the caller.
     *
     * @return formatted String for displaying the data we contain
     */
    @NonNull
    public String toString() {
        StringBuilder returnString = new StringBuilder("1");
        long totalVariations = 1;
        for (Integer choices :variations) {
            returnString.append("x").append(choices);
            totalVariations *= choices;
        }
        String formattedResult = NumberFormat.getNumberInstance(Locale.US).format(totalVariations);
        return returnString.toString() + " = " + formattedResult;
    }
}
