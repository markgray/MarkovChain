package com.example.android.markovchain

import android.widget.MultiAutoCompleteTextView

/**
 * This custom `MultiAutoCompleteTextView.Tokenizer` splits a `CharSequence` string into
 * words using only spaces as the delimiter. It is used to split up the `String` which is going
 * to be used by `BibleSearch` for its `MultiAutoCompleteTextView`
 */
class SpaceTokenizer : MultiAutoCompleteTextView.Tokenizer {

    /**
     * Returns the start of the token that ends at offset cursor within text. Using an [Int] pointer
     * variable `var i` and starting at the position given by our [Int] parameter [cursor] we first
     * back up until we find the first char which has a blank char in front of it in the [CharSequence]
     * parameter passed us in [text], then we go forward, stopping when we either find a non-blank
     * char or reach the [cursor] pointer again. We then return our pointer `i` as the start of the
     * token that ends at offset cursor.
     *
     * @param text   Text we are working on
     * @param cursor Offset within text to start at
     * @return Offset to next token to be considered
     */
    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        var i = cursor

        while (i > 0 && text[i - 1] != ' ') {
            i--
        }
        while (i < cursor && text[i] == ' ') {
            i++
        }

        return i
    }

    /**
     * Returns the end of the token (minus trailing punctuation) that begins at offset [cursor]
     * within our [CharSequence] parameter [text]. Starting at [cursor] we increment an [Int] pointer
     * `var i` until we find a blank char and upon doing so we return `i` as the end of the token. If
     * we do not find a blank before reaching the end of `text` we return the length of `text`
     * as the end of the token.
     *
     * @param text   Text we are working on
     * @param cursor offset within text to start at
     * @return offset of space character that ends the token
     */
    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        var i = cursor
        val len = text.length

        while (i < len) {
            if (text[i] == ' ') {
                return i
            } else {
                i++
            }
        }

        return len
    }

    /**
     * Returns our [CharSequence] parameter [text], modified, if necessary, to ensure that it ends
     * with a space character. If the last char of text is already a blank we just return [text],
     * otherwise we append a blank to the end of [text] and return that.
     *
     * @param text Text we are working on
     * @return text terminated by at least one space
     */
    override fun terminateToken(text: CharSequence): CharSequence {
        return if (text[text.length - 1] == ' ') text else "$text "
    }
}

