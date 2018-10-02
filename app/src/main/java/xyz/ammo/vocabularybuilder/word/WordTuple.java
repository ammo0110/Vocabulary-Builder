package xyz.ammo.vocabularybuilder.word;

import android.os.Parcel;
import android.os.Parcelable;

public class WordTuple implements Parcelable {

    private String word;
    private String type;
    private String meaning;
    private String synonyms;
    private String example;

    public WordTuple(String word, String type, String meaning, String synonyms, String example) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
        this.synonyms = synonyms;
        this.example = example;
    }

    private WordTuple(Parcel in) {
        word = in.readString();
        type = in.readString();
        meaning = in.readString();
        synonyms = in.readString();
        example = in.readString();
    }

    public String getWord() {
        return word;
    }

    public String getType() {
        return type;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public String getExample() {
        return example;
    }

    @Override
    public String toString() {
        return word + ", " + type + ", " + meaning + ", " + synonyms + ", "+example;
    }

    /* Parcelable Implementation */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(type);
        dest.writeString(meaning);
        dest.writeString(synonyms);
        dest.writeString(example);
    }

    public static final Parcelable.Creator<WordTuple> CREATOR = new Parcelable.Creator<WordTuple>() {

        @Override
        public WordTuple createFromParcel(Parcel in) {
            return new WordTuple(in);
        }

        @Override
        public WordTuple[] newArray(int size) {
            return new WordTuple[size];
        }
    };
}