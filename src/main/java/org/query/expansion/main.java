package org.query.expansion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class main {
    public static void main(String[] args) {
        try {
            createIndex();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static void createIndex() throws IOException {
        System.out.println("Creating index");

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new RAMDirectory();
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
        Document document = new Document();

        Field urlField = new StringField("url", "http://www.vg.no", Field.Store.YES);
        document.add(urlField);

        Field titleField = new StringField("title", "This is a title", Field.Store.YES);
        document.add(titleField);

        writer.addDocument(document);
    }
}
