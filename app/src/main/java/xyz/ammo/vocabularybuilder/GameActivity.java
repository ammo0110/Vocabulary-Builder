package xyz.ammo.vocabularybuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.utils.Intents;
import xyz.ammo.vocabularybuilder.word.SQLiteWordEngine;
import xyz.ammo.vocabularybuilder.word.WordEngine;
import xyz.ammo.vocabularybuilder.word.WordTuple;

public class GameActivity extends BaseActivity {

    @BindView(R.id.word) TextView wordTv;
    @BindView(R.id.type) TextView typeTv;
    @BindView(R.id.meaning) TextView meaningTv;

    private WordEngine engine;
    private Runnable meaningChangeRunnable;
    private int mInterval;
    private static final String TAG = "MyGameActivity";

    private WordTuple wordTuple;
    public static final String CURRENT_WORD = "CurrentWord";
    private boolean randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = MyApplication.getDefaultSharedPreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "onCreate invoked");

        ButterKnife.bind(this);

        mInterval = prefs.getInt(MyApplication.TIME_INTERVAL, 1000);

        engine = new SQLiteWordEngine();
        // If database is empty, exit game
        if(engine.getSize() == 0) {
            Toast.makeText(this, "Database is empty, please add a word first", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Show the first word on the screen
            wordTuple = randomize ? engine.getRandom() : engine.getNext();
            Log.d(TAG, "" + wordTuple);
            wordTv.setText(wordTuple.getWord());
            typeTv.setText(wordTuple.getType());
            meaningTv.setText("");  //Empty, if user waits then show else move on

            meaningChangeRunnable = new TextChangeRunnable(meaningTv, wordTuple.getMeaning());
            meaningTv.postDelayed(meaningChangeRunnable, mInterval);
        }
    }

    @OnClick(R.id.meaning) void nextWord() {
        meaningTv.removeCallbacks(meaningChangeRunnable);

        wordTuple = randomize ? engine.getRandom() : engine.getNext();
        Log.d(TAG, "" + wordTuple);
        wordTv.setText(wordTuple.getWord());
        typeTv.setText(wordTuple.getType());
        meaningTv.setText("");  //Empty, if user waits then show else move on

        meaningChangeRunnable = new TextChangeRunnable(meaningTv, wordTuple.getMeaning());
        meaningTv.postDelayed(meaningChangeRunnable, mInterval);
    }

    @OnClick(R.id.fab) void viewData() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if(prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        WordInfoDialogFragment widf = WordInfoDialogFragment.newInstance(wordTuple);
        widf.show(ft, "dialog");
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
                String searchQuery = wordTv.getText().toString();
                searchQuery.replace(' ', '+');
                Uri uri = Uri.parse("https://www.google.com/#q=define:" + searchQuery);
                Intents.maybeStartActivity(this, new Intent(Intent.ACTION_VIEW, uri));
                return true;
            case R.id.menu_game_shuffle:
                randomize = !item.isChecked();
                item.setChecked(randomize);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy invoked");
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
                textView.setText(text);
            }
        }
    }
}
