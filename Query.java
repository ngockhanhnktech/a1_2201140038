package engine;

import java.util.ArrayList;
import java.util.List;

public class Query {
    private final List<Word> searchKeywords;

    public Query(String searchPhrase) {
        searchKeywords = extractKeywords(searchPhrase);
    }

    public List<Word> getKeywords() {
        return new ArrayList<>(searchKeywords);
    }

    private List<Word> extractKeywords(String phrase) {
        List<Word> keywords = new ArrayList<>();
        String[] rawTerms = phrase.split("\\s+");

        for (String rawTerm : rawTerms) {
            Word word = Word.createWord(rawTerm);
            if (word.isKeyword()) {
                keywords.add(word);
            }
        }

        return keywords;
    }

    public List<Match> matchAgainst(Doc document) {
        List<Match> matches = new ArrayList<>();
        List<Word> combinedWords = getCombinedWords(document);

        for (Word keyword : searchKeywords) {
            Match match = findMatchInDocument(keyword, combinedWords, document);
            if (match != null) {
                matches.add(match);
            }
        }

        matches.sort(null);
        return matches;
    }

    private List<Word> getCombinedWords(Doc document) {
        List<Word> combinedWords = new ArrayList<>();
        combinedWords.addAll(document.getTitle());
        combinedWords.addAll(document.getBody());
        return combinedWords;
    }

    private Match findMatchInDocument(Word keyword, List<Word> combinedWords, Doc document) {
        int occurrenceCount = 0;
        int firstOccurrenceIndex = -1;

        for (int index = 0; index < combinedWords.size(); index++) {
            if (combinedWords.get(index).equals(keyword)) {
                occurrenceCount++;
                if (firstOccurrenceIndex == -1) {
                    firstOccurrenceIndex = index;
                }
            }
        }

        return (occurrenceCount > 0) ? new Match(document, keyword, occurrenceCount, firstOccurrenceIndex) : null;
    }
}
