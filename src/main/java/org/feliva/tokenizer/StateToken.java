package org.feliva.tokenizer;

//https://html.spec.whatwg.org/multipage/parsing.html#data-state


import org.jsoup.parser.CharacterReader;

public enum StateToken {


    Data {
        void read(Tokeniser t, CaracterReader r) {
            switch (r.current()) {
                case '\u0000':
//                    t.error(this);
//                    t.emit(r.consume());
                    break;
                case '&':
                    t.advanceTransition(CharacterReferenceInData);
                    break;
                case '<':
                    t.advanceTransition(TagOpen);
                    break;
                case '\uffff':
//                    t.emit(new Token.EOF());
                    break;
                default:
                    String data = r.consumeData();
                    t.emitData(data);
            }

        }
    },
    TagOpen {
        void read(Tokeniser t, CaracterReader r) {
            switch (r.current()) {
                case '!':
                    t.advanceTransition(MarkupDeclarationOpen);
                    break;
                case '/':
                    t.advanceTransition(EndTagOpen);
                    break;
                case '?':
//                    t.createBogusCommentPending();
                    t.transition(BogusComment);
                    break;
                default:
                    if (r.matchesAsciiAlpha()) {
                        t.createCurrentTag(true);
                        t.transition(TagName);
                    } else {
//                        t.error(this);
//                        t.emit('<');
                        t.transition(Data);
                    }
            }

        }
    },
    EndTagOpen {
        void read(Tokeniser t, CaracterReader r) {

            if (r.matchesAsciiAlpha()) {
                t.createCurrentTag(false);
                t.transition(TagName);
            } else if (r.matches('>')) {
//                t.error(this);
                t.advanceTransition(Data);
            } else {
//                t.error(this);
//                t.createBogusCommentPending();
//                t.commentPending.append('/');
                t.transition(BogusComment);
            }

        }
    },
    MarkupDeclarationOpen {
        void read(Tokeniser t, CaracterReader r) {
            if (r.matchConsume("--")) {
//                t.createCommentPending();
                t.transition(CommentStart);
            } else if (r.matchConsumeIgnoreCase("DOCTYPE")) {
                t.transition(Doctype);
            } else if (r.matchConsume("[CDATA[")) {
//                t.createTempBuffer();
                t.transition(CdataSection);
            } else {
//                t.error(this);
//                t.createBogusCommentPending();
                t.transition(BogusComment);
            }

        }
    },
    CommentStart {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.commentPending.append('�');
                    t.transition(Comment);
                    break;
                case '-':
                    t.transition(CommentStartDash);
                    break;
                case '>':
//                    t.error(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
                    t.transition(Comment);
            }

        }
    },
    CommentStartDash {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.commentPending.append('�');
                    t.transition(Comment);
                    break;
                case '-':
                    t.transition(CommentEnd);
                    break;
                case '>':
//                    t.error(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
//                    t.commentPending.append(c);
                    t.transition(Comment);
            }

        }
    },
    Comment {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.current();
            switch (c) {
                case '\u0000':
//                    t.error(this);
                    r.advance();
//                    t.commentPending.append('�');
                    break;
                case '-':
                    t.advanceTransition(CommentEndDash);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
//                    t.commentPending.append(r.consumeToAny(new char[]{'-', '\u0000'}));
            }

        }
    },
    CommentEnd {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.commentPending.append("--").append('�');
                    t.transition(Comment);
                    break;
                case '!':
                    t.transition(CommentEndBang);
                    break;
                case '-':
//                    t.commentPending.append('-');
                    break;
                case '>':
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
//                    t.commentPending.append("--").append(c);
                    t.transition(Comment);
            }

        }
    },
    CommentEndBang {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.commentPending.append("--!").append('�');
                    t.transition(Comment);
                    break;
                case '-':
//                    t.commentPending.append("--!");
                    t.transition(CommentEndDash);
                    break;
                case '>':
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
//                    t.commentPending.append("--!").append(c);
                    t.transition(Comment);
            }

        }
    },
    CommentEndDash {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.commentPending.append('-').append('�');
                    t.transition(Comment);
                    break;
                case '-':
                    t.transition(CommentEnd);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
//                    t.commentPending.append('-').append(c);
                    t.transition(Comment);
            }

        }
    },
    CharacterReferenceInData {
        void read(Tokeniser t, CaracterReader r) {
//            TokeniserState.readCharRef(t, Data);
        }
    },
    BogusComment {
        void read(Tokeniser t, CaracterReader r) {
//            t.commentPending.append(r.consumeTo('>'));
            char next = r.current();
            if (next == '>' || next == '\uffff') {
                r.consume();
//                t.emitCommentPending();
                t.transition(Data);
            }

        }
    },
    TagName {
        void read(Tokeniser t, CaracterReader r) {
            String tagName = r.consumeTagName();
            t.current.setName(tagName);
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.tagPending.appendTagName(TokeniserState.replacementStr);
                    break;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '>':
                    t.emitCurrentTag();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.appendTagName(c);
            }

        }
    },
    SelfClosingStartTag {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '>':
//                    t.tagPending.selfClosing = true;
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
//                    t.error(this);
                    t.transition(BeforeAttributeName);
            }

        }
    },
    BeforeAttributeName {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '@'://defs function
                    t.createDefsAttribute();
                    t.transition(DefsAttributeName);
                    break;
                case '\u0000':
                    r.unconsume();
//                    t.error(this);
//                    t.tagPending.newAttribute();
                    t.transition(AttributeName);
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    break;
                case '"':
                case '\'':
                case '=':
//                    t.error(this);
//                    t.tagPending.newAttribute();
//                    t.tagPending.appendAttributeName(c, r.pos() - 1, r.pos());
                    t.transition(AttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '>':
                    t.emitCurrentTag();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
            }

        }
    },
    DefsAttributeName{
        void read(Tokeniser t, CaracterReader r) {
            String name = r.defsConsumeAtributeName();
            t.defsAttribute.nome = name;
            switch (r.consume()){
                case '(':
                    t.transition(DefsAttributeValue);
                    break;
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue('�', r.pos() - 1, r.pos());
                    t.transition(AttributeValue_unquoted);
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    break;
                case '>':
//                    t.error(this);
//                    t.emitTagPending();
                    t.emitDefsAttribute();
                    t.emitCurrentTag();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
            }

        }
    },
    DefsAttributeValue {
        void read(Tokeniser t, CaracterReader r) {
            String value = r.defsConsumeAtributeValue();
            t.defsAttribute.value = value;
            switch (r.consume()){
                case ')':
                    t.emitDefsAttribute();
                    t.transition(BeforeAttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '>':
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;

            }
        }
    },
    AttributeName {
        void read(Tokeniser t, CaracterReader r) {
            int pos = r.consume(); //r.pos();
            String name = r.consumeToAnySorted(attributeNameCharsSorted);
//            t.tagPending.appendAttributeName(name, pos, r.pos());
//            pos = r.pos();
            char c = r.consume();
            switch (c) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(AfterAttributeName);
                    break;
                case '"':
                case '\'':
                case '<':
//                    t.error(this);
//                    t.tagPending.appendAttributeName(c, pos, r.pos());
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '=':
                    t.transition(BeforeAttributeValue);
                    break;
                case '>':
                    t.emitCurrentTag();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.appendAttributeName(c, pos, r.pos());
            }

        }
    },
    AfterAttributeName {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeName('�', r.pos() - 1, r.pos());
                    t.transition(AttributeName);
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    break;
                case '"':
                case '\'':
                case '<':
//                    t.error(this);
//                    t.tagPending.newAttribute();
//                    t.tagPending.appendAttributeName(c, r.pos() - 1, r.pos());
                    t.transition(AttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '=':
                    t.transition(BeforeAttributeValue);
                    break;
                case '>':
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
            }

        }
    },
    BeforeAttributeValue {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue('�', r.pos() - 1, r.pos());
                    t.transition(AttributeValue_unquoted);
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    break;
                case '"':
                    t.transition(AttributeValue_doubleQuoted);
                    break;
                case '&':
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
                    break;
                case '\'':
                    t.transition(AttributeValue_singleQuoted);
                    break;
                case '<':
                case '=':
                case '`':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue(c, r.pos() - 1, r.pos());
                    t.transition(AttributeValue_unquoted);
                    break;
                case '>':
//                    t.error(this);
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
            }

        }
    },
    AttributeValue_doubleQuoted {
        void read(Tokeniser t, CaracterReader r) {
            int pos = r.consume();//r.pos();
            String value = r.consumeAttributeQuoted(false);
//            if (value.length() > 0) {
//                t.tagPending.appendAttributeValue(value, pos, r.pos());
//            } else {
//                t.tagPending.setEmptyAttributeValue();
//            }

            pos = r.consume();//r.pos();
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue('�', pos, r.pos());
                    break;
                case '"':
                    t.transition(AfterAttributeValue_quoted);
                    break;
                case '&':
//                    int[] ref = t.consumeCharacterReference('"', true);
//                    if (ref != null) {
//                        t.tagPending.appendAttributeValue(ref, pos, r.pos());
//                    } else {
//                        t.tagPending.appendAttributeValue('&', pos, r.pos());
//                    }
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.appendAttributeValue(c, pos, r.pos());
            }

        }
    },
    AttributeValue_singleQuoted {
        void read(Tokeniser t, CaracterReader r) {
            int pos = r.consume();//r.pos();
            String value = r.consumeAttributeQuoted(true);
//            if (value.length() > 0) {
//                t.tagPending.appendAttributeValue(value, pos, r.pos());
//            } else {
//                t.tagPending.setEmptyAttributeValue();
//            }
//
//            pos = r.pos();
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue('�', pos, r.pos());
                    break;
                case '&':
//                    int[] ref = t.consumeCharacterReference('\'', true);
//                    if (ref != null) {
//                        t.tagPending.appendAttributeValue(ref, pos, r.pos());
//                    } else {
//                        t.tagPending.appendAttributeValue('&', pos, r.pos());
//                    }
                    break;
                case '\'':
                    t.transition(AfterAttributeValue_quoted);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.appendAttributeValue(c, pos, r.pos());
            }

        }
    },
    AttributeValue_unquoted {
        void read(Tokeniser t, CaracterReader r) {
            int pos = r.consume();//r.pos();
//            String value = r.consumeToAnySorted(attributeValueUnquoted);
//            if (value.length() > 0) {
//                t.tagPending.appendAttributeValue(value, pos, r.pos());
//            }
//
//            pos = r.pos();
            char c = r.consume();
            switch (c) {
                case '\u0000':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue('�', pos, r.pos());
                    break;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    break;
                case '"':
                case '\'':
                case '<':
                case '=':
                case '`':
//                    t.error(this);
//                    t.tagPending.appendAttributeValue(c, pos, r.pos());
                    break;
                case '&':
//                    int[] ref = t.consumeCharacterReference('>', true);
//                    if (ref != null) {
//                        t.tagPending.appendAttributeValue(ref, pos, r.pos());
//                    } else {
//                        t.tagPending.appendAttributeValue('&', pos, r.pos());
//                    }
                    break;
                case '>':
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
//                    t.tagPending.appendAttributeValue(c, pos, r.pos());
            }

        }
    },
    AfterAttributeValue_quoted {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '>':
//                    t.emitTagPending();
                    t.transition(Data);
                    break;
                case '\uffff':
//                    t.eofError(this);
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
//                    t.error(this);
                    t.transition(BeforeAttributeName);
            }

        }
    },
    Doctype {
        void read(Tokeniser t, CaracterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeDoctypeName);
                    break;
                case '\uffff':
//                    t.eofError(this);
                case '>':
//                    t.error(this);
//                    t.createDoctypePending();
//                    t.doctypePending.forceQuirks = true;
//                    t.emitDoctypePending();
                    t.transition(Data);
                    break;
                default:
//                    t.error(this);
                    t.transition(BeforeDoctypeName);
            }

        }
    },
    BeforeDoctypeName {
        void read(Tokeniser t, CaracterReader r) {
            if (r.matchesAsciiAlpha()) {
//                t.createDoctypePending();
                t.transition(DoctypeName);
            } else {
                char c = r.consume();
                switch (c) {
                    case '\u0000':
//                        t.error(this);
//                        t.createDoctypePending();
//                        t.doctypePending.name.append('�');
                        t.transition(DoctypeName);
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        break;
                    case '\uffff':
//                        t.eofError(this);
//                        t.createDoctypePending();
//                        t.doctypePending.forceQuirks = true;
//                        t.emitDoctypePending();
                        t.transition(Data);
                        break;
                    default:
//                        t.createDoctypePending();
//                        t.doctypePending.name.append(c);
                        t.transition(DoctypeName);
                }

            }
        }
    },
    DoctypeName {
        void read(Tokeniser t, CaracterReader r) {
            if (r.matchesLetter()) {
//                String name = r.consumeLetterSequence();
//                t.doctypePending.name.append(name);
            } else {
                char c = r.consume();
                switch (c) {
                    case '\u0000':
//                        t.error(this);
//                        t.doctypePending.name.append('�');
                        break;
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
//                        t.transition(AfterDoctypeName);
                        break;
                    case '>':
//                        t.emitDoctypePending();
                        t.transition(Data);
                        break;
                    case '\uffff':
//                        t.eofError(this);
//                        t.doctypePending.forceQuirks = true;
//                        t.emitDoctypePending();
                        t.transition(Data);
                        break;
                    default:
//                        t.doctypePending.name.append(c);
                }

            }
        }
    },
    CdataSection {
        void read(Tokeniser t, CaracterReader r) {
            String data = r.consumeTo("]]>");
//            t.dataBuffer.append(data);
            if (r.matchConsume("]]>") || r.isEmpty()) {
//                t.emit(new Token.CData(t.dataBuffer.toString()));
                t.transition(Data);
            }

        }
    };

    static final char nullChar = '\u0000';
    static final char[] attributeNameCharsSorted = new char[]{'\t', '\n', '\f', '\r', ' ', '"', '\'', '/', '<', '=', '>'};
    static final char[] attributeValueUnquoted = new char[]{'\u0000', '\t', '\n', '\f', '\r', ' ', '"', '&', '\'', '<', '=', '>', '`'};
    private static final char replacementChar = '�';
    private static final String replacementStr = String.valueOf('�');
    private static final char eof = '\uffff';

    StateToken(){}

    abstract void read(Tokeniser var1, CaracterReader var2);
}
