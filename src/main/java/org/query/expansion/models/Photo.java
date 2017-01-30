package org.query.expansion.models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.query.expansion.values.PhotoFields;

import java.util.ArrayList;

public class Photo {
    private String url;
    private ArrayList<String> tags;

    public Document getLuceneDocument() {
        Document document = new Document();

        Field urlField = new StringField(PhotoFields.URL, "http://www.vg.no", Field.Store.YES);
        document.add(urlField);

        Field titleField = new StringField(PhotoFields.TITLE, "This is a title", Field.Store.YES);
        document.add(titleField);

        Field tagsField = new StringField(PhotoFields.TAGS, "The tags should be added here", Field.Store.YES);
        document.add(tagsField);

        return document;
    }
}
