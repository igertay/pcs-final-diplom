import java.io.File;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

public class BooleanSearchEngine implements SearchEngine {

    protected Map<String, List<PageEntry>> searchResult = new HashMap<>();

    public BooleanSearchEngine(File pdfs) throws IOException {

        File[] fileList = pdfs.listFiles();
        for (File pdf : fileList) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (var word : freqs.keySet()) {
                    var pageEntry = new PageEntry(pdf.getName(), i, freqs.get(word));
                    if (searchResult.containsKey(word)) {
                        List<PageEntry> pageEntryList = new ArrayList<>(searchResult.get(word));
                        pageEntryList.add(pageEntry);
                        searchResult.put(word, pageEntryList);
                        Collections.sort(pageEntryList);

                    } else {
                        searchResult.put(word, Arrays.asList(pageEntry));
                    }
                }
            }

        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (searchResult.containsKey(word)) {
            List<PageEntry> pageEntryList = searchResult.get(word);
            return pageEntryList;
        } else {
            return Collections.emptyList();
        }
    }

    static String answerJson(List<PageEntry> pageEntryList) {
        var builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        List<String> jsonList = new ArrayList<>();
        if (pageEntryList.isEmpty()) {
            return "Слово не найдено.";
        }
        for (PageEntry pageEntry : pageEntryList) {
            jsonList.add(gson.toJson(pageEntry));
        }
        return jsonList.toString();
    }
}