package org.query.expansion;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;

public class TagFieldType extends FieldType{

    public TagFieldType() {
        setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        setStoreTermVectors(true);
        setTokenized(true);
        setStored(true);
        freeze();
    }
}
