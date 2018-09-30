package xyz.ammo.vocabularybuilder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.OnClick;
import butterknife.ButterKnife;

import xyz.ammo.vocabularybuilder.databaseui.DBEditActivity;
import xyz.ammo.vocabularybuilder.settings.SettingsActivity;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "MyHomeActivity";

    // This is the database where words added by the user are stored
    private static final String userDB = "UserWords.db";
    public static final String KEY_USERDB = "UserDB";

    // This is the request code for exporting database to external storage
    private static final int WRITE_REQUEST_CODE = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @OnClick(R.id.choice1) void launchGame() {
        Intent intent = new Intent(this, GameActivity.class);

        String userLocalPath = getFilesDir() + "/" + userDB;
        intent.putExtra(KEY_USERDB, userLocalPath);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles presses on the action bar items
        switch(item.getItemId()) {
            case R.id.export_db:
                exportDatabase();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.choice2) void launchEditDBMenu() {
        Intent intent = new Intent(this, DBEditActivity.class);

        String userLocalPath = getFilesDir() + "/" + userDB;
        intent.putExtra(KEY_USERDB, userLocalPath);

        startActivity(intent);
    }

    private void exportDatabase() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType("application/x-sqlite3");
        intent.putExtra(Intent.EXTRA_TITLE, "words.db");
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = resultData.getData();
            Log.d(TAG, "Uri is: " + uri);
            try {
                String userLocalPath = getFilesDir() + "/" + userDB;
                InputStream is = new FileInputStream(userLocalPath);
                OutputStream os = getContentResolver().openOutputStream(uri);
                dumpInputToOutput(is, os);
            }
            catch(IOException ex) {
                Log.e(TAG, "Exception occurred while exporting file: " + ex.getMessage());
            }
        }
    }

    private void copyAssetsDBtoLocal(String localPath) {
        File dbFile = new File(localPath);
        Log.d(TAG, "Primary database location: " + localPath);
        if(!dbFile.exists()) {
            Log.d(TAG, "Going to copy assets/databases/Words.db to local space");
            // Copy database from assets to local directory for the first time
            try {
                InputStream is = getAssets().open("databases/Words.db");
                FileOutputStream os = new FileOutputStream(dbFile);
                dumpInputToOutput(is, os);
                Log.d(TAG, "assets/databases/Words.db copied");
            }
            catch(IOException ex) {
                Log.e(TAG, "Exception occurred: " + ex.getMessage());
            }
        }
    }

    private void dumpInputToOutput(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[1024];
        while(is.read(buf) > 0) {
            os.write(buf);
        }
        os.flush();
        is.close();
        os.close();
    }
}
