package xyz.ammo.vocabularybuilder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class DBEditActivity extends AppCompatActivity {

    @BindView(R.id.tiet1) TextView wordTv;
    @BindView(R.id.tiet2) TextView typeTv;
    @BindView(R.id.tiet3) TextView meaningTv;
    @BindView(R.id.tiet4) TextView synonymTv;

    private static final String TAG = "MyDBEditActivity";

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbedit);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.addWordButton) void addWordToDatabase() {
        String dbPath = getIntent().getExtras().getString(HomeActivity.KEY_USERDB);
        database = new WordDBOpenHelper(getApplicationContext(), dbPath).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WordDBOpenHelper.COLUMN_WORD, wordTv.getText().toString());
        values.put(WordDBOpenHelper.COLUMN_TYPE, typeTv.getText().toString());
        values.put(WordDBOpenHelper.COLUMN_MEANING, meaningTv.getText().toString());
        values.put(WordDBOpenHelper.COLUMN_SYNONYMS, synonymTv.getText().toString());

        if(database.insert(WordDBOpenHelper.TABLE_NAME, null, values) > 0) {
            Log.d(TAG, "Word entered in database");
            database.close();
            finish();
        }
        else {
            Log.e(TAG, "Error in adding word to database");
        }
    }

}
