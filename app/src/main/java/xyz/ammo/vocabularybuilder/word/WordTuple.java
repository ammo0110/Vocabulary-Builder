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
        return buf.append(this.word)
                .append(", ")
                .append(this.type)
                .append(", ")
                .append(this.shortMeaning)
                .append(", ")
                .append(this.synonyms)
                .append(", ")
                .append(this.example)
                .toString();
    }

    public String markdownify() {
        StringBuilder buf = new StringBuilder();
        return buf.append("## ")
                .append(this.word)
                .append("\n### Synonyms\n")
                .append(this.synonyms)
                .append("\n\n### Example\n")
                .append("1. ")
                .append(this.example)
                .toString();
    }
}
