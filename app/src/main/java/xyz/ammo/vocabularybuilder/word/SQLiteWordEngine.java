package xyz.ammo.vocabularybuilder.word;

import android.database.Cursor;
import android.util.Log;

import java.util.Random;

import xyz.ammo.vocabularybuilder.storage.DefaultWordDB;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class SQLiteWordEngine implements WordEngine {

    private Cursor cur;
    private int size;   // The number of words in database

    private static final String TAG = "MySQLiteEngine";

    public SQLiteWordEngine() {
        DefaultWordDB primaryDB = DefaultWordDB.getInstance();

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
        this.cur = primaryDB.rawQuery(String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s FROM %6$s ORDER BY %1$s ASC",
                WordDBOpenHelper.COLUMN_WORD,
                WordDBOpenHelper.COLUMN_TYPE,
                WordDBOpenHelper.COLUMN_MEANING,
                WordDBOpenHelper.COLUMN_SYNONYMS,
                WordDBOpenHelper.COLUMN_EXAMPLE,
                WordDBOpenHelper.TABLE_NAME), null);
    }

    public int getSize() {
        return size;
    }

    public void closeEngine() {
        this.cur.close();
    }

    @Override
    public WordTuple getNext(boolean randomize) {
        if(randomize) {
            Random rand = new Random();
            int index = rand.nextInt(size);
            cur.moveToPosition(index);
        }
        else if(!cur.moveToNext()) {
            // Move cursor to the beginning
            cur.moveToFirst();
        }
        return new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
    }
}
