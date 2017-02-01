package org.query.expansion;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Directory index = new RAMDirectory();
        Indexer indexer = new Indexer(index);

        try {
            indexer.indexDocumentsFromFile("");
            search(index);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    private static void search(Directory index) throws IOException, ParseException {
        String queryString = "sky";
        Searcher searcher = new Searcher(index);
        TopDocs topDocuments = searcher.search(queryString);
        searcher.printSearchResults(topDocuments);
    }
}
