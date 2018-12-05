package xyz.ammo.vocabularybuilder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    protected boolean mIsDarkTheme;

    private static final String THEME = "DARK_THEME";
    private static final String TAG = "MyBaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        mIsDarkTheme = savedInstanceState != null ? savedInstanceState.getBoolean(THEME) : prefs.getBoolean(MyApplication.DARK_THEME, false);
        if(mIsDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        if(mIsDarkTheme != prefs.getBoolean(MyApplication.DARK_THEME, false)) {
            Log.d(TAG, "Recreating Activity");
            mIsDarkTheme = !mIsDarkTheme;
            recreate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean(THEME, mIsDarkTheme);
        super.onSaveInstanceState(outBundle);
    }
}
