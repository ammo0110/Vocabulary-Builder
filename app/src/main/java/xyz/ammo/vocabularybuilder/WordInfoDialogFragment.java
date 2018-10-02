package xyz.ammo.vocabularybuilder;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.text.SpannableString;

import xyz.ammo.vocabularybuilder.word.WordTuple;

public class WordInfoDialogFragment extends DialogFragment {

    private WordTuple wordInfo;

    public WordInfoDialogFragment() {

    }

    public static WordInfoDialogFragment newInstance(WordTuple wordInfo) {
        WordInfoDialogFragment ret =  new WordInfoDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(GameActivity.CURRENT_WORD, wordInfo);
        ret.setArguments(args);

        return ret;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wordInfo = getArguments().getParcelable(GameActivity.CURRENT_WORD);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.wordinfo_alert_dialog, null);

        ((TextView)view.findViewById(R.id.word)).setText(wordInfo.getWord());
        String type = wordInfo.getType();
        String meaning = wordInfo.getMeaning();
        SpannableString s = new SpannableString(type.toLowerCase() + ": " + meaning.toLowerCase());
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, type.length()+1, SpannableString.SPAN_COMPOSING);
        ((TextView)view.findViewById(R.id.definition)).setText(s);
        ((TextView)view.findViewById(R.id.example)).setText(wordInfo.getExample());
        ((TextView)view.findViewById(R.id.synonyms)).setText(wordInfo.getSynonyms());

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setPositiveButton(android.R.string.ok, null)
                .setView(view);

        return builder.create();
    }
}
