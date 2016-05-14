package com.example.android.common;

import android.util.Log;
import android.view.View;

import java.io.*;
import java.util.*;

public class Markov {
    static final String TAG = "Markov";
    static final int MAXGEN = 10000; // maximum words generated
    public List<String> mOutput = new ArrayList<>();
    public Chain chain;
    private DoneListener doneListener;
    private View view;

    public void setDoneListener(DoneListener doneListener, View view) {
        this.view = view;
        this.doneListener = doneListener;
    }



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
        Hashtable<Prefix, String[]> statetab = new Hashtable<>(); // key = Prefix, value = suffix Array
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
                    statetab.put(new Prefix(prefix), words);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            loaded = true;
            if (doneListener != null) {
                doneListener.onDone(view);
            }
        }

        // Chain add: add word to suffix list, update prefix
        void add(String word) {

            String[] suf = statetab.get(prefix);
            if (suf == null) {
                suf = new String[3];
                suf[0] = prefix.pref[0];
                suf[1] = prefix.pref[1];
                suf[2] = word;
                statetab.put(new Prefix(prefix), suf);
            } else {
                String[] newSuf = new String[suf.length + 1];
                System.arraycopy(suf, 0, newSuf, 0, suf.length);
                newSuf[suf.length] = word;
                statetab.put(prefix, newSuf);
            }
            prefix.pref[0] = prefix.pref[1];
            prefix.pref[1] = word;
        }

        // Chain generate: generate output words
        void generate(int nwords) {

            prefix = new Prefix(NONWORD);
            String suf;
            int r;

            for (int i = 0; i < nwords; i++) {
                String[] s = statetab.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                    return;
                }
                r = Math.abs(rand.nextInt()) % (s.length - 2);
                suf = s[r+2];

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.length);
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

        private String capitalize(final String line) {
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        }

        public String line() {
            StringBuilder builder = new StringBuilder(120);
            String suf = "";
            int r;

            init();
            while (notEnd(suf)) {
                String[] s = statetab.get(prefix);
                if (s == null) {
                    Log.d(TAG, "internal error: no state");
                    return "Error!";
                }
                r = Math.abs(rand.nextInt()) % (s.length - 2);
                suf = s[r + 2];

                if (suf.equals(NONWORD)) {
                    Log.i(TAG, "Suffix at " + r + " is NONWORD");
                    Log.i(TAG, "Size of Vector s is: " + s.length);
                    prefix = new Prefix(NONWORD);
                }
                builder.append(suf).append(" ");
                prefix.pref[0] = prefix.pref[1];
                prefix.pref[1] = suf;
            }
            return capitalize(builder.toString());
        }

    }

    public class Prefix {
        public String[] pref;    // NPREF adjacent words from input
        static final int MULTIPLIER = 31;    // for hashCode()

        // Prefix constructor: duplicate existing prefix
        Prefix(Prefix p) {
            pref = new String[2];
            pref[0] = p.pref[0];
            pref[1] = p.pref[1];
        }

        // Prefix constructor: n copies of str
        Prefix(String str) {
            pref =  new String[2];
            pref[0] = str;
            pref[1] = str;
        }

        // Prefix hashCode: generate hash from all prefix words
        @Override
        public int hashCode() {
            int h = 0;

            for (int i = 0; i < 2; i++)
                h = MULTIPLIER * h + pref[i].hashCode();
            return h;
        }

        // Prefix equals: compare two prefixes for equal words
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Prefix)) {
                return false;
            }
            Prefix p = (Prefix) o;
            return (p.pref[0].equals(this.pref[0]) && p.pref[1].equals(this.pref[1]));
        }

    }

}
