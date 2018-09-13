package xyz.ammo.vocabularybuilder.databaseui;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import xyz.ammo.vocabularybuilder.R;

public class DBEditActivity extends AppCompatActivity {
    private static final String TAG = "MyDBEditActivity";

    private Fragment addWordFragment;
    private Fragment updateWordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbedit);

        addWordFragment = AddWordFragment.newInstance();
        updateWordFragment = UpdateWordFragment.newInstance();

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selected = null;
                switch(menuItem.getItemId()) {
                    case R.id.addWord:
                        selected = addWordFragment;
                        break;
                    case R.id.updateWord:
                        selected = updateWordFragment;
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

        // Set first fragment by default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.scrollView, addWordFragment);
        transaction.commit();

    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
