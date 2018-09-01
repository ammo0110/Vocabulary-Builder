package xyz.ammo.vocabularybuilder.word;

public class WordTuple {

    private String word;
    private String type;
    private String shortMeaning;
    private String synonyms;

    public WordTuple(String word, String type, String shortMeaning, String synonyms) {
        this.word = word;
        this.type = type;
        this.shortMeaning = shortMeaning;
        this.synonyms = synonyms;
    }

    public String getWord() {
        return word;
    }

    public String getType() {
        return type;
    }

    public String getShortMeaning() {
        return shortMeaning;
    }

    public String getSynonyms() {
        return synonyms;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.word);
        buf.append(", ");
        buf.append(this.type);
        buf.append(", ");
        buf.append(this.shortMeaning);
        buf.append(", ");
        buf.append(this.synonyms);

        return buf.toString();
    }
}
