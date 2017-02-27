package org.query.expansion;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.query.expansion.models.TermData;
import org.query.expansion.values.PhotoFields;

public class QueryUtil {
    private String queryString;
    private String[] originalQueryTerms;

    private static final String SPLIT_CHARACTER = " ";

    public QueryUtil(String queryString) {
        this.queryString = queryString;
    }

    public Query getMultiPhraseQueryFromQueryString(String queryString) {
        originalQueryTerms = queryString.split(SPLIT_CHARACTER);
        Term[] terms = new Term[originalQueryTerms.length];

        for (int i = 0; i < originalQueryTerms.length; i++) {
            terms[i] = new Term(PhotoFields.TAGS, originalQueryTerms[i]);
        }

        return new MultiPhraseQuery
                .Builder()
                .add(terms)
                .build();
    }

    public void addQueryExpandedTerms(TermData[] scoredTerms) {

    }
}
