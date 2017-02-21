package org.query.expansion;

import org.query.expansion.models.Photo;
import org.query.expansion.models.Tag;
import org.query.expansion.models.TermData;

import java.io.IOException;
import java.util.HashMap;

public class QueryExpansion {
    private Photo[] photos;
    private HashMap<String, TermData> terms;
    private int numberOfTermsInTopKDocuments;
    private int numberOfTermsInCollection = 10;
    private Searcher searcher;

    public QueryExpansion(Photo[] photos, Searcher searcher) {
        this.photos = photos;
        this.searcher = searcher;

        terms = new HashMap<String, TermData>();
    }

    public String[] getQueryExpandedTerms() {
        try {
            generateTermDataFromPhotosArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Terms in hasmap: " + terms.size());
        System.out.println("Number of terms in top-k documents: " + numberOfTermsInTopKDocuments);
        return null;
    }

    public void generateTermDataFromPhotosArray() throws IOException {

        for (Photo photo : photos) {
            for (Tag tag : photo.getTags()) {
                String term = tag.getContent();

                if (terms.containsKey(term)) {
                    terms.get(term).incrementNumberOfTimesInTopKDocuments();
                } else {
                    int numberOfTimesInCollection = searcher.getNumberOfTimesInCollection(term);
                    TermData termData = new TermData(term);
                    termData.setNumberOfTimesInCollection(numberOfTimesInCollection);

                    terms.put(term, termData);
                }
            }

            numberOfTermsInTopKDocuments += photo.getTags().size();
        }
    }
}
