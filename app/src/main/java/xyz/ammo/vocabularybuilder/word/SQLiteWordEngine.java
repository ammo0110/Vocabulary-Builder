package xyz.ammo.vocabularybuilder.word;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Random;

import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class SQLiteWordEngine implements WordEngine {

    private SQLiteDatabase primaryDB;
    private Cursor cur;
    private int size;   // The number of words in database
    private boolean randomize;

    public static final String TAG = "MySQLiteEngine";

    public SQLiteWordEngine(Context context, String dbName) {
        this.primaryDB = new WordDBOpenHelper(context, dbName).getReadableDatabase();

        Cursor temp = primaryDB.rawQuery(String.format("SELECT COUNT(%s) FROM %s", WordDBOpenHelper.COLUMN_WORD, WordDBOpenHelper.TABLE_NAME), null);
        if(temp.moveToFirst()) {
            this.size = temp.getInt(0);
            Log.d(TAG, "Word count is: " + size);
        }
        else {
            Log.e(TAG, "Cannot retrieve the word count of database");
        }
        temp.close();

        // This cursor will be used for retrieving words
        this.cur = primaryDB.rawQuery(String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                WordDBOpenHelper.COLUMN_WORD,
                WordDBOpenHelper.COLUMN_TYPE,
                WordDBOpenHelper.COLUMN_MEANING,
                WordDBOpenHelper.COLUMN_SYNONYMS,
                WordDBOpenHelper.COLUMN_EXAMPLE,
                WordDBOpenHelper.TABLE_NAME), null);
        this.randomize = false;
    }

    public void closeEngine() {
        this.cur.close();
        this.primaryDB.close();
    }

    @Override
    public WordTuple getNext() {
        if(randomize) {
            Random rand = new Random();
            int index = rand.nextInt(size);
            cur.moveToPosition(index);
            return new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
        }
        if(!cur.moveToNext()) {
            // Move cursor to the beginning
            cur.moveToFirst();
        }
        return new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
    }
}
