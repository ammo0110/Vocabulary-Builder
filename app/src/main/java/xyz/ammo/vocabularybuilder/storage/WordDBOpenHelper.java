package xyz.ammo.vocabularybuilder.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordDBOpenHelper extends SQLiteOpenHelper {

    public static final String COLUMN_WORD = "Word";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_MEANING = "ShortMeaning";
    public static final String COLUMN_SYNONYMS = "Synonyms";

    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "MyWordDBOpenHelper";

    public WordDBOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate Invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade Invoked");
    }
}
