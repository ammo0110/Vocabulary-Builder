package xyz.ammo.vocabularybuilder.storage;

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
}
