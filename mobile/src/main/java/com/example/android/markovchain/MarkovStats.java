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
}
