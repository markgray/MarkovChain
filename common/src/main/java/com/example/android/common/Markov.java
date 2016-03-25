package com.example.android.common;

import android.util.Log;

import java.io.*;
import java.util.*;

public class Markov {
    static final String TAG = "Markov";
    static final int MAXGEN = 1000000; // maximum words generated
    public List<String> mOutput = new ArrayList<>();

    public void startUp (String[] args) throws IOException {
        Chain chain = new Chain();
        int nwords = MAXGEN;

        for (String quotes: args) {
            chain.build(new StringReader(quotes));
        }
        chain.generate(nwords);
    }

    public class Chain {
        static final int NPREF = 2;    // size of prefix
        static final String NONWORD = "\n";
        // "word" that can't appear
        Hashtable statetab = new Hashtable();
        // key = Prefix, value = suffix Vector
        Prefix prefix = new Prefix(NPREF, NONWORD);
        // initial prefix
        Random rand = new Random();

        // Chain build: build State table from input stream
        void build(Reader quotes) throws IOException {
            StreamTokenizer st = new StreamTokenizer(quotes);

            st.resetSyntax();                     // remove default rules
            st.wordChars(0, Character.MAX_VALUE); // turn on all chars
            st.whitespaceChars(0, ' ');           // except up to blank
            while (st.nextToken() != StreamTokenizer.TT_EOF)
                add(st.sval);
            add(NONWORD);
        }

        // Chain add: add word to suffix list, update prefix
        void add(String word) {
            Vector suf = (Vector) statetab.get(prefix);
            if (suf == null) {
                suf = new Vector();
                statetab.put(new Prefix(prefix), suf);
            }
            suf.addElement(word);
            prefix.pref.removeElementAt(0);
            prefix.pref.addElement(word);
        }

        // Chain generate: generate output words
        void generate(int nwords) {
            prefix = new Prefix(NPREF, NONWORD);
            for (int i = 0; i < nwords; i++) {
                Vector s = (Vector) statetab.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                }
                int r = Math.abs(rand.nextInt()) % s.size();
                String suf = (String) s.elementAt(r);
                if (suf.equals(NONWORD))
                    break;
                mOutput.add(suf);
                prefix.pref.removeElementAt(0);
                prefix.pref.addElement(suf);
            }
        }
    }

    public class Prefix {
        public Vector pref;    // NPREF adjacent words from input
        static final int MULTIPLIER = 31;    // for hashCode()

        // Prefix constructor: duplicate existing prefix
        Prefix(Prefix p) {
            pref = (Vector) p.pref.clone();
        }

        // Prefix constructor: n copies of str
        Prefix(int n, String str) {
            pref = new Vector();
            for (int i = 0; i < n; i++)
                pref.addElement(str);
        }

        // Prefix hashCode: generate hash from all prefix words
        public int hashCode() {
            int h = 0;

            for (int i = 0; i < pref.size(); i++)
                h = MULTIPLIER * h + pref.elementAt(i).hashCode();
            return h;
        }

        // Prefix equals: compare two prefixes for equal words
        public boolean equals(Object o) {
            Prefix p = (Prefix) o;

            for (int i = 0; i < pref.size(); i++)
                if (!pref.elementAt(i).equals(p.pref.elementAt(i)))
                    return false;
            return true;
        }

    }
}
