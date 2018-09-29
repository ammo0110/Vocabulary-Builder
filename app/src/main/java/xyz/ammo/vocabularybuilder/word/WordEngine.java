package xyz.ammo.vocabularybuilder.word;

public interface WordEngine {

    int getSize();

    WordTuple getNext();

    WordTuple getRandom();

}
