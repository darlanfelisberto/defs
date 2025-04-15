package org.feliva.tokenizer;

public class DefsAttribute {
    private String nome;
    private String value;
    private Tag tagParent;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public Tag getTagParent() {
        return tagParent;
    }
    public void setTagParent(Tag tagParent) {
        this.tagParent = tagParent;
    }
}
