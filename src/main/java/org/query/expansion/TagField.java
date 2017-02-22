package org.query.expansion;

import org.apache.lucene.document.Field;

public class TagField extends Field {

    public TagField(String name, String value) {
        super(name, value, new TagFieldType());
    }
}
