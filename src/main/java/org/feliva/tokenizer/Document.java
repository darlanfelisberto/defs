package org.feliva.tokenizer;

import java.util.LinkedList;
import java.util.List;

public class Document {

    List<Tag> tagList = new LinkedList<Tag>();
    List<Tag> indexDef =  new LinkedList<Tag>();

    public void addTag(Tag tag){
        this.tagList.add(tag);
    }
    public void addIndexDef(Tag tag){
        this.indexDef.add(tag);
    }
}
