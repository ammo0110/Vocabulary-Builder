package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.R;

public class AddWordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.tiet1) TextView wordTv;
    @BindView(R.id.tiet2) TextView typeTv;
    @BindView(R.id.tiet3) TextView meaningTv;
    @BindView(R.id.tiet4) TextView synonymTv;
    @BindView(R.id.tiet5) TextView exampleTv;
    
    // Key strings for fetching text from these text views
    public static final String KEY_WORD = "WordAddDB";
    public static final String KEY_TYPE = "TypeAddDB";
    public static final String KEY_MEANING = "MeaningAddDB";
    public static final String KEY_SYNONYM = "SynonymAddDB";
    public static final String KEY_EXAMPLE = "ExampleAddDB";

    private static final String TAG = "MyAddWordFragment";

    public AddWordFragment() {
        // Required empty public constructor
    }

    public static AddWordFragment newInstance() {
        AddWordFragment fragment = new AddWordFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_word, container, false);
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

        return view;
    }

    @OnClick(R.id.addWordButton) public void sendWordForAddition() {
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("custom")
                .authority("add")
                .appendQueryParameter(KEY_WORD, wordTv.getText().toString().trim())
                .appendQueryParameter(KEY_TYPE, typeTv.getText().toString().trim())
                .appendQueryParameter(KEY_MEANING, meaningTv.getText().toString().trim())
                .appendQueryParameter(KEY_SYNONYM, synonymTv.getText().toString().trim())
                .appendQueryParameter(KEY_EXAMPLE, exampleTv.getText().toString().trim())
                .build();
        Log.d(TAG, "Add Word Button Pressed");
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
        void onFragmentInteraction(Uri uri);
    }
}
