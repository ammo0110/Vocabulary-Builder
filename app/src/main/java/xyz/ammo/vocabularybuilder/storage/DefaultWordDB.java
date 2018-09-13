package xyz.ammo.vocabularybuilder.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import xyz.ammo.vocabularybuilder.MyApplication;

public class DefaultWordDB {

    private static final DefaultWordDB mInstance = new DefaultWordDB(); // Single instance

    private Context mContext;   // Global Application Context
    private String  dbName = "UserWords.db";     // Name of the default database
    private WordDBOpenHelper mHelper; // Used to open and access default database

    private DefaultWordDB() {
        mContext = MyApplication.getMyAppContext();
        String dbPath = mContext.getFilesDir() + "/" + dbName;
        mHelper = new WordDBOpenHelper(mContext, dbPath);
    }

    public static DefaultWordDB getInstance() {
        return mInstance;
    }

    public Cursor rawQuery(String query, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.rawQuery(query, selectionArgs);
    }

    public long insert(String word, String type, String meaning, String synonyms, String examples) {
        // Add words to database
        ContentValues values = new ContentValues();
        values.put(WordDBOpenHelper.COLUMN_WORD, word.trim());
        values.put(WordDBOpenHelper.COLUMN_TYPE, type.trim());
        values.put(WordDBOpenHelper.COLUMN_MEANING, meaning.trim());
        values.put(WordDBOpenHelper.COLUMN_SYNONYMS, synonyms.trim());
        values.put(WordDBOpenHelper.COLUMN_EXAMPLE, examples.trim());

        return mHelper.getWritableDatabase().insert(WordDBOpenHelper.TABLE_NAME, null, values);
    }
}
