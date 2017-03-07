package org.query.expansion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.query.expansion.models.Photo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Indexer {
    private Directory index;
    private IndexWriter indexWriter;

    public Indexer(Directory index) {
        this.index = index;
    }

    public void indexDocumentsFromFile(String filePath) throws IOException {
        System.out.println("Creating index");

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(index, config);

        System.out.println("Indexing documents");
        long startTime = System.currentTimeMillis();

        indexPhotosFromFile(filePath);
        //indexDocument(writer);

        long endTime = System.currentTimeMillis();
        long numberOfDocuments = indexWriter.numDocs();
        long elapsedTime = endTime - startTime;

        indexWriter.commit();
        indexWriter.close();

        System.out.println("Indexing " + numberOfDocuments + " documents took " + elapsedTime + " ms");
    }

    private void indexPhotosFromFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        String line;
        int counter = 0;
        while ((line = bufferedReader.readLine()) != null) {
            indexDocument(Photo.fromJson(line));

            if (counter == 10000) {
                break;
            }

            counter++;
        }

        bufferedReader.close();
    }

    private void indexDocument(Photo photo) throws IOException {
        indexWriter.addDocument(photo.getLuceneDocument());
    }
}
