package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.DefaultWordDB;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class UpdateWordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.wordSpinner) Spinner wordSelector;

    private static final String TAG = "MyUpdateWordFragment";

    public UpdateWordFragment() {
        // Required empty public constructor
    }

    public static UpdateWordFragment newInstance() {
        UpdateWordFragment fragment = new UpdateWordFragment();
        return fragment;
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

        // Initialize cursor adapter
        Cursor cur = DefaultWordDB.getInstance().rawQuery(String.format("SELECT %1$s, %2$s FROM %3$s ORDER BY %1$s ASC", WordDBOpenHelper.COLUMN_WORD, WordDBOpenHelper.COLUMN_TYPE, WordDBOpenHelper.TABLE_NAME), null);
        ArrayAdapter<SpannableStringBuilder>adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item);
        while(cur.moveToNext()) {
            SpannableStringBuilder b = new SpannableStringBuilder();
            b.append(cur.getString(0), new StyleSpan(Typeface.BOLD), Spanned.SPAN_COMPOSING)
                    .append(" ")
                    .append(cur.getString(1), new StyleSpan(Typeface.ITALIC), Spanned.SPAN_COMPOSING);
            adapter.add(b);
        }

        cur.close();
        // Set Cursor Adapter for word spinner
        wordSelector.setAdapter(adapter);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach invoked");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach invoked");
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
