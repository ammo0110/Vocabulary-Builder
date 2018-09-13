package xyz.ammo.vocabularybuilder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import ru.noties.markwon.Markwon;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.renderer.SpannableRenderer;

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

    private WordTuple wordTuple;
    private boolean randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "onCreate invoked");

        ButterKnife.bind(this);

        engine = new SQLiteWordEngine();
        this.randomize = false;

        // If database is empty, exit game
        if(engine.getSize() == 0) {
            Toast.makeText(this, "Database is empty, please add a word first", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // Show the first word on the screen
            wordTuple = engine.getNext(randomize);
            Log.d(TAG, "" + wordTuple);
            wordTv.setText(wordTuple.getWord());
            typeTv.setText(wordTuple.getType());
            meaningTv.setText("");  //Empty, if user waits then show else move on

            meaningChangeRunnable = new TextChangeRunnable(meaningTv, wordTuple.getShortMeaning());
            meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
        }
    }

    @OnClick(R.id.meaning) void nextWord() {
        meaningTv.removeCallbacks(meaningChangeRunnable);

        wordTuple = engine.getNext(randomize);
        Log.d(TAG, "" + wordTuple);
        wordTv.setText(wordTuple.getWord());
        typeTv.setText(wordTuple.getType());
        meaningTv.setText("");  //Empty, if user waits then show else move on

        meaningChangeRunnable = new TextChangeRunnable(meaningTv, wordTuple.getShortMeaning());
        meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
    }

    @OnClick(R.id.fab) void viewData() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        final Parser parser = Markwon.createParser();
        final SpannableConfiguration config = SpannableConfiguration.create(this);
        final SpannableRenderer renderer = new SpannableRenderer();
        final Node node = parser.parse(wordTuple.markdownify());
        builder.setTitle(wordTuple.getWord())
                .setMessage(renderer.render(config, node))
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .show();
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
