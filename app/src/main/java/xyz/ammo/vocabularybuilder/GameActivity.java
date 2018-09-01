package xyz.ammo.vocabularybuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.word.SQLiteWordEngine;
import xyz.ammo.vocabularybuilder.word.WordEngine;
import xyz.ammo.vocabularybuilder.word.WordTuple;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.word) TextView wordTv;
    @BindView(R.id.meaning) TextView meaningTv;

    private WordEngine engine;
    private Runnable meaningChangeRunnable;
    private static final int MILLIS_DELAY_IN_SHOWING_MEANING = 1000;
    private static final String TAG = "MyGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d(TAG, "onCreate invoked");

        ButterKnife.bind(this);

        String localPath = getFilesDir() + "/" + "PrimaryWords.db";
        engine = new SQLiteWordEngine(getApplicationContext(), localPath);

        // Show the first word on the screen
        WordTuple tuple = engine.getNext();
        wordTv.setText(tuple.getWord());
        meaningTv.setText("");  //Empty, if user waits then show else move on

        meaningChangeRunnable = new TextChangeRunnable(meaningTv, tuple.getShortMeaning());
        meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
    }

    @OnClick(R.id.definition) void nextWord() {
        meaningTv.removeCallbacks(meaningChangeRunnable);

        WordTuple tuple = engine.getNext();
        wordTv.setText(tuple.getWord());
        meaningTv.setText("");  //Empty, if user waits then show else move on

        meaningChangeRunnable = new TextChangeRunnable(meaningTv, tuple.getShortMeaning());
        meaningTv.postDelayed(meaningChangeRunnable, MILLIS_DELAY_IN_SHOWING_MEANING);
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
                textView.setText(text);
            }
        }
    }
}
