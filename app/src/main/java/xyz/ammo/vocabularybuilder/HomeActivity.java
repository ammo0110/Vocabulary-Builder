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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        String localPath = getFilesDir() + "/" + "PrimaryWords.db";
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

    @OnClick(R.id.choice1) void launchGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
