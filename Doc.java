package engine;

import java.util.ArrayList;
import java.util.List;

public class Doc {
    private final List<Word> titleWords;
    private final List<Word> bodyWords;

    public Doc(String content) {
        String[] lines = content.split("\n");
        this.titleWords = extractTitleWords(lines);
        this.bodyWords = extractBodyWords(lines);
    }

    private List<Word> extractTitleWords(String[] lines) {
        return lines.length > 0 ? extractWords(lines[0]) : new ArrayList<>();
    }

    private List<Word> extractBodyWords(String[] lines) {
        return lines.length > 1 ? extractWords(lines[1]) : new ArrayList<>();
    }

    private List<Word> extractWords(String line) {
        if (isLineEmpty(line)) {
            return new ArrayList<>();
        }

        List<Word> words = new ArrayList<>();
        for (String rawWord : line.split("\\s+")) {
            Word word = Word.createWord(rawWord);
            addWordIfValid(words, word);
        }
        return words;
    }

    private boolean isLineEmpty(String line) {
        return line == null || line.trim().isEmpty();
    }

    private void addWordIfValid(List<Word> words, Word word) {
        if (!word.getText().isEmpty()) {
            words.add(word);
        }
    }

    public List<Word> getTitle() {
        return titleWords;
    }

    public List<Word> getBody() {
        return bodyWords;
    }
}
