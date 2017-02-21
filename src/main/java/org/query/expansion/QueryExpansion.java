package org.query.expansion;

import org.apache.lucene.search.TopDocs;
import org.query.expansion.models.TermData;

import java.util.HashMap;

public class QueryExpansion {
    private TopDocs topKDocuments;
    private HashMap<String, TermData> terms;
    private int numberOfTermsInTopKDocuments;
    private int numberOfTermsInCollection;

    public QueryExpansion(TopDocs topKDocuments) {
        this.topKDocuments = topKDocuments;
    }

    public HashMap getTopKTermCounts() {
        return null;
    }
}
