package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Word {
    public static Set<String> stopWords = new HashSet<>();

    private final String prefix;
    private final String coreText;
    private final String suffix;
    private final boolean isValid;

    private Word(String prefix, String coreText, String suffix, boolean isValid) {
        this.prefix = prefix;
        this.coreText = coreText;
        this.suffix = suffix;
        this.isValid = isValid;
    }

    private static boolean containsValidText(String text) {
        boolean hasLetter = false;
        for (char currentChar : text.toCharArray()) {
            if (Character.isLetter(currentChar)) {
                hasLetter = true;
            } else if (Character.isDigit(currentChar)) {
                return false;
            }
        }
        return hasLetter;
    }

    public static boolean loadStopWords(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                stopWords.add(scanner.next().toLowerCase());
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static Word createWord(String input) {
        if (isInvalidInput(input)) {
            return new Word("", input, "", false);
        }

        StringBuilder prefixBuilder = new StringBuilder();
        StringBuilder suffixBuilder = new StringBuilder();
        String coreText = extractCoreText(input, prefixBuilder, suffixBuilder);

        if (isValidCoreText(coreText)) {
            coreText = handlePossessiveOrContraction(coreText, suffixBuilder);
            return new Word(prefixBuilder.toString(), coreText, suffixBuilder.toString(), true);
        }

        return new Word("", input, "", false);
    }

    private static boolean isInvalidInput(String input) {
        return input == null || input.isEmpty() || input.contains(" ");
    }

    private static String extractCoreText(String input, StringBuilder prefixBuilder, StringBuilder suffixBuilder) {
        int index = extractPrefix(input, prefixBuilder);
        String coreText = input.substring(index);
        index = extractSuffix(coreText, suffixBuilder);
        return coreText.substring(0, index + 1);
    }

    private static int extractPrefix(String input, StringBuilder prefixBuilder) {
        int index = 0;
        while (index < input.length() && !Character.isLetterOrDigit(input.charAt(index))) {
            prefixBuilder.append(input.charAt(index));
            index++;
        }
        return index;
    }

    private static int extractSuffix(String coreText, StringBuilder suffixBuilder) {
        int index = coreText.length() - 1;
        while (index >= 0 && !Character.isLetterOrDigit(coreText.charAt(index))) {
            suffixBuilder.insert(0, coreText.charAt(index));
            index--;
        }
        return index;
    }

    private static boolean isValidCoreText(String coreText) {
        return coreText.length() > 0 && containsValidText(coreText);
    }

    private static String handlePossessiveOrContraction(String coreText, StringBuilder suffixBuilder) {
        if (coreText.endsWith("'d") || coreText.endsWith("'s")) {
            suffixBuilder.insert(0, coreText.substring(coreText.length() - 2));
            return coreText.substring(0, coreText.length() - 2);
        }
        return coreText;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Word)) return false;

        Word word = (Word) object;
        return this.coreText.equalsIgnoreCase(word.coreText);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getText() {
        return coreText;
    }

    public boolean isKeyword() {
        return isValid && !stopWords.contains(coreText.toLowerCase());
    }

    @Override
    public String toString() {
        return prefix + coreText + suffix;
    }
}
