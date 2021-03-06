package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.WordSQLiteDatabase;
import xyz.ammo.vocabularybuilder.utils.Intents;

public class AddWordFragment extends Fragment {

    @BindView(R.id.tiet1) TextView wordTv;
    @BindView(R.id.tiet2) Spinner typeTv;
    @BindView(R.id.tiet3) TextView meaningTv;
    @BindView(R.id.tiet4) TextView synonymTv;
    @BindView(R.id.tiet5) TextView exampleTv;

    @BindView(R.id.searchButton) Button searchButton;
    @BindView(R.id.addWordButton) Button addWordButton;

    private static final String TAG = "MyAddWordFragment";

    public AddWordFragment() {
        // Required empty public constructor
    }

    public static AddWordFragment newInstance() {
        return new AddWordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate invoked");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_word, container, false);
        ButterKnife.bind(this, view);
        
        typeTv.setAdapter(ArrayAdapter.createFromResource(this.getContext(), R.array.word_types, android.R.layout.simple_spinner_dropdown_item));
        searchButton.setEnabled(false);
        addWordButton.setEnabled(false);

        return view;
    }

    @OnClick(R.id.addWordButton) public void addWord() {
        WordSQLiteDatabase db = WordSQLiteDatabase.getDefaultInstance();
        long ret = db.insert(wordTv.getText().toString(), typeTv.getSelectedItem().toString(),
                             meaningTv.getText().toString(), synonymTv.getText().toString(),
                             exampleTv.getText().toString());
        if(ret > 0) {
            Log.d(TAG, "Word entered in database");
            Toast.makeText(this.getContext(), "Word entered in database", Toast.LENGTH_SHORT).show();
            // Clear all text views as well
            wordTv.setText("");
            typeTv.setSelection(0);
            meaningTv.setText("");
            synonymTv.setText("");
            exampleTv.setText("");
        }
        else {
            Log.e(TAG, "Error in adding word to database");
            Toast.makeText(this.getContext(), "Error! Please check the field constraints", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.searchButton) public void searchWord() {
        String searchQuery = wordTv.getText().toString();
        searchQuery.replace(' ', '+');
        Uri uri = Uri.parse("https://www.google.com/#q=define:" + searchQuery);
        Intents.maybeStartActivity(this.getContext(), new Intent(Intent.ACTION_VIEW, uri));
    }

    @OnTextChanged(R.id.tiet1) public void onWordTextChanged(CharSequence text) {
        if(text.toString().trim().length() == 0) {
          searchButton.setEnabled(false);
          addWordButton.setEnabled(false);
        }
        else if(!addWordButton.isEnabled()) {
          searchButton.setEnabled(true);
          addWordButton.setEnabled(true);
        }
    }

    @Override 
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach invoked");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach invoked");
    }
}
