package xyz.ammo.vocabularybuilder.settings;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import xyz.ammo.vocabularybuilder.BaseActivity;
import xyz.ammo.vocabularybuilder.MyApplication;
import xyz.ammo.vocabularybuilder.R;

public class SettingsActivity extends BaseActivity implements TimeIntervalDialogFragment.OnClickListener {

    private int mInterval;

    @BindView(R.id.switchTheme) CheckBox themeSelector;

    private static final String TAG = "MySettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate invoked");
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        mInterval = prefs.getInt(MyApplication.TIME_INTERVAL, 1000);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        ((TextView)findViewById(R.id.intervalLabel)).setText(String.format("%.1f s", mInterval/1000.0));
        themeSelector.setChecked(mIsDarkTheme);
    }

    /* For Time Interval Setting */
    public void showTimeIntervalDialog(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        TimeIntervalDialogFragment tidf = TimeIntervalDialogFragment.newInstance(mInterval);
        tidf.show(ft, "dialog");
    }

    @Override
    public void onDialogButtonClick(int interval, int whichButton) {
        if(whichButton == Dialog.BUTTON_POSITIVE) {
            Log.d(TAG, "Interval value received: " + interval);
            SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
            prefs.edit().putInt(MyApplication.TIME_INTERVAL, interval).apply();
            mInterval = interval;
            ((TextView)findViewById(R.id.intervalLabel)).setText(String.format("%.1f s", mInterval/1000.0));
        }
    }

    // For Dark Theme Setting
    public void switchTheme(View view) {
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        mIsDarkTheme = !mIsDarkTheme;
        if(view.getId() != R.id.switchTheme) {
            themeSelector.setChecked(mIsDarkTheme);
        }
        prefs.edit().putBoolean(MyApplication.DARK_THEME, mIsDarkTheme).apply();
        // Recreate the activity
        recreate();
    }
}
