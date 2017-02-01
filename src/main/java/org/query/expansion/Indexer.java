package org.query.expansion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.query.expansion.models.Photo;

import java.io.IOException;
import java.util.ArrayList;

public class Indexer {
    private Directory index;

    public Indexer(Directory index) {
        this.index = index;
    }

    public void indexDocumentsFromFile(String filePath) throws IOException {
        System.out.println("Creating index");

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(index, config);

        System.out.println("Indexing documents");
        long startTime = System.currentTimeMillis();

        indexDocument(writer);

        long endTime = System.currentTimeMillis();
        long numberOfDocuments = writer.numDocs();
        long elapsedTime = endTime - startTime;

        writer.close();

        System.out.println("Indexing documents took " + elapsedTime + " ms");
        System.out.println("Number of documents: " + numberOfDocuments);
    }

    private static void indexDocument(IndexWriter writer) throws IOException {
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("sky");
        tags.add("blue");
        tags.add("clouds");

        Photo photo = new Photo("This is a title", "http://www.vg.no", tags);

        writer.addDocument(photo.getLuceneDocument());
    }
}
