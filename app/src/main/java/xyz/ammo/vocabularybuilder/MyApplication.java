package xyz.ammo.vocabularybuilder;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getMyAppContext() {
        return mContext;
    }
}
