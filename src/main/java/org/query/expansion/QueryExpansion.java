package org.query.expansion;

import org.apache.lucene.search.TopDocs;

import java.util.HashMap;

public class QueryExpansion {
    private TopDocs topKDocuments;

    public QueryExpansion(TopDocs topKDocuments) {
        this.topKDocuments = topKDocuments;
    }

    public HashMap getTopKTermCounts() {
        return null;
    }
}
