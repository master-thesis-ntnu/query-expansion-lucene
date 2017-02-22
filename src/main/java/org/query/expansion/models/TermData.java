package org.query.expansion.models;

import org.query.expansion.KullbackLeibler;
import org.query.expansion.singelton.TermValues;

public class TermData implements Comparable<TermData> {
    private String term;
    private int numberOfTimesInTopKDocuments = 1;
    private int numberOfTimesInCollection;
    private double klScore;

    public TermData(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public int getNumberOfTimesInTopKDocuments() {
        return numberOfTimesInTopKDocuments;
    }

    public void incrementNumberOfTimesInTopKDocuments() {
        numberOfTimesInTopKDocuments++;
    }

    public int getNumberOfTimesInCollection() {
        return numberOfTimesInCollection;
    }

    public void setNumberOfTimesInCollection(int numberOfTimesInCollection) {
        this.numberOfTimesInCollection = numberOfTimesInCollection;
    }

    public void calculateKlScore() {
        klScore = KullbackLeibler.calculateKullbackLeiblerDistance(
                numberOfTimesInTopKDocuments,
                TermValues.totalNumberOfTermsInTopKDocuments,
                numberOfTimesInCollection,
                TermValues.totalNumberOfTermsInTopKDocuments
        );
    }

    public double getKlScore() {
        return klScore;
    }

    @Override
    public String toString() {
        return "TermData{" +
                "term='" + term + '\'' +
                ", numberOfTimesInTopKDocuments=" + numberOfTimesInTopKDocuments +
                ", getNumberOfTimesInCollection=" + numberOfTimesInCollection +
                '}';
    }

    public int compareTo(TermData o) {
        return (int) (this.getKlScore() - o.getKlScore());
    }
}
