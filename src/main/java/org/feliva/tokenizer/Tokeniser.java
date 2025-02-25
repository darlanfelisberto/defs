package org.feliva.tokenizer;

import java.util.Stack;

public class Tokeniser {

    StateToken state = StateToken.Data;
    CaracterReader reader;

    Document document = new Document();

    DefsAttribute defsAttribute;
    Tag current;
    boolean isOpen = true;

    Stack<Tag> stack = new Stack<>();


    public Tokeniser(CaracterReader reader) {
        this.reader = reader;
    }

    void advanceTransition(StateToken newState) {
        this.transition(newState);
        this.reader.advance();
    }

    void transition(StateToken newState) {
//        if (newState == TokeniserState.TagOpen) {
//            this.markupStartPos = this.reader.pos();
//        }

        this.state = newState;
    }

    void emit(String str) {
//        if (this.charsString == null) {
//            this.charsString = str;
//        } else {
//            if (this.charsBuilder.length() == 0) {
//                this.charsBuilder.append(this.charsString);
//            }
//
//            this.charsBuilder.append(str);
//        }
//
//        this.charPending.startPos(this.charStartPos);
//        this.charPending.endPos(this.reader.pos());
    }

    public void addNewTag(String name){

        Tag tag = new Tag(name);

        if(this.stack.isEmpty()){
           //é um elemento root do document
           this.document.tagList.add(tag);
        }else{
            tag.setParent(this.stack.peek());
            this.stack.peek().addChildre(tag);
        }
//        this.current = tag;
        stack.push(tag);
    }

    public void closeTag(){
        this.stack.pop();
    }

    public void addNewData(String data){
        Tag tag = new Tag("");
        tag.setData(data);
        stack.peek().addChildre(tag);
    }

    public void emitCurrentTag(){

        if(!this.isOpen){//close tag
            this.resetCurrent();
            this.stack.pop();
            return;
        }

        if(this.stack.isEmpty()){
            //é um elemento root do document
            this.document.tagList.add(this.current);
        }else{
            this.current.setParent(this.stack.peek());
            this.stack.peek().addChildre(this.current);
        }
        stack.push(this.current);
        this.resetCurrent();
    }

    public void emitData(String data){
        Tag tag = new Tag();
        tag.setData(data);

        if(this.stack.isEmpty()){
            //é um elemento root do document
            this.document.tagList.add(tag);
//            this.current.addChildre(tag);
        }else{
            tag.setParent(this.stack.peek());
            this.stack.peek().addChildre(tag);
        }
    }

    public void createDefsBlock(){
        this.current = new DefsTag();
        this.isOpen = true;
    }

    public void emitDefsAttribute(){
        this.current.addDefsAttribute(this.defsAttribute);
        this.defsAttribute = null;
    }

    public void createDefsAttribute(){
        this.defsAttribute = new DefsAttribute();
    }

    public void resetCurrent(){
        this.current = null;
        this.isOpen = false;
    }


    public void createCurrentTag(boolean open){
        this.current = new Tag();
        this.isOpen = open;
    }
}
