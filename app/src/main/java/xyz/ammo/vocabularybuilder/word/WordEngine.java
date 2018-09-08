package xyz.ammo.vocabularybuilder.word;

public interface WordEngine {

    // Fetches next word for display, use randomize to implement random word fetching
    WordTuple getNext(boolean randomize);

}
