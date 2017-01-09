package com.example.android.markovchain;

import java.util.ArrayList;

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
            returnString.append(choices).append("x");
            totalVariations *= choices.intValue();
        }
        return returnString.toString() + totalVariations;
    }
}
