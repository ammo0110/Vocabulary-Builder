package xyz.ammo.vocabularybuilder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import xyz.ammo.vocabularybuilder.word.SQLiteWordEngine;
import xyz.ammo.vocabularybuilder.word.WordTuple;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.word) TextView wordTv;
    @BindView(R.id.meaning) TextView meaningTv;

    private SQLiteWordEngine engine;
    private Runnable meaningChangeRunnable;
    private static final int MILLIS_DELAY_IN_SHOWING_MEANING = 1000;

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
    protected void onPause() {
        super.onPause();
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
