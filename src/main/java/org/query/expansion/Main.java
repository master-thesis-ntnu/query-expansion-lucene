package org.query.expansion;

import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.query.expansion.models.Photo;
import org.query.expansion.util.ElapsedTime;

import java.io.IOException;

public class Main {
    private static final int NUMBER_OF_TEST_RUNS = 1000;

    public static void main(String[] args) {
        Directory index = new RAMDirectory();
        Indexer indexer = new Indexer(index);
        String queryString = "square inkwell";

        try {
            indexer.indexDocumentsFromFile("/home/jonas/git/query-expansion/data/flickr-parsed.data");
            //search(index, queryString);
            //queryExpansionSearch(index, queryString);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            runSearchTests(index, queryString);
            runQueryExpansionSearchTests(index, queryString);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void runQueryExpansionSearchTests(Directory index, String queryString) throws IOException {
        long sumOfDeltaTimes = 0;
        ElapsedTime elapsedTime = new ElapsedTime();

        for (int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            elapsedTime.start();
            queryExpansionSearch(index, queryString);
            elapsedTime.stop();

            sumOfDeltaTimes += elapsedTime.getElapsedTimeInMicroSeconds();
        }
        long averageDeltaTime = sumOfDeltaTimes / NUMBER_OF_TEST_RUNS;

        System.out.println("Number of test runs: " + NUMBER_OF_TEST_RUNS);
        System.out.println("Average query expansion search time: " + averageDeltaTime + " microseconds");
    }

    private static void runSearchTests(Directory index, String queryString) throws IOException {
        long sumOfDeltaTimes = 0;
        ElapsedTime elapsedTime = new ElapsedTime();

        for (int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            elapsedTime.start();
            search(index, queryString);
            elapsedTime.stop();

            sumOfDeltaTimes += elapsedTime.getElapsedTimeInMicroSeconds();
        }
        long averageDeltaTime = sumOfDeltaTimes / NUMBER_OF_TEST_RUNS;

        System.out.println("Number of test runs: " + NUMBER_OF_TEST_RUNS);
        System.out.println("Average search time: " + averageDeltaTime + " microseconds");
    }

    private static Photo[] queryExpansionSearch(Directory index, String queryString) throws IOException {
        Searcher searcher = new Searcher(index);

        searcher.openIndexReaderAndSearcher();
        Photo[] initialPhotoResult = searcher.search(queryString);

        QueryExpansion queryExpansion = new QueryExpansion(initialPhotoResult, queryString, searcher);
        MultiPhraseQuery multiPhraseQuery = queryExpansion.getQueryExpandedMultiPhraseQuery();
        Photo[] queryExpandedPhotoResults = searcher.search(multiPhraseQuery);

        searcher.closeIndexReader();

        /* for (Photo photo : queryExpandedPhotoResults) {
            String tags = "";

            for (Tag tag : photo.getTags()) {
                tags += tag.getContent() + ", ";
            }

            System.out.println(tags);
        }*/

        return queryExpandedPhotoResults;
    }

    private static Photo[] search(Directory index, String queryString) throws IOException {
        Searcher searcher = new Searcher(index);
        searcher.openIndexReaderAndSearcher();
        Photo[] photos = searcher.search(queryString);
        searcher.closeIndexReader();

        // searcher.printSearchResults(photos);

        return photos;
    }
}
