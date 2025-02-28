package org.feliva.tokenizer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Document {

    List<Tag> tagList = new LinkedList<Tag>();
    Set<Tag> indexDef =  new HashSet<Tag>();

    public void addTag(Tag tag){
        this.tagList.add(tag);
    }
    public void addIndexDef(Tag tag){
        this.indexDef.add(tag);
    }

    public Set<Tag> getIndexDef() {
        return indexDef;
    }
}
