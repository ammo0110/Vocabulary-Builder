package xyz.ammo.vocabularybuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.BindView;
import xyz.ammo.vocabularybuilder.word.SQLiteWordEngine;
import xyz.ammo.vocabularybuilder.word.WordTuple;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.word) TextView wordTv;
    @BindView(R.id.meaning) TextView meaningTv;

    private SQLiteWordEngine engine;
    private Runnable meaningChangeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        String localPath = getFilesDir() + "/" + "PrimaryWords.db";
        engine = new SQLiteWordEngine(getApplicationContext(), localPath);

        // Show the first word on the screen
        WordTuple tuple = engine.getNext();
        wordTv.setText(tuple.getWord());
        meaningTv.setText(tuple.getShortMeaning());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
