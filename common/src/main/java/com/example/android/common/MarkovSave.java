package com.example.android.common;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class MarkovSave {
    static final String TAG = "Markov";
    static final int MAXGEN = 10000; // maximum words generated
    public List<String> mOutput = new ArrayList<>();
    public Chain chain;

    public void startUp (String[] args) throws IOException {
        chain = new Chain();
        int nwords = MAXGEN;

        StringBuilder stringBuilder = new StringBuilder();
        for (String quotes: args) {
            stringBuilder.append(quotes);
        }
        chain.build(new StringReader(stringBuilder.toString()));
        chain.generate(nwords);
    }

    public void startUp (BufferedReader reader) throws IOException {
        chain = new Chain();
        int nwords = MAXGEN;

        chain.loadStateTable(reader);
        chain.generate(nwords);
    }

    public void load (BufferedReader reader) throws IOException {
        chain = new Chain();
        chain.loadStateTable(reader);
    }

    public void make (Reader reader) throws IOException {
        chain = new Chain();
        chain.build(reader);
    }

    public String line() {
        return chain.line();
    }

    public class Chain {
        static final String NONWORD = "%"; // "word" that can't appear
        Hashtable<Prefix, Vector<String>> statetab = new Hashtable<>(); // key = Prefix, value = suffix Vector
        Prefix prefix = new Prefix(NONWORD); // initial prefix
        Random rand = new Random();
        boolean firstLine = true;
        public boolean loaded = false; // set once chain is loaded

        // Chain build: build State table from input stream
        void build(Reader quotes) throws IOException {
            if (loaded) return;
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
            loaded = true;
        }

        void loadStateTable(BufferedReader reader) {
            if (loaded) return;
            String line;
            prefix.pref = new String[2];
            prefix.pref[0] = NONWORD;
            prefix.pref[1] = NONWORD;
            try {
                while ((line = reader.readLine()) != null) {
                    String words[] = line.split(" ");
                    prefix.pref[0] = words[0];
                    prefix.pref[1] = words[1];
                    Vector<String> suf = statetab.get(prefix);
                    if (suf == null) {
                        suf = new Vector<>();
                        statetab.put(new Prefix(prefix), suf);
                    }
                    for (int i = 2; i < words.length; i++) {
                        suf.addElement(words[i]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            loaded = true;
        }

        // Chain add: add word to suffix list, update prefix
        void add(String word) {

            Vector<String> suf = statetab.get(prefix);
            if (suf == null) {
                suf = new Vector<>();
                statetab.put(new Prefix(prefix), suf);
            }
            suf.addElement(word);
            prefix.pref[0] = prefix.pref[1];
            prefix.pref[1] = word;
        }

        // Chain generate: generate output words
        void generate(int nwords) {

            prefix = new Prefix(NONWORD);
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
                    prefix = new Prefix(NONWORD);
                }
                mOutput.add(suf);
                prefix.pref[0] = prefix.pref[1];
                prefix.pref[1] = suf;
            }
        }

        void init() {
            if (firstLine) {
                prefix = new Prefix(NONWORD);
                firstLine = false;
            }
        }

        boolean notEnd(String word) {
            return !(word.contains(".") || word.contains("?") || word.contains("!"));
        }

        public String line() {
            StringBuilder builder = new StringBuilder(120);
            String suf = "";
            int r;

            init();
            while (notEnd(suf)) {
                Vector<String> s = statetab.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                    return "Error!";
                }
                r = Math.abs(rand.nextInt()) % s.size();
                suf = s.elementAt(r);

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.size());
                    prefix = new Prefix(NONWORD);
                }
                builder.append(suf).append(" ");
                prefix.pref[0] = prefix.pref[1];
                prefix.pref[1] = suf;
            }
            return builder.toString();
        }

    }

    public class Prefix {
        public String[] pref;    // NPREF adjacent words from input
        static final int MULTIPLIER = 31;    // for hashCode()

        // Prefix constructor: duplicate existing prefix
        Prefix(Prefix p) {
            //noinspection unchecked
            pref = p.pref.clone();
        }

        // Prefix constructor: n copies of str
        Prefix(String str) {
            pref =  new String[2];
            pref[0] = str;
            pref[1] = str;
        }

        // Prefix hashCode: generate hash from all prefix words
        public int hashCode() {
            int h = 0;

            for (int i = 0; i < 2; i++)
                h = MULTIPLIER * h + pref[i].hashCode();
            return h;
        }

        // Prefix equals: compare two prefixes for equal words
        public boolean equals(Object o) {
            if (!(o instanceof Prefix)) {
                return false;
            }
            Prefix p = (Prefix) o;
            return (p.pref[0].equals(pref[0]) && p.pref[1].equals(pref[1]));
        }

    }


}
