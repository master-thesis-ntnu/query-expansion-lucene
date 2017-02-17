package org.query.expansion;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.query.expansion.values.PhotoFields;

import java.io.IOException;

public class Searcher {
    private Directory index;
    private IndexReader indexReader;
    private IndexSearcher indexSearcher;

    private static final int DEFAULT_NUMBER_OF_HITS = 10;

    public Searcher(Directory index) {
        this.index = index;
    }

    public TopDocs search(String queryString) throws IOException {
        return search(queryString, DEFAULT_NUMBER_OF_HITS);
    }

    public TopDocs search(String queryString, int numberOfResults) throws IOException {
        Query query = getQueryFromQueryString(queryString);

        openIndexReaderAndSearcher();
        TopDocs topDocuments = indexSearcher.search(query, numberOfResults);
        indexReader.close();

        return topDocuments;
    }

    private Query getQueryFromQueryString(String queryString) {
        Term term = new Term(PhotoFields.TAGS, queryString);

        return new TermQuery(term);
    }

    public void printSearchResults(TopDocs topDocuments) {
        try {
            openIndexReaderAndSearcher();
            Printer.printResult(topDocuments, indexSearcher);
            closeIndexReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openIndexReaderAndSearcher() throws IOException {
        indexReader = DirectoryReader.open(index);
        indexSearcher = new IndexSearcher(indexReader);
    }

    private void closeIndexReader() throws IOException {
        indexReader.close();
    }
}
