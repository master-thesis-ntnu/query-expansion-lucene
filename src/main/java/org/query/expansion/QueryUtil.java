package org.query.expansion;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.query.expansion.models.TermData;
import org.query.expansion.values.PhotoFields;

public class QueryUtil {
    private String queryString;
    private String[] originalQueryTerms;
    private TermData[] scoredTerms;

    private static final String SPLIT_CHARACTER = " ";
    private static final int MAX_NUMBER_OF_SEARCH_TERMS = 5;

    public QueryUtil(String queryString) {
        this.queryString = queryString;
        originalQueryTerms = queryString.split(SPLIT_CHARACTER);
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

    public void setScoredTerms(TermData[] scoredTerms) {
        this.scoredTerms = scoredTerms;
    }

    /**
     * This method has to be called after setScoredTerms.
     * @return MultiPhraseQuery
     */
    public MultiPhraseQuery getExpandedMultiPhraseQuery() {
        if (scoredTerms == null) {
            throw new IllegalArgumentException("Scored terms is not set");
        }

        int sizeOfNewQueryTerms = getSizeOfNewQueryTerms();
        Term[] expandedQueryTerms = new Term[sizeOfNewQueryTerms];

        int newQueryTermsIndex = 0;

        for (int i = 0; i < expandedQueryTerms.length; i++) {
            if (i < originalQueryTerms.length) {
                expandedQueryTerms[i] = new Term(PhotoFields.TAGS, originalQueryTerms[i]);
            } else {
                for (int j = newQueryTermsIndex; j < scoredTerms.length; j++) {
                    String term = scoredTerms[j].getTerm();

                    if (!termExistsInOriginalQuery(term)) {
                        expandedQueryTerms[i] = new Term(PhotoFields.TAGS, term);
                        newQueryTermsIndex++;

                        break;
                    }

                    newQueryTermsIndex++;
                }
            }
        }

        return new MultiPhraseQuery
                .Builder()
                .add(expandedQueryTerms)
                .build();
    }

    private boolean termExistsInOriginalQuery(String term) {
        return ArrayUtils.contains(originalQueryTerms, term);
    }

    private int getSizeOfNewQueryTerms() {
        int combinedTermSize = originalQueryTerms.length + scoredTerms.length;

        return combinedTermSize > MAX_NUMBER_OF_SEARCH_TERMS? MAX_NUMBER_OF_SEARCH_TERMS : combinedTermSize;
    }

    private String[] splitStringOnCharacter(String string, String splitCharacter) {
        return string.split(splitCharacter);
    }
}
