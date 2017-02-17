package org.query.expansion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
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
    private TaxonomyWriter taxonomyWriter;
    private Directory taxonomyIndex;

    public Indexer(Directory index) {
        this.index = index;
    }

    public Indexer(Directory index, Directory taxonomyIndex) {
        this.index = index;
        this.taxonomyIndex = taxonomyIndex;
    }

    public void indexDocumentsFromFile(String filePath) throws IOException {
        System.out.println("Creating index");

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(index, config);
        taxonomyWriter = new DirectoryTaxonomyWriter(taxonomyIndex);

        System.out.println("Indexing documents");
        long startTime = System.currentTimeMillis();

        indexPhotosFromFile(filePath);

        long endTime = System.currentTimeMillis();
        long numberOfDocuments = indexWriter.numDocs();
        long elapsedTime = endTime - startTime;

        taxonomyWriter.commit();
        taxonomyWriter.close();
        indexWriter.close();

        System.out.println("Indexing documents took " + elapsedTime + " ms");
        System.out.println("Number of documents: " + numberOfDocuments);
    }

    private void indexPhotosFromFile(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        String line;
        int counter = 0;
        while ((line = bufferedReader.readLine()) != null) {
            indexDocument(Photo.fromJson(line));

            if (counter == 100) {
                break;
            }

            counter++;
        }

        bufferedReader.close();
    }

    private void indexDocument(Photo photo) throws IOException {
        photo.writePhotoToIndex(indexWriter);
    }
}
