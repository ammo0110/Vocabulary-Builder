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

        Cursor temp = primaryDB.rawQuery("SELECT COUNT(Word) FROM Words", null);
        if(temp.moveToFirst()) {
            this.size = temp.getInt(0);
            Log.d(TAG, "Word count is: " + size);
        }
        else {
            Log.e(TAG, "Cannot retrieve the word count of database");
        }
        temp.close();

        // This cursor will be used for retrieving words
        this.cur = primaryDB.rawQuery("SELECT Word, Type, ShortMeaning, Synonyms FROM Words", null);
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
            return new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
        }
        if(!cur.moveToNext()) {
            // Move cursor to the beginning
            cur.moveToFirst();
        }
        return new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3));
    }
}
