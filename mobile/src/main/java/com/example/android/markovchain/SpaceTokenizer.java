package com.example.android.markovchain;


//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

/**
 * This custom MultiAutoCompleteTextView.Tokenizer splits a CharSequence string into words using
 * only spaces as the delimiter. It is used to split up the String which is going to be used by
 * BibleSearch for the MultiAutoCompleteTextView Tokenizer.
 */
@SuppressWarnings("WeakerAccess")
public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    /**
     * Returns the start of the token that ends at offset cursor within text. Using a pointer
     * <b>int i</b> and starting at the position <b>cursor</b> we first back up until
     * we find the first char which has a blank char in front of it in the parameter passed us in
     * <b>CharSequence text</b>, then we go forward, stopping when we either find a non-blank
     * char or reach the cursor pointer again. We then return our pointer <b>i</b> as the
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
     * <b>cursor</b> within <b>text</b>. Starting at <b>cursor</b> we
     * increment a pointer <b>int i</b> until we find a blank char and when doing
     * so we return <b>i</b> as the end of the token. If we do no find a blank before
     * reaching the end of <b>CharSequence text</b> we return the length of <b>text</b>
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
     * Returns <b>text</b>, modified, if necessary, to ensure that it ends with a space character.
     * If the last char of text is already a blank we return text, otherwise we append a blank to
     * the end of text and return that.
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

