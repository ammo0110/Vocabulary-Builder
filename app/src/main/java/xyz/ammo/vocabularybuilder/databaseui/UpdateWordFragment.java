package xyz.ammo.vocabularybuilder.databaseui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.WordSQLiteDatabase;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class UpdateWordFragment extends Fragment {

    @BindView(R.id.wordSpinner) Spinner wordSelector;

    @BindView(R.id.uTiet1) TextView wordTv;
    @BindView(R.id.uTiet2) Spinner typeTv;
    @BindView(R.id.uTiet3) TextView meaningTv;
    @BindView(R.id.uTiet4) TextView synonymTv;
    @BindView(R.id.uTiet5) TextView exampleTv;

    @BindView(R.id.updateWordButton) Button updateWordButton;

    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mList;   // The list backing the array adapter

    private static final String TAG = "MyUpdateWordFragment";

    public UpdateWordFragment() {
        // Required empty public constructor
    }

    public static UpdateWordFragment newInstance() {
        return new UpdateWordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate invoked");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_word, container, false);
        ButterKnife.bind(this, view);

        typeTv.setAdapter(ArrayAdapter.createFromResource(this.getContext(), R.array.word_types, android.R.layout.simple_spinner_dropdown_item));
        mList = new ArrayList<>(WordSQLiteDatabase.getDefaultInstance().getWordCount());
        mAdapter = new MyArrayAdapter(this.getContext(), R.layout.wordtype_spinner_item, mList);
        mAdapter.add("Select Word to Update");
        wordSelector.setAdapter(mAdapter);
        updateWordButton.setEnabled(false);

        wordSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    // Disable Update Button
                    updateWordButton.setEnabled(false);
                    return;
                }
                String label = parent.getItemAtPosition(position).toString();
                int m = label.lastIndexOf("(");
                String word = label.substring(0, m);
                String type = label.substring(m+1, label.length()-1);   // Removing parenthesis
                Log.d(TAG, "Word selected: " + word);
                Log.d(TAG, "Type selected: " + type);

                // Start a fill query task
                new FormFillQueryTask().execute(word, type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        new SpinnerQueryTask().execute();
        return view;
    }

    @OnClick(R.id.updateWordButton) void updateWord() {
        if(wordSelector.getSelectedItemPosition() == 0) {
            Toast.makeText(this.getContext(), "Error! Select a word to update", Toast.LENGTH_SHORT).show();
            return;
        }
        String label = wordSelector.getSelectedItem().toString();
        int m = label.lastIndexOf("(");
        String word = label.substring(0, m);
        String type = label.substring(m+1, label.length()-1);   // Removing parenthesis
        String newWord = wordTv.getText().toString().trim();
        String newType = typeTv.getSelectedItem().toString();

        WordSQLiteDatabase db = WordSQLiteDatabase.getDefaultInstance();
        int ret = db.update(newWord, newType, meaningTv.getText().toString(),
                synonymTv.getText().toString(), exampleTv.getText().toString(), word, type);

        if(ret > 0) {
            Log.d(TAG, "Word updated in database");

            // Change the entry text
            mList.set(wordSelector.getSelectedItemPosition(), newWord + "(" + newType + ")");
            mAdapter.notifyDataSetChanged();

            Toast.makeText(this.getContext(), "Word updated in database", Toast.LENGTH_SHORT).show();
            // Reset the form
            wordSelector.setSelection(0);
            wordTv.setText("");
            typeTv.setSelection(0);
            meaningTv.setText("");
            synonymTv.setText("");
            exampleTv.setText("");
        }
        else {
            Log.e(TAG, "Error in updating word to database");
            Toast.makeText(this.getContext(), "Error! Please check the field constraints", Toast.LENGTH_SHORT).show();
        }
    }

    @OnTextChanged(R.id.uTiet1) public void onWordTextChanged(CharSequence text) {
        if(text.toString().trim().length() == 0 || wordSelector.getSelectedItemPosition() == 0) {
          updateWordButton.setEnabled(false);
        }
        else if(!updateWordButton.isEnabled()) {
          updateWordButton.setEnabled(true);
        }
    }

    private class SpinnerQueryTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        public Cursor doInBackground(Void... voids) {
            return WordSQLiteDatabase.getDefaultInstance().rawQuery(String.format("SELECT %s, %s FROM %s",
                  WordDBOpenHelper.COLUMN_WORD,
                  WordDBOpenHelper.COLUMN_TYPE,
                  WordDBOpenHelper.TABLE_NAME), null);
        }

        @Override
        protected void onPostExecute(Cursor cur) {
            while(cur.moveToNext()) {
                String word = cur.getString(0);
                String type = cur.getString(1);
                mAdapter.add(word + "(" + type + ")");
            }
            cur.close();
        }
    }

    private class FormFillQueryTask extends AsyncTask<String, Void, Cursor> {

        @Override
        public Cursor doInBackground(String... strings) {
            return WordSQLiteDatabase.getDefaultInstance().rawQuery(String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s FROM %6$s WHERE %1$s=? AND %2$s=?",
                    WordDBOpenHelper.COLUMN_WORD,
                    WordDBOpenHelper.COLUMN_TYPE,
                    WordDBOpenHelper.COLUMN_MEANING,
                    WordDBOpenHelper.COLUMN_SYNONYMS,
                    WordDBOpenHelper.COLUMN_EXAMPLE,
                    WordDBOpenHelper.TABLE_NAME), new String[]{strings[0], strings[1]});
        }

        @Override
        public void onPostExecute(Cursor cur) {
            if(cur.moveToFirst()) {
                wordTv.setText(cur.getString(0));
                meaningTv.setText(cur.getString(2));
                synonymTv.setText(cur.getString(3));
                exampleTv.setText(cur.getString(4));

                // Select the spinner item
                String type = cur.getString(1);
                if(type.equals("Noun")) {
                    typeTv.setSelection(0);
                }
                else if(type.equals("Verb")) {
                    typeTv.setSelection(1);
                }
                else if(type.equals("Adjective")) {
                    typeTv.setSelection(2);
                }
                else if(type.equals("Adverb")) {
                    typeTv.setSelection(3);
                }
                else {
                    Log.e(TAG, "Unhandled type: " + type);
                }
            }
            cur.close();
        }
    }
}
