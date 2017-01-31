package org.query.expansion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.query.expansion.models.Photo;
import org.query.expansion.values.PhotoFields;

import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        Directory index = new RAMDirectory();

        try {
            createIndex(index);
            search(index);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    private static void createIndex(Directory index) throws IOException {
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

    private static void search(Directory index) throws IOException, ParseException {
        String queryString = "sky";
        Term term = new Term(PhotoFields.TAGS, queryString);
        Query query = new TermQuery(term);
        int hitsPerPage = 10;

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs topDocuments  = searcher.search(query, hitsPerPage);

        Printer.printResult(topDocuments, searcher);

        reader.close();
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
