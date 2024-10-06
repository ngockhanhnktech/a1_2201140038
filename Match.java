package engine;

public class Match implements Comparable<Match> {
    private final Doc doc;
    private final Word word;
    private final int frequency;
    private final int firstOccurrenceIndex;

    public Match(Doc doc, Word word, int frequency, int firstOccurrenceIndex) {
        this.doc = doc;
        this.word = word;
        this.frequency = frequency;
        this.firstOccurrenceIndex = firstOccurrenceIndex;
    }

    public int getFreq() {
        return frequency;
    }

    public int getFirstIndex() {
        return firstOccurrenceIndex;
    }

    @Override
    public int compareTo(Match other) {
        return compareFirstOccurrenceIndex(other);
    }

    public Word getWord() {
        return word;
    }

    private int compareFirstOccurrenceIndex(Match other) {
        return Integer.compare(this.firstOccurrenceIndex, other.firstOccurrenceIndex);
    }
}
