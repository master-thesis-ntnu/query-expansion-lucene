package org.query.expansion;

import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.query.expansion.models.Photo;
import org.query.expansion.models.Tag;

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
        String queryString = "square inkwell";
        Searcher searcher = new Searcher(index);

        searcher.openIndexReaderAndSearcher();
        Photo[] initialPhotoResult = searcher.search(queryString);

        QueryExpansion queryExpansion = new QueryExpansion(initialPhotoResult, queryString, searcher);
        MultiPhraseQuery multiPhraseQuery = queryExpansion.getQueryExpandedMultiPhraseQuery();
        Photo[] queryExpandedPhotoResults = searcher.search(multiPhraseQuery);

        searcher.closeIndexReader();

        for (Photo photo : queryExpandedPhotoResults) {
            String tags = "";

            for (Tag tag : photo.getTags()) {
                tags += tag.getContent() + ", ";
            }

            System.out.println(tags);
        }
    }

    private static void search(Directory index) throws IOException {
        String queryString = "square inkwell";
        Searcher searcher = new Searcher(index);
        Photo[] photos = searcher.search(queryString);
        searcher.printSearchResults(photos);
    }
}
