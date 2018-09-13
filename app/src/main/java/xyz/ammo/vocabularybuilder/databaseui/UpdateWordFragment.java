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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;

import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.DefaultWordDB;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class UpdateWordFragment extends Fragment {

    @BindView(R.id.wordSpinner) Spinner wordSelector;
    @BindView(R.id.uTiet2) AutoCompleteTextView typeTv;

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
        AutoCompleteTextView actv = (AutoCompleteTextView)typeTv;
        actv.setAdapter(ArrayAdapter.createFromResource(this.getContext(), R.array.word_types, android.R.layout.simple_spinner_dropdown_item));
        actv.setKeyListener(null);
        actv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView)v).showDropDown();
                return false;
            }
        });

        SpinnerQueryTask queryTask = new SpinnerQueryTask(this.getContext(), wordSelector);
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

        private Context mContext;
        private Spinner mSpinner;

        public SpinnerQueryTask(Context context, Spinner spinner) {
            mContext = context;
            mSpinner = spinner;
        }

        @Override
        public Cursor doInBackground(Void... voids) {
            return DefaultWordDB.getInstance().rawQuery(String.format("SELECT %1$s, %2$s FROM %3$s ORDER BY %1$s ASC", WordDBOpenHelper.COLUMN_WORD, WordDBOpenHelper.COLUMN_TYPE, WordDBOpenHelper.TABLE_NAME), null);
        }

        @Override
        protected void onPostExecute(Cursor cur) {
            ArrayAdapter<SpannableString>adapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item);
            while(cur.moveToNext()) {
                String word = cur.getString(0);
                String type = cur.getString(1);
                SpannableString s = new SpannableString(word + " " + type);
                s.setSpan(new StyleSpan(Typeface.BOLD), 0, word.length(), Spanned.SPAN_COMPOSING);
                s.setSpan(new StyleSpan(Typeface.ITALIC), word.length()+1, word.length()+type.length()+1, Spanned.SPAN_COMPOSING);
                adapter.add(s);
            }
            cur.close();
            mSpinner.setAdapter(adapter);
        }
    }
}
