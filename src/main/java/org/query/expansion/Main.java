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
            indexer.indexDocumentsFromFile("/home/jonas/git/query-expansion/data/flickr-parsed.data");
            //search(index);
            queryExpansionSearch(index);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void queryExpansionSearch(Directory index) throws IOException {
        String queryString = "square";
        Searcher searcher = new Searcher(index);
        TopDocs topDocuments = searcher.search(queryString);
    }

    private static void search(Directory index) throws IOException, ParseException {
        String queryString = "square";
        Searcher searcher = new Searcher(index);
        TopDocs topDocuments = searcher.search(queryString);
        searcher.printSearchResults(topDocuments);
    }
}
