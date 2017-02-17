package org.query.expansion.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.index.IndexWriter;
import org.query.expansion.values.PhotoFields;

import java.io.IOException;
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

    public void writePhotoToIndex(IndexWriter indexWriter) throws IOException {
        Document document = new Document();
        FacetsConfig facetsConfig = getTagsFacetConfig();

        Field idField = new StringField(PhotoFields.ID, id, Field.Store.YES);
        document.add(idField);

        Field dateUploadedField = new LongPoint(PhotoFields.DATE_UPLOADED, getDateUploadedAsLong());
        document.add(dateUploadedField);

        //Field urlField = new StringField(PhotoFields.URL, url, Field.Store.NO);
        //document.add(urlField);

        Field titleField = new TextField(PhotoFields.TITLE, title, Field.Store.YES);
        document.add(titleField);

        String[] tagsStringArray = new String[tags.size()];
        for (int i = 0; i < tagsStringArray.length; i++) {
            // Field tagsField = new StringField(PhotoFields.TAGS, tag.getContent(), Field.Store.YES);
            //document.add(tagsField);
            tagsStringArray[i] = tags.get(i).getContent();
        }
        FacetField tagsFacetField = new FacetField(PhotoFields.TAGS, tagsStringArray);
        document.add(tagsFacetField);

        indexWriter.addDocument(facetsConfig.build(document));
    }

    private FacetsConfig getTagsFacetConfig() {
        FacetsConfig config = new FacetsConfig();
        config.setIndexFieldName(PhotoFields.TAGS, PhotoFields.TAGS_FACET);

        return config;
    }

    public String getId() {
        return id;
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
