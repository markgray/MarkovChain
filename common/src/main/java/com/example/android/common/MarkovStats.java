package com.example.android.common;

import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MarkovStats {
    public ArrayList<Integer> variations;

    public MarkovStats() {
        variations = new ArrayList<>();
    }

    public MarkovStats add(Integer var) {
        variations.add(var);
        return this;
    }

    public String toString() {
        StringBuilder returnString = new StringBuilder();
        int totalVariations = 1;
        for (Integer choices :variations) {
            returnString.append("x").append(choices);
            totalVariations *= choices;
        }
        return returnString.toString() + " = " + totalVariations;
    }
}
