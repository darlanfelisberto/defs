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
                    t.emit(data);
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
                        t.createTagPending(true);
                        t.transition(TagName);
                    } else {
                        t.error(this);
                        t.emit('<');
                        t.transition(Data);
                    }
            }

        }
    },
    EndTagOpen {
        void read(org.jsoup.parser.Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.emit("</");
                t.transition(Data);
            } else if (r.matchesAsciiAlpha()) {
                t.createTagPending(false);
                t.transition(TagName);
            } else if (r.matches('>')) {
                t.error(this);
                t.advanceTransition(Data);
            } else {
                t.error(this);
                t.createBogusCommentPending();
                t.commentPending.append('/');
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
                t.transition();
            } else if (r.matchConsume("[CDATA[")) {
                t.createTempBuffer();
                t.transition(CdataSection);
            } else {
                t.error(this);
                t.createBogusCommentPending();
                t.transition(BogusComment);
            }

        }
    },
    CommentStart {
        void read(org.jsoup.parser.Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error(this);
                    t.commentPending.append('�');
                    t.transition(Comment);
                    break;
                case '-':
                    t.transition(CommentStartDash);
                    break;
                case '>':
                    t.error(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                case '\uffff':
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    break;
                default:
                    r.unconsume();
                    t.transition(Comment);
            }

        }
    },
    CharacterReferenceInData {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.readCharRef(t, Data);
        }
    },
    BogusComment {
        void read(org.jsoup.parser.Tokeniser t, CharacterReader r) {
            t.commentPending.append(r.consumeTo('>'));
            char next = r.current();
            if (next == '>' || next == '\uffff') {
                r.consume();
                t.emitCommentPending();
                t.transition(Data);
            }

        }
    },
}
