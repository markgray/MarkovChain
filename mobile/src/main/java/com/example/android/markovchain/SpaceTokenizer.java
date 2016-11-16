package com.example.android.markovchain;


import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

/**
 * This custom MultiAutoCompleteTextView.Tokenizer splits a CharSequence string into words using
 * only spaces as the delimiter. It is used to split up the String which is going to be used by
 * BibleSearch for the MultiAutoCompleteTextView Tokenizer.
 */
public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    /**
     * Returns the start of the token that ends at offset cursor within text. Using a pointer
     * <code>int i</code> and starting at the position <code>cursor</code> we first back up until
     * we find the first char which has a blank char in front of it in the parameter passed us in
     * <code>CharSequence text</code>, then we go forward, stopping when we either find a non-blank
     * char or reach the cursor pointer again. We then return our pointer <code>i</code> as the
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
     * Returns the end of the token (minus trailing punctuation) that begins at offset
     * <code>cursor</code> within <code>text</code>. Starting at <code>cursor</code> we
     * increment a pointer <code>int i</code> until we find a blank char and when doing
     * so we return <code>i</code> as the end of the token. If we do no find a blank before
     * reaching the end of <code>CharSequence text</code> we return the length of <code>text</code>
     *
     * as the end of the token.
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
     * Returns <code>text</code>, modified, if necessary, to ensure that
     * it ends with a space character
     *
     * @param text Text we are working on
     * @return text terminated by single space
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();

        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }

        if (i > 0 && text.charAt(i - 1) == ' ') {
            return text;
        } else {
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text + " ");
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text + " ";
            }
        }
    }
}

