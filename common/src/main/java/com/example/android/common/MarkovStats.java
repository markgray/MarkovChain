package com.example.android.common;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Class used to store and report stats of Markov generated verses
 */
@SuppressWarnings("WeakerAccess")
public class MarkovStats {
    /**
     * {@code List} of number of possible suffixes for each two word {@code Prefix} involved in
     * generating the Markov verse we are connected to
     */
    public ArrayList<Integer> variations;

    public MarkovStats() {
        variations = new ArrayList<>();
    }

    public MarkovStats add(Integer var) {
        variations.add(var);
        return this;
    }

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
