package org.feliva.tokenizer;

public class Tokeniser {

    StateToken state = StateToken.Data;
    CaracterReader reader;

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
}
