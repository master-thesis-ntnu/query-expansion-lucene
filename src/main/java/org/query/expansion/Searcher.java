package org.query.expansion;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.query.expansion.models.Photo;
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

    public Photo[] search(String queryString) throws IOException {
        return search(queryString, DEFAULT_NUMBER_OF_HITS);
    }

    public Photo[] search(String queryString, int numberOfResults) throws IOException {
        Query query = getQueryFromQueryString(queryString);
        TopDocs topDocuments = indexSearcher.search(query, numberOfResults);

        return getPhotosFromTopDocs(topDocuments);
    }

    private Query getQueryFromQueryString(String queryString) {
        Term term = new Term(PhotoFields.TAGS, queryString);

        return new TermQuery(term);
    }

    public int getNumberOfTimesInCollection(String field, String term) throws IOException {
        return (int) indexReader.totalTermFreq(new Term(field, term));
    }

    private Photo[] getPhotosFromTopDocs(TopDocs topDocuments) throws IOException {
        ScoreDoc[] scoreDocuments = topDocuments.scoreDocs;
        Photo[] photos = new Photo[topDocuments.scoreDocs.length];

        for (int i = 0; i < scoreDocuments.length; i++) {
            int documentId = scoreDocuments[i].doc;
            Document document = indexSearcher.doc(documentId);
            photos[i] = new Photo(document);
        }

        return photos;
    }

    public void printSearchResults(Photo[] photos) {
        try {
            openIndexReaderAndSearcher();
            Printer.printResult(photos);
            closeIndexReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openIndexReaderAndSearcher() throws IOException {
        indexReader = DirectoryReader.open(index);
        indexSearcher = new IndexSearcher(indexReader);
    }

    public void closeIndexReader() throws IOException {
        indexReader.close();
    }
}
