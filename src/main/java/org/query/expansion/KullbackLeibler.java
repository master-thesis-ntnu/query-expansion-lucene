package org.query.expansion;

public class KullbackLeibler {
    public static double calculateKullbackLeiblerDistance(
            int numberOfTimesInTopkDocuments, int totalNumberOfTermsInTopKDocuments,
            int numberOfTimesInCollection, int totalNumberOfTermsInCollection) {

        double prel = numberOfTimesInTopkDocuments  / totalNumberOfTermsInTopKDocuments;
        double pcol = numberOfTimesInCollection     / totalNumberOfTermsInCollection;

        return prel * Math.log(prel / pcol);
    }
}
