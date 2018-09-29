package xyz.ammo.vocabularybuilder.word;

import android.database.Cursor;
import android.util.Log;

import java.util.Random;

import xyz.ammo.vocabularybuilder.storage.WordSQLiteDatabase;
import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class SQLiteWordEngine implements WordEngine {

    private WordSQLiteDatabase database;

    private WordTuple[] mList;  // This will hold all the words
    private int mItr;   // The counter used to iterate mList
    private int size;   // The number of words in database

    private static final String TAG = "MySQLiteEngine";

    public SQLiteWordEngine() {
        database = WordSQLiteDatabase.getDefaultInstance();

        size = database.getWordCount();
        Log.d(TAG, "Word count is: " + size);
        mList = new WordTuple[size];

        Cursor cur = database.rawQuery(String.format("SELECT %1$s, %2$s, %3$s, %4$s, %5$s FROM %6$s ORDER BY %1$s ASC",
                WordDBOpenHelper.COLUMN_WORD,
                WordDBOpenHelper.COLUMN_TYPE,
                WordDBOpenHelper.COLUMN_MEANING,
                WordDBOpenHelper.COLUMN_SYNONYMS,
                WordDBOpenHelper.COLUMN_EXAMPLE,
                WordDBOpenHelper.TABLE_NAME), null);

        for(int idx = 0; cur.moveToNext(); idx++) {
            mList[idx] = new WordTuple(cur.getString(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4));
        }
        cur.close();
        mItr = 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public WordTuple getNext() {
        if(mItr == size) {
            // Start from beginning
            mItr = 0;
        }
        return mList[mItr++];
    }

    @Override
    public WordTuple getRandom() {
        Random rand = new Random();
        mItr = rand.nextInt(size);
        return mList[mItr++];
    }
}
