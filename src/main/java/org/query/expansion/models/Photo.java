package org.query.expansion.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.lucene.document.*;
import org.query.expansion.TagField;
import org.query.expansion.values.PhotoFields;

import java.util.ArrayList;

public class Photo {
    private String id;
    @SerializedName("dateuploaded")
    private String dateUploaded;
    private String title;
    private String url;
    private ArrayList<Tag> tags;

    public Photo(Document document) {
        title = document.get(PhotoFields.TITLE);
        url = document.get(PhotoFields.URL);
        tags = new ArrayList<Tag>();

        for(String tag: document.getValues(PhotoFields.TAGS)) {
            tags.add(new Tag(tag));
        }
    }

    public Document getLuceneDocument() {
        Document document = new Document();

        Field idField = new StringField(PhotoFields.ID, id, Field.Store.YES);
        document.add(idField);

        Field dateUploadedField = new LongPoint(PhotoFields.DATE_UPLOADED, getDateUploadedAsLong());
        document.add(dateUploadedField);

        //Field urlField = new StringField(PhotoFields.URL, url, Field.Store.NO);
        //document.add(urlField);

        Field titleField = new TextField(PhotoFields.TITLE, title, Field.Store.YES);
        document.add(titleField);

        for (Tag tag : tags) {
            Field tagsField = new TagField(PhotoFields.TAGS, tag.getContent());
            document.add(tagsField);
        }

        return document;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    private long getDateUploadedAsLong() {
        return Long.parseLong(dateUploaded);
    }

    public static Photo fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Photo.class);
    }
}
