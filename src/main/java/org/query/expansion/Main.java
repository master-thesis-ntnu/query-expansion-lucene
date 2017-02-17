package org.query.expansion;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Directory index = new RAMDirectory();
        Directory taxonamyIndex = new RAMDirectory();
        Indexer indexer = new Indexer(index, taxonamyIndex);
        Searcher searcher = new Searcher(index);

        try {
            indexer.indexDocumentsFromFile("/home/jonas/git/query-expansion/data/flickr-parsed.data");

            String queryString = "square";
            TopDocs topDocuments = searcher.search(queryString);
            searcher.printSearchResults(topDocuments);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } /*catch (ParseException parseException) {
            parseException.printStackTrace();
        }*/
    }

    private static void search(Directory index) throws IOException, ParseException {
        String queryString = "sky";
        Searcher searcher = new Searcher(index);
        TopDocs topDocuments = searcher.search(queryString);
        searcher.printSearchResults(topDocuments);
    }
}
