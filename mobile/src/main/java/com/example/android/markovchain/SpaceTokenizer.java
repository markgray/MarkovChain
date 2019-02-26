package com.example.android.markovchain;

import android.widget.MultiAutoCompleteTextView;

/**
 * This custom {@code MultiAutoCompleteTextView.Tokenizer} splits a {@code CharSequence} string into
 * words using only spaces as the delimiter. It is used to split up the {@code String} which is going
 * to be used by {@code BibleSearch} for its {@code MultiAutoCompleteTextView}
 */
@SuppressWarnings("WeakerAccess")
public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    /**
     * Returns the start of the token that ends at offset cursor within text. Using a pointer
     * {@code int i} and starting at the position given by our parameter {@code cursor} we first
     * back up until we find the first char which has a blank char in front of it in the parameter
     * passed us in {@code CharSequence text}, then we go forward, stopping when we either find a
     * non-blank char or reach the cursor pointer again. We then return our pointer {@code i} as the
     * start of the token that ends at offset cursor.
     *
     * @param text   Text we are working on
     * @param cursor Offset within text to start at
     * @return Offset to next token to be considered
     */
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i - 1) != ' ') {
            i--;
        }
        while (i < cursor && text.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    /**
     * Returns the end of the token (minus trailing punctuation) that begins at offset {@code cursor}
     * within {@code CharSequence text}. Starting at {@code cursor} we increment a pointer {@code int i}
     * until we find a blank char and upon doing so we return {@code i} as the end of the token. If
     * we do not find a blank before reaching the end of {@code text} we return the length of {@code text}
     * as the end of the token.
     * 
     * @param text   Text we are working on
     * @param cursor offset within text to start at
     * @return offset of space character that ends the token
     */
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == ' ') {
                return i;
            } else {
                i++;
            }
        }

        return len;
    }

    /**
     * Returns our parameter {@code CharSequence text}, modified, if necessary, to ensure that it ends
     * with a space character. If the last char of text is already a blank we return {@code CharSequence text},
     * otherwise we append a blank to the end of {@code text} and return that.
     *
     * @param text Text we are working on
     * @return text terminated by at least one space
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        if (text.charAt(text.length() - 1) == ' ') return text;
        return text + " ";
    }
}

