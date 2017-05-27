package org.query.expansion;

import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.query.expansion.models.Photo;
import org.query.expansion.util.ElapsedTime;
import org.query.expansion.util.Statistics;
import org.query.expansion.values.SearchTypes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final int NUMBER_OF_TEST_RUNS = 10;
    private static final int NUMBER_OF_CACHE_WARMING_RUNS = 10;
    private static Path path = Paths.get("/home/jonas/git/query-expansion/data/lucene.index");
    private static String CACHE_WARMING_QUERY = "square insta";
    private static String TEST_QUERY = "blue sky";
    private static int[] resultSizes = {
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200
    };

    public static void main(String[] args) {
        // createIndex();
        runQueryExpansionTests();
        // runSingleSearch();
    }

    private static void runSingleSearch() {
        String queryString = "blue sky";

        try {
            Directory index = FSDirectory.open(path);
            Searcher searcher = new Searcher(index);
            searcher.openIndexReaderAndSearcher();
            Photo[] photos = searcher.search(queryString);
            searcher.closeIndexReader();
            searcher.printSearchResults(photos);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void runQueryExpansionTests() {
        Directory index;
        Searcher searcher;

        for (int resultSize : resultSizes) {
            try {
                // Open new connections to make sure cache is clean
                index = FSDirectory.open(path);
                searcher = new Searcher(index);
                searcher.openIndexReaderAndSearcher();

                // Run baseline tests
                warmCache(searcher, CACHE_WARMING_QUERY, NUMBER_OF_CACHE_WARMING_RUNS, SearchTypes.MULTI_TERM_SEARCH);
                System.out.println("Running baseline tests..");
                runSearchTests(searcher, TEST_QUERY, resultSize);
                System.out.println("Done");

                // Close everything to make sure cache is gone before next run
                searcher.closeIndexReader();
                index.close();

                // Open new connections to make sure cache is clean
                index = FSDirectory.open(path);
                searcher = new Searcher(index);
                searcher.openIndexReaderAndSearcher();

                // Run query expansion tests
                warmCache(searcher, CACHE_WARMING_QUERY, NUMBER_OF_CACHE_WARMING_RUNS, SearchTypes.QUERY_EXPANSION_SEARCH);
                System.out.println("Running query expansion tests..");
                runQueryExpansionSearchTests(searcher, TEST_QUERY, resultSize);
                System.out.println("Done");

                // Close everything to make sure cache is gone before next run
                searcher.closeIndexReader();
                index.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private static void warmCache(Searcher searcher, String query, int numberOfWarmingRuns, int searchType) throws IOException {
        System.out.println("Warming cache...");
        for (int i = 0; i < numberOfWarmingRuns; i++) {
            switch (searchType) {
                case SearchTypes.MULTI_TERM_SEARCH:
                    searcher.search(query);
                    break;
                case SearchTypes.QUERY_EXPANSION_SEARCH:
                    queryExpansionSearch(searcher, query, 10);
                    break;
            }
        }
        System.out.println("Done");
    }

    private static void createIndex() {
        Directory index = null;

        try {
            index = FSDirectory.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Indexer indexer = new Indexer(index);

        try {
            indexer.indexDocumentsFromFile("/home/jonas/git/query-expansion/data/flickr-data-copy.data");
            //search(index, queryString);
            //queryExpansionSearch(index, queryString);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void runQueryExpansionSearchTests(Searcher searcher, String queryString, int numberOfResults) throws IOException {
        long sumOfDeltaTimes = 0;
        ElapsedTime elapsedTime = new ElapsedTime();

        for (int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            elapsedTime.start();
            queryExpansionSearch(searcher, queryString, numberOfResults);
            elapsedTime.stop();

            sumOfDeltaTimes += elapsedTime.getElapsedTimeInNanoSeconds();
        }

        double averageSearchTime = Statistics.getAverageSearchTime(sumOfDeltaTimes, NUMBER_OF_TEST_RUNS);

        System.out.println("Query expansion tests");
        System.out.println("Number of test runs: " + NUMBER_OF_TEST_RUNS);
        System.out.println("Average query expansion search time: " + averageSearchTime + " milliseconds");
    }

    private static void runSearchTests(Searcher searcher, String queryString, int numberOfResults) throws IOException {
        long sumOfDeltaTimes = 0;
        ElapsedTime elapsedTime = new ElapsedTime();

        for (int i = 0; i < NUMBER_OF_TEST_RUNS; i++) {
            elapsedTime.start();
            searcher.search(queryString, numberOfResults);
            elapsedTime.stop();

            sumOfDeltaTimes += elapsedTime.getElapsedTimeInNanoSeconds();
        }

        double averageSearchTime = Statistics.getAverageSearchTime(sumOfDeltaTimes, NUMBER_OF_TEST_RUNS);

        System.out.println("Baseline tests");
        System.out.println("Number of test runs: " + NUMBER_OF_TEST_RUNS);
        System.out.println("Average search time: " + averageSearchTime + " milliseconds");
    }

    private static Photo[] queryExpansionSearch(Searcher searcher, String queryString, int queryExpansionResultSize) throws IOException {
        Photo[] initialPhotoResult = searcher.search(queryString, queryExpansionResultSize);

        QueryExpansion queryExpansion = new QueryExpansion(initialPhotoResult, queryString, searcher);
        MultiPhraseQuery multiPhraseQuery = queryExpansion.getQueryExpandedMultiPhraseQuery();

        return searcher.search(multiPhraseQuery, queryExpansionResultSize);
    }
}
