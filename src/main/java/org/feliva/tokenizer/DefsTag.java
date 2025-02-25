package org.feliva.tokenizer;

public class DefsTag extends Tag{

    private String expression;

    public DefsTag() {
    }

    public DefsTag(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
