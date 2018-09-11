package xyz.ammo.vocabularybuilder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        // Set Cursor Adapter for word spinner
        wordSelector.setAdapter(((DBEditActivity)getActivity()).getAdapter());
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
