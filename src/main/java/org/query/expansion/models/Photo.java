package org.query.expansion.models;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.query.expansion.values.PhotoFields;

import java.util.ArrayList;

public class Photo {
    private String title;
    private String url;
    private ArrayList<String> tags;

    public Photo() {

    }

    public Photo(String title, String url, ArrayList<String> tags) {
        this.title = title;
        this.url = url;
        this.tags = tags;
    }

    public Photo(Document document) {
        title = document.get(PhotoFields.TITLE);
        url = document.get(PhotoFields.URL);
        tags = new ArrayList<String>();
    }

    public Document getLuceneDocument() {
        Document document = new Document();

        Field urlField = new StringField(PhotoFields.URL, url, Field.Store.YES);
        document.add(urlField);

        Field titleField = new TextField(PhotoFields.TITLE, title, Field.Store.YES);
        document.add(titleField);

        Field tagsField = new StringField(PhotoFields.TAGS, tags.toString(), Field.Store.YES);
        document.add(tagsField);

        return document;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
