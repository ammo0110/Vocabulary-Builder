package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.DefaultWordDB;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class UpdateWordFragment extends Fragment {

    @BindView(R.id.wordSpinner) Spinner wordSelector;

    @BindView(R.id.uTiet1) TextView wordTv;
    @BindView(R.id.uTiet2) AutoCompleteTextView typeTv;
    @BindView(R.id.uTiet3) TextView meaningTv;
    @BindView(R.id.uTiet4) TextView synonymTv;
    @BindView(R.id.uTiet5) TextView exampleTv;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_word, container, false);
        ButterKnife.bind(this, view);

        // Hack for emulating material theme spinners
        typeTv.setAdapter(ArrayAdapter.createFromResource(this.getContext(), R.array.word_types, android.R.layout.simple_spinner_dropdown_item));
        typeTv.setKeyListener(null);
        typeTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView)v).showDropDown();
                return false;
            }
        });

        ArrayAdapter<SpannableString>adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.select_dialog_item);
        wordSelector.setAdapter(adapter);
        adapter.add(new SpannableString("Select Word to Update"));
        wordSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    // Ignore
                    return;
                }
                String[] tokens = parent.getItemAtPosition(position).toString().split("\\(");
                String word = tokens[0];
                String type = tokens[1].substring(0, tokens[1].length()-1);

                // Start a fill query task
                new FormFillQueryTask().execute(word, type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        SpinnerQueryTask queryTask = new SpinnerQueryTask(adapter);
        queryTask.execute();
        return view;
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

    private class SpinnerQueryTask extends AsyncTask<Void, Void, Cursor> {

        private ArrayAdapter<SpannableString> mAdapter;

        public SpinnerQueryTask(ArrayAdapter<SpannableString> adapter) {
            mAdapter = adapter;
        }

        @Override
        public Cursor doInBackground(Void... voids) {
            return DefaultWordDB.getInstance().rawQuery(String.format("SELECT %1$s, %2$s FROM %3$s ORDER BY %1$s ASC", WordDBOpenHelper.COLUMN_WORD, WordDBOpenHelper.COLUMN_TYPE, WordDBOpenHelper.TABLE_NAME), null);
        }

        @Override
        protected void onPostExecute(Cursor cur) {
            while(cur.moveToNext()) {
                String word = cur.getString(0);
                String type = cur.getString(1);
                SpannableString s = new SpannableString(word + "(" + type + ")");
                s.setSpan(new StyleSpan(Typeface.BOLD), 0, word.length(), Spanned.SPAN_COMPOSING);
                s.setSpan(new StyleSpan(Typeface.ITALIC), word.length(), word.length()+type.length()+2, Spanned.SPAN_COMPOSING);
                mAdapter.add(s);
            }
            cur.close();
        }
    }

    private class FormFillQueryTask extends AsyncTask<String, Void, Cursor> {

        @Override
        public Cursor doInBackground(String... strings) {
            return DefaultWordDB.getInstance().rawQuery(String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s FROM %6$s WHERE %1$s = '%7$s' AND %2$s = '%8$s'",
                    WordDBOpenHelper.COLUMN_WORD,
                    WordDBOpenHelper.COLUMN_TYPE,
                    WordDBOpenHelper.COLUMN_MEANING,
                    WordDBOpenHelper.COLUMN_SYNONYMS,
                    WordDBOpenHelper.COLUMN_EXAMPLE,
                    WordDBOpenHelper.TABLE_NAME,
                    strings[0],
                    strings[1]), null);
        }

        @Override
        public void onPostExecute(Cursor cur) {
            if(cur.moveToFirst()) {
                wordTv.setText(cur.getString(0));
                typeTv.setText(cur.getString(1));
                meaningTv.setText(cur.getString(2));
                synonymTv.setText(cur.getString(3));
                exampleTv.setText(cur.getString(4));
            }
            cur.close();
        }
    }
}
