package xyz.ammo.vocabularybuilder.databaseui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.R;
import xyz.ammo.vocabularybuilder.storage.DefaultWordDB;

public class AddWordFragment extends Fragment {

    @BindView(R.id.tiet1) TextView wordTv;
    @BindView(R.id.tiet2) Spinner typeTv;
    @BindView(R.id.tiet3) TextView meaningTv;
    @BindView(R.id.tiet4) TextView synonymTv;
    @BindView(R.id.tiet5) TextView exampleTv;

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


        return view;
    }

    @OnClick(R.id.addWordButton) public void addWord() {
        DefaultWordDB db = DefaultWordDB.getInstance();
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
