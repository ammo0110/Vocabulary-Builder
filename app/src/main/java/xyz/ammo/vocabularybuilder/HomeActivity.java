package xyz.ammo.vocabularybuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.OnClick;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "MyHomeActivity";

    // This is the database which came bundled with the app in assets folder
    private static final String primaryDB = "PrimaryWords.db";
    public static final String KEY_PRIMARYDB = "PrimaryDB";

    // This is the database where words added by the user are stored
    private static final String secondaryDB = "UserWords.db";
    public static final String KEY_USERDB = "UserDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // Copy primary database from assets/databases to local space
        String localPath = getFilesDir() + "/" + primaryDB;
        copyAssetsDBtoLocal(localPath);
    }

    @OnClick(R.id.choice1) void launchGame() {
        Intent intent = new Intent(this, GameActivity.class);

        String primaryLocalPath = getFilesDir() + "/" + primaryDB;
        String userLocalPath = getFilesDir() + "/" + secondaryDB;
        intent.putExtra(KEY_PRIMARYDB, primaryLocalPath);
        intent.putExtra(KEY_USERDB, userLocalPath);

        startActivity(intent);
    }

    @OnClick(R.id.fab) void launchEditDBMenu() {
        Intent intent = new Intent(this, DBEditActivity.class);

        String userLocalPath = getFilesDir() + "/" + secondaryDB;
        intent.putExtra(KEY_USERDB, userLocalPath);

        startActivity(intent);
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
                byte[] buf = new byte[1024];
                while(is.read(buf) > 0) {
                    os.write(buf);
                }
                os.flush();
                is.close();
                os.close();
                Log.d(TAG, "assets/databases/Words.db copied");
            }
            catch(IOException ex) {
                Log.e(TAG, "Exception occurred: " + ex.getMessage());
            }
        }
    }
}
