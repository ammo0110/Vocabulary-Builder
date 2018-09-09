package xyz.ammo.vocabularybuilder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.net.Uri;

import xyz.ammo.vocabularybuilder.storage.WordDBOpenHelper;

public class DBEditActivity extends AppCompatActivity implements AddWordFragment.OnFragmentInteractionListener {

    private static final String TAG = "MyDBEditActivity";

    private Fragment addWordFragment;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbedit);

        String dbPath = getIntent().getExtras().getString(HomeActivity.KEY_USERDB);
        database = new WordDBOpenHelper(getApplicationContext(), dbPath).getWritableDatabase();

        addWordFragment = AddWordFragment.newInstance();

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected = null;
                    switch(menuItem.getItemId()) {
                        case R.id.addWord:
                            selected = addWordFragment;
                            break;
                        default:
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.scrollView, selected);
                    transaction.commit();
                    return true;
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.scrollView, addWordFragment);
        transaction.commit();

    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, uri.getAuthority());
        if(uri.getAuthority().equals("add")) {
            // Add words to database 
            ContentValues values = new ContentValues();
            values.put(WordDBOpenHelper.COLUMN_WORD, uri.getQueryParameter(AddWordFragment.KEY_WORD));
            values.put(WordDBOpenHelper.COLUMN_TYPE, uri.getQueryParameter(AddWordFragment.KEY_TYPE));
            values.put(WordDBOpenHelper.COLUMN_MEANING, uri.getQueryParameter(AddWordFragment.KEY_MEANING));
            values.put(WordDBOpenHelper.COLUMN_SYNONYMS, uri.getQueryParameter(AddWordFragment.KEY_SYNONYM));
            values.put(WordDBOpenHelper.COLUMN_EXAMPLE, uri.getQueryParameter(AddWordFragment.KEY_EXAMPLE));

            if(database.insert(WordDBOpenHelper.TABLE_NAME, null, values) > 0) {
                Log.d(TAG, "Word entered in database");
                Toast.makeText(this, "Word entered in database", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Log.e(TAG, "Error in adding word to database");
                Toast.makeText(this, "Error! Please check the field constraints", Toast.LENGTH_SHORT).show();
            }
        }
    } 

}
