package xyz.ammo.vocabularybuilder.settings;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.MyApplication;
import xyz.ammo.vocabularybuilder.R;

public class SettingsActivity extends AppCompatActivity implements TimeIntervalDialogFragment.OnClickListener {

    private static final String TAG = "MySettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.textView4) public void showDialog() {
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        int interval = prefs.getInt(MyApplication.TIME_INTERVAL, 1000);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        TimeIntervalDialogFragment tidf = TimeIntervalDialogFragment.newInstance(interval);
        tidf.show(ft, "dialog");
    }

    @Override
    public void onDialogButtonClick(int interval, int whichButton) {
        if(whichButton == Dialog.BUTTON_POSITIVE) {
            Log.d(TAG, "Interval value received: " + interval);
            SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
            prefs.edit().putInt(MyApplication.TIME_INTERVAL, interval).apply();
        }
    }
}
