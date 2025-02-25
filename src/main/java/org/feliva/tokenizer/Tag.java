package org.feliva.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag {
    protected String name;
    protected String data;
    protected Map<String, String> attributes = new HashMap<String, String>();
    protected List<DefsAttribute> defsAttributes = new ArrayList<>();
    protected List<Tag> children = new ArrayList<Tag>();
    protected Tag parent;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public void addChildre(Tag tag){
        tag.parent = this;
        this.children.add(tag);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase();
    }

//    public void addAttribute(Attribute defsAttribute) {
//        this.defsAttributes.add(defsAttribute);
//    }

    public void addDefsAttribute(DefsAttribute defsAttribute) {
        this.defsAttributes.add(defsAttribute);
    }

    public List<Tag> getChildren() {
        return children;
    }

    public void setChildren(List<Tag> children) {
        this.children = children;
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }
}
