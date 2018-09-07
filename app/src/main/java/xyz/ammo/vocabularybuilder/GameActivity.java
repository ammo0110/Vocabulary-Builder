package xyz.ammo.vocabularybuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import ru.noties.markwon.Markwon;

import xyz.ammo.vocabularybuilder.word.SQLiteWordEngine;
import xyz.ammo.vocabularybuilder.word.WordTuple;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.word) TextView wordTv;
    @BindView(R.id.type) TextView typeTv;
    @BindView(R.id.meaning) TextView meaningTv;

    private SQLiteWordEngine engine;
    private Runnable meaningChangeRunnable;
    private static final int MILLIS_DELAY_IN_SHOWING_MEANING = 1000;
    private static final String TAG = "MyGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "onCreate invoked");

        ButterKnife.bind(this);

        String localPath = getIntent().getExtras().getString(HomeActivity.KEY_USERDB);
        engine = new SQLiteWordEngine(getApplicationContext(), localPath);

        // If database is empty, exit game
        if(engine.getSize() == 0) {
            Toast.makeText(this, "Database is empty, please add a word first", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Show the first word on the screen
            WordTuple tuple = engine.getNext();
            Log.d(TAG, "" + tuple);
            wordTv.setText(tuple.getWord());
            typeTv.setText(tuple.getType());
            meaningTv.setText("");  //Empty, if user waits then show else move on

            meaningChangeRunnable = new TextChangeRunnable(meaningTv, tuple.getShortMeaning());
            meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
        }
    }

    @OnClick(R.id.meaning)
    void nextWord() {
        meaningTv.removeCallbacks(meaningChangeRunnable);

        WordTuple tuple = engine.getNext();
        Log.d(TAG, "" + tuple);
        wordTv.setText(tuple.getWord());
        typeTv.setText(tuple.getType());
        meaningTv.setText("");  //Empty, if user waits then show else move on

        meaningChangeRunnable = new TextChangeRunnable(meaningTv, tuple.getShortMeaning());
        meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles presses on the action bar items
        switch(item.getItemId()) {
            case R.id.menu_game_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy invoked");
        engine.closeEngine();
    }

    private static class TextChangeRunnable implements Runnable {
        private final TextView textView;
        private final String text;

        public TextChangeRunnable(TextView textView, String text) {
            this.textView = textView;
            this.text = text;
        }

        @Override
        public void run() {
            if(textView != null) {
                Markwon.setMarkdown(textView, text);
            }
        }
    }
}