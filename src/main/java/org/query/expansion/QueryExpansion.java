package org.query.expansion;

import org.apache.lucene.search.MultiPhraseQuery;
import org.query.expansion.models.Photo;
import org.query.expansion.models.TermData;
import org.query.expansion.values.PhotoFields;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class QueryExpansion {
    private Photo[] photos;
    private Searcher searcher;
    private String originalQuery;
    private QueryUtil queryUtil;

    private HashMap<String, TermData> terms;
    private int totalNumberOfTermsInTopKDocuments;
    private int totalNumberOfTermsInCollection;

    public QueryExpansion(Photo[] firstPhotoResult, String originalQuery, Searcher searcher) {
        this.photos = firstPhotoResult;
        this.searcher = searcher;
        this.originalQuery = originalQuery;

        queryUtil = new QueryUtil(originalQuery);
        terms = new HashMap<String, TermData>();
    }

    public MultiPhraseQuery getQueryExpandedMultiPhraseQuery() {
        try {
            generateTermDataFromPhotosArray();
            totalNumberOfTermsInCollection = searcher.getTotalNumberOfTermsInCollection(PhotoFields.TAGS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TermData[] calculatedKlScores = new TermData[terms.size()];

        int index = 0;
        for (TermData termData : terms.values()) {
            termData.calculateKlScore(totalNumberOfTermsInTopKDocuments, totalNumberOfTermsInCollection);
            calculatedKlScores[index] = termData;

            index++;
        }

        /*for (TermData termData : calculatedKlScores) {
            System.out.println(termData.getTerm() + ": " + termData.getKlScore());
        }

        System.out.println("Terms in hashmap: " + terms.size());
        System.out.println("Number of photos in the result: " + photos.length);
        System.out.println("Number of terms in top-k documents: " + totalNumberOfTermsInTopKDocuments);*/

        Arrays.sort(calculatedKlScores);
        queryUtil.setScoredTerms(calculatedKlScores);

        return queryUtil.getExpandedMultiPhraseQuery();
    }

    private void generateTermDataFromPhotosArray() throws IOException {

        for (Photo photo : photos) {
            for (String tag : photo.getTags()) {
                String term = tag;

                if (terms.containsKey(term)) {
                    terms.get(term).incrementNumberOfTimesInTopKDocuments();
                } else {
                    int numberOfTimesInCollection = searcher.getTotalNumberOfTimesInCollection(PhotoFields.TAGS, term);
                    TermData termData = new TermData(term);
                    termData.setNumberOfTimesInCollection(numberOfTimesInCollection);

                    terms.put(term, termData);
                }
            }

            totalNumberOfTermsInTopKDocuments += photo.getTags().size();
        }
    }
}
