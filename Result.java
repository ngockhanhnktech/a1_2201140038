package engine;

import java.util.List;

public class Result implements Comparable<Result> {
    private Doc doc;
    private List<Match> matches;

    public Result(Doc doc, List<Match> matches) {
        this.doc = doc;
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public int getTotalFrequency() {
        return matches.stream().mapToInt(Match::getFreq).sum();
    }

    public double getAverageFirstIndex() {
        return matches.stream().mapToInt(Match::getFirstIndex).average().orElse(0.0);
    }

    @Override
    public int compareTo(Result other) {
        int countComparison = Integer.compare(other.matches.size(), this.matches.size());
        if (countComparison != 0) return countComparison;

        int frequencyComparison = Integer.compare(other.getTotalFrequency(), this.getTotalFrequency());
        return frequencyComparison != 0 ? frequencyComparison : Double.compare(this.getAverageFirstIndex(), other.getAverageFirstIndex());
    }

    public Doc getDoc() {
        return doc;
    }

    public String htmlHighlight() {
        StringBuilder htmlBuilder = new StringBuilder("<h3>");
        appendTitleHtmlHighlight(htmlBuilder, doc.getTitle());
        htmlBuilder.append("</h3><p>");
        appendBodyHtmlHighlight(htmlBuilder, doc.getBody());
        htmlBuilder.append("</p>");
        return htmlBuilder.toString().trim();
    }

    private void appendTitleHtmlHighlight(StringBuilder htmlBuilder, List<Word> titleWords) {
        for (int index = 0; index < titleWords.size(); index++) {
            Word word = titleWords.get(index);
            boolean isMatched = isWordMatched(word);
            appendWord(htmlBuilder, word, isMatched, "<u>");
            if (index < titleWords.size() - 1) {
                htmlBuilder.append(" ");
            }
        }
    }

    private void appendBodyHtmlHighlight(StringBuilder htmlBuilder, List<Word> bodyWords) {
        for (int index = 0; index < bodyWords.size(); index++) {
            Word word = bodyWords.get(index);
            boolean isMatched = isWordMatched(word);
            appendWord(htmlBuilder, word, isMatched, "<b>");
            if (index < bodyWords.size() - 1) {
                htmlBuilder.append(" ");
            }
        }
    }

    private boolean isWordMatched(Word word) {
        return matches.stream()
                .anyMatch(match -> match.getWord().getText().equalsIgnoreCase(word.getText()));
    }

    private void appendWord(StringBuilder htmlBuilder, Word word, boolean isMatched, String tag) {
        appendPrefix(htmlBuilder, word);
        if (isMatched) {
            appendWordWithTag(htmlBuilder, word, tag);
        } else {
            htmlBuilder.append(word.getText());
        }
        appendSuffix(htmlBuilder, word);
    }

    private void appendPrefix(StringBuilder htmlBuilder, Word word) {
        if (!word.getPrefix().isEmpty()) {
            htmlBuilder.append(word.getPrefix());
        }
    }

    private void appendSuffix(StringBuilder htmlBuilder, Word word) {
        if (!word.getSuffix().isEmpty()) {
            htmlBuilder.append(word.getSuffix());
        }
    }

    private void appendWordWithTag(StringBuilder htmlBuilder, Word word, String tag) {
        htmlBuilder.append(tag).append(word.getText()).append("</").append(tag.substring(1, tag.length() - 1)).append(">");
    }
}
