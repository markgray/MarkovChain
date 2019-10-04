package com.example.android.markovchain;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * {@code DialogFragment} to display number of possibilities given the first two words, and the verse
 * we randomly generated using {@code Markov.line}.
 */
public class MarkovDialog extends DialogFragment {
    /**
     * Possibility statistics for the current verse
     */
    public String mPossibles;
    /**
     * Text of the current verse
     */
    public String mVerse;

    /**
     * Factory method for constructing and initializing a {@code MarkovDialog} instance. First we
     * initialize our variables {@code MarkovDialog f} and {@code Bundle args} with new instances.
     * Then we store our parameter {@code String possibles} in {@code args} under the key "possibles",
     * and our parameter {@code String verse} under the key "verse". We set the arguments of {@code f}
     * to {@code args} and return {@code f} to the caller.
     *
     * @param possibles Possibility statistics for the current verse
     * @param verse     Text of the current verse
     * @return a {@code MarkovDialog} whose argument {@code Bundle} contains our parameters
     */
    public static MarkovDialog newInstance(String possibles, String verse) {
        MarkovDialog f = new MarkovDialog();

        Bundle args = new Bundle();
        args.putString("possibles", possibles);
        args.putString("verse", verse);

        f.setArguments(args);

        return f;
    }

    /**
     * Called to do initial creation of a {@code DialogFragment}. First we call our super's implementation
     * of {@code onCreate}. We then initialize our field {@code String mPossibles} with the string stored
     * in our argument {@code Bundle} under the key "possibles", and our field {@code String mVerse} with
     * the string stored in our argument {@code Bundle} under the key "verse". Finally we set our style
     * to STYLE_NORMAL without a custom theme.
     *
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPossibles = getArguments().getString("possibles");
        mVerse = getArguments().getString("verse");

        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    /**
     * Called to have the {@code DialogFragment} instantiate its user interface view. We initialize our
     * variable {@code View v} by using our parameter {@code LayoutInflater inflater} to inflate our
     * layout file R.layout.markov_dialog, using our parameter {@code ViewGroup container} for layout
     * params without attaching to it. We initialize our variable {@code TextView pv} by finding the view
     * in {@code v} with id R.id.possibles and set its text to our field {@code String mPossibles} and
     * initialize our variable {@code TextView vv} by finding the view in {@code v} with id R.id.verse
     * and set its text to our field {@code String mVerse}. Next we initialize our variable {@code Button db}
     * by finding the view in {@code v} with id R.id.dismiss and set its {@code OnClickListener} to an
     * anonymous class whose {@code onClick} override calls the {@code dismiss()} method to dismiss our
     * fragment and its dialog. Finally we return {@code v} to the caller.
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI will be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate
     *                  the LayoutParams of the view.
     * @param savedInstanceState We do not override {@code onSaveInstanceState} so do not use.
     * @return Return the View for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.markov_dialog, container, false);
        TextView pv = v.findViewById(R.id.possibles);
        pv.setText(mPossibles);
        TextView vv = v.findViewById(R.id.verse);
        vv.setText(mVerse);

        Button db = v.findViewById(R.id.dismiss);
        db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
