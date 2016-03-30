package com.example.android.common;

import android.util.Log;

import java.io.*;
import java.util.*;

public class Markov {
    static final String TAG = "Markov";
    static final int MAXGEN = 10000; // maximum words generated
    public List<String> mOutput = new ArrayList<>();

    public void startUp (String[] args) throws IOException {
        Chain chain = new Chain();
        int nwords = MAXGEN;

        StringBuilder stringBuilder = new StringBuilder();
        for (String quotes: args) {
            stringBuilder.append(quotes);
        }
        chain.build(new StringReader(stringBuilder.toString()));
        chain.generate(nwords);
    }

    public void startUp (BufferedReader reader) throws IOException {
        Chain chain = new Chain();
        int nwords = MAXGEN;

        chain.loadStateTable(reader);
        chain.generate(nwords);
    }

    public class Chain {
        static final int NPREF = 2;    // size of prefix
        static final String NONWORD = "\n";
        // "word" that can't appear
        Hashtable<Prefix, Vector<String>> statetab = new Hashtable<>();
        // key = Prefix, value = suffix Vector
        Prefix prefix = new Prefix(NPREF, NONWORD);
        // initial prefix
        Random rand = new Random();

        // Chain build: build State table from input stream
        void build(Reader quotes) throws IOException {
            StreamTokenizer st = new StreamTokenizer(quotes);
            int wordsRead = 0;

            st.resetSyntax();                     // remove default rules
            st.wordChars(0, Character.MAX_VALUE); // turn on all chars
            st.whitespaceChars(0, ' ');           // except up to blank
            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                add(st.sval);
                if (st.sval.equals(NONWORD)){
                    Log.i(TAG, "NONWORD occurs in input");
                }

                wordsRead++;
            }
            Log.i(TAG, "Words read: " + wordsRead);
            add(NONWORD);
        }

        void loadStateTable(BufferedReader reader) {
            String line;
            try {
                while ((line = reader.readLine()) != null) {

                    StreamTokenizer st = new StreamTokenizer(new StringReader(line));
                    int wordsRead = 0;

                    st.resetSyntax();                     // remove default rules
                    st.wordChars(0, Character.MAX_VALUE); // turn on all chars
                    st.whitespaceChars(0, ' ');           // except up to blank

                    

                    while (st.nextToken() != StreamTokenizer.TT_EOF) {
                        add(st.sval);
                        if (st.sval.equals(NONWORD)) {
                            Log.i(TAG, "NONWORD occurs in input");
                        }
// TODO: need to collect two tokens for prefix, and rest of tokens on line are suffixes to be added one by one
// empty string is a NONWORD Token


                        wordsRead++;
                    }
                    Log.i(TAG, "Words read: " + wordsRead);
                    add(NONWORD);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Chain add: add word to suffix list, update prefix
        void add(String word) {
            Vector<String> suf = statetab.get(prefix);
            if (suf == null) {
                suf = new Vector<>();
                statetab.put(new Prefix(prefix), suf);
            }
            suf.addElement(word);
            prefix.pref.removeElementAt(0);
            prefix.pref.addElement(word);
        }

        // Chain generate: generate output words
        void generate(int nwords) {

            prefix = new Prefix(NPREF, NONWORD);
            String suf;
            int r;

            for (int i = 0; i < nwords; i++) {
                Vector<String> s = statetab.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                    return;
                }
                r = Math.abs(rand.nextInt()) % s.size();
                suf = s.elementAt(r);

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.size());
                    Log.i(TAG, "Words generated:" + i);
                    prefix = new Prefix(NPREF, NONWORD);
                }
                mOutput.add(suf);
                prefix.pref.removeElementAt(0);
                prefix.pref.addElement(suf);
            }
        }
    }

    public class Prefix {
        public Vector<String> pref;    // NPREF adjacent words from input
        static final int MULTIPLIER = 31;    // for hashCode()

        // Prefix constructor: duplicate existing prefix
        Prefix(Prefix p) {
            //noinspection unchecked
            pref = (Vector<String>) p.pref.clone();
        }

        // Prefix constructor: n copies of str
        Prefix(int n, String str) {
            pref = new Vector<>();
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
            if (!(o instanceof Prefix)) {
                return false;
            }
            Prefix p = (Prefix) o;

            for (int i = 0; i < pref.size(); i++)
                if (!pref.elementAt(i).equals(p.pref.elementAt(i)))
                    return false;
            return true;
        }

    }
}
