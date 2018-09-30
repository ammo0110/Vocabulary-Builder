package xyz.ammo.vocabularybuilder.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import xyz.ammo.vocabularybuilder.MyApplication;
import xyz.ammo.vocabularybuilder.R;

public class TimeIntervalDialogFragment extends DialogFragment {

    // The time interval in milliseconds
    private int mInterval;

    // For calculating time interval in milliseconds
    private final int offset = 1000;
    private final int slope = 500;

    // Implement this listener in the parent activity
    private OnClickListener mListener;

    private static final String TAG = "MyTIDialogFragment";

    public TimeIntervalDialogFragment() {

    }

    public static TimeIntervalDialogFragment newInstance(int interval) {
        TimeIntervalDialogFragment ret = new TimeIntervalDialogFragment();

        Bundle args = new Bundle();
        args.putInt(MyApplication.TIME_INTERVAL, interval);
        ret.setArguments(args);

        return ret;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate invoked");
        mInterval = getArguments().getInt(MyApplication.TIME_INTERVAL);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnClickListener) {
            mListener = (OnClickListener)context;
        }
        else {
            throw new ClassCastException(context.toString() + " must implement TimeIntervalDialogFragment.OnClickListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog invoked");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.seekbar_alert_dialog, null);

        final TextView label = view.findViewById(R.id.valueLabel);
        label.setText(String.format("%.1fs", mInterval/1000.0));
        SeekBar seek = view.findViewById(R.id.seekBar);
        seek.setProgress((mInterval-offset)/slope);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mInterval = i*slope + offset;
                label.setText(String.format("%.1fs", mInterval/1000.0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int whichButton) {
                mListener.onDialogButtonClick(mInterval, whichButton);
            }
        };
        builder.setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .setView(view)
                .setTitle("Interval");

        // Create the alert dialog object and return it
        return builder.create();
    }

    public interface OnClickListener {

        void onDialogButtonClick(int interval, int whichButton);
    }
}