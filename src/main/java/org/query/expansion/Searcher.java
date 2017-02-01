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

    public Searcher(Directory index) {
        this.index = index;
    }
    public TopDocs search(String queryString) throws IOException {
        return search(queryString, 10);
    }

    public TopDocs search(String queryString, int numberOfResults) throws IOException {
        Term term = new Term(PhotoFields.TAGS, queryString);
        Query query = new TermQuery(term);
        int hitsPerPage = 10;

        indexReader = DirectoryReader.open(index);
        indexSearcher = new IndexSearcher(indexReader);

        return indexSearcher.search(query, hitsPerPage);
    }

    public void printSearchResults(TopDocs topDocuments) {
        try {
            Printer.printResult(topDocuments, indexSearcher);
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
