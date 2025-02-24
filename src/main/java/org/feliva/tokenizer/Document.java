package org.feliva.tokenizer;

import java.util.LinkedList;
import java.util.List;

public class Document {

    List<Tag> tagList = new LinkedList<Tag>();

    public void addTag(Tag tag){
        this.tagList.add(tag);
    }
}
