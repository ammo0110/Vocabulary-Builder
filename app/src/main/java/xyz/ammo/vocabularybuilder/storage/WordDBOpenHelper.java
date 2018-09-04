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
    public static final String COLUMN_EXAMPLE = "Example";

    public static final String TABLE_NAME = "Words";

    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "MyWordDBOpenHelper";

    public WordDBOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate Invoked");
        // Create table if it doesn't exist
        String sql = String.format("CREATE TABLE IF NOT EXISTS %1$s(" +
                        "%2$s TEXT NOT NULL CHECK(%2$s != ''), " +
                        "%3$s TEXT CHECK(%3$s == 'Noun' OR %3$s == 'Verb' OR %3$s == 'Adjective' OR %3$s == 'Adverb'), " +
                        "%4$s TEXT, " +
                        "%5$s TEXT, " +
                        "%6$s TEXT, " +
                        "PRIMARY KEY(%2$s, %3$s))",
                TABLE_NAME, COLUMN_WORD, COLUMN_TYPE, COLUMN_MEANING, COLUMN_SYNONYMS, COLUMN_EXAMPLE);
        Log.d(TAG, "SQL query to create table: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade Invoked");
    }
}
