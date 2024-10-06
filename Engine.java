package engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Engine {
    private final List<Doc> documents = new ArrayList<>();

    public int loadDocs(String directoryPath) {
        File directory = new File(directoryPath);
        File[] textFiles = getTextFiles(directory);
        if (textFiles == null) return 0;

        for (File file : textFiles) {
            loadDocument(file);
        }
        return documents.size();
    }

    private File[] getTextFiles(File directory) {
        return directory.listFiles((dir, name) -> name.endsWith(".txt"));
    }

    private void loadDocument(File file) {
        try {
            String content = Files.readString(file.toPath());
            documents.add(new Doc(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Doc[] getDocuments() {
        return documents.toArray(Doc[]::new);
    }

    public List<Result> search(Query query) {
        List<Result> results = new ArrayList<>();

        for (Doc document : documents) {
            List<Match> matches = query.matchAgainst(document);
            addResultIfMatchesFound(results, document, matches);
        }

        results.sort(null);
        return results;
    }

    private void addResultIfMatchesFound(List<Result> results, Doc document, List<Match> matches) {
        if (!matches.isEmpty()) {
            results.add(new Result(document, matches));
        }
    }

    public String htmlResult(List<Result> results) {
        StringBuilder htmlBuilder = new StringBuilder();
        results.forEach(result -> htmlBuilder.append(result.htmlHighlight()));
        return htmlBuilder.toString().trim();
    }
}
