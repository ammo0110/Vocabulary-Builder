package xyz.ammo.vocabularybuilder.word;

public class WordTuple {

    private String word;
    private String type;
    private String shortMeaning;
    private String synonyms;
    private String example;

    public WordTuple(String word, String type, String shortMeaning, String synonyms, String example) {
        this.word = word;
        this.type = type;
        this.shortMeaning = shortMeaning;
        this.synonyms = synonyms;
        this.example = example;
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

    public String getExample() {
        return example;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("#");
        buf.append(this.word);
        buf.append("\n");
        buf.append("_");
        buf.append(this.type);
        buf.append("_\n\n##Meaning");
        buf.append(this.shortMeaning);
        buf.append("\n\n##Synonyms");
        buf.append(this.synonyms);
        buf.append("\n\n##Example");
        buf.append(this.example);

        return buf.toString();
    }
}
