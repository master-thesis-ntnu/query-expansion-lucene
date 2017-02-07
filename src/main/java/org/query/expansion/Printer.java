package org.query.expansion;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.query.expansion.models.Photo;
import org.query.expansion.models.Tag;

import java.io.IOException;

public class Printer {
    public static void printResult(TopDocs topDocuments, IndexSearcher searcher) throws IOException {
        for (ScoreDoc scoreDocument : topDocuments.scoreDocs) {
            int documentId = scoreDocument.doc;
            Document document = searcher.doc(documentId);
            Photo photo = new Photo(document);

            prettyPrintPhotoData(photo);
        }

        System.out.println("Number of hits in the result: " + topDocuments.scoreDocs.length);
    }

    private static void prettyPrintPhotoData(Photo photo) {
        System.out.println("---------------");
        System.out.println(photo.getTitle());

        String tags = "Tags: ";
        for (Tag tag : photo.getTags()) {
            tags += tag + ", ";
        }
        System.out.println(tags);

        System.out.println("---------------");
    }
}
