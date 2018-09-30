package xyz.ammo.vocabularybuilder;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApplication extends Application {

    private static Context mContext;
    private static String sharedPrefsName = "MyFlashcards";
    private static final String TAG = "MyApplication";

    public static final String TIME_INTERVAL = "TimeInterval";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getMyAppContext() {
        return mContext;
    }

    public static SharedPreferences getDefaultSharedPreferences() {
        return mContext.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
    }
}
