package org.query.expansion.models;

public class TermData {
    private String term;
    private int numberOfTimesInTopKDocuments;
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

    @Override
    public String toString() {
        return "TermData{" +
                "term='" + term + '\'' +
                ", numberOfTimesInTopKDocuments=" + numberOfTimesInTopKDocuments +
                ", getNumberOfTimesInCollection=" + numberOfTimesInCollection +
                '}';
    }
}
