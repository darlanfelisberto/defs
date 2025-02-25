package org.feliva.tokenizer;

import org.jsoup.UncheckedIOException;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CaracterReader {
    SeekableByteChannel byteChannel;
    InputStream stream;
    Reader reader;

    Map<String,String> cache = new HashMap<>();

    static final int BUFFER_SIZE = 8096;

    int bufLength = 0;
    boolean finish = false;

    int indexRead = 0;
    char[] charBuffer = new char[BUFFER_SIZE];

    CaracterReader(Path path) throws IOException {
        this.byteChannel = Files.newByteChannel(path);
        this.stream = Channels.newInputStream(byteChannel);
        this.reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        this.readToBuffer();
    }

    public void readToBuffer() throws IOException {

        while (bufLength < BUFFER_SIZE) {
            int read = reader.read(charBuffer,bufLength,BUFFER_SIZE-bufLength);
            if (read == -1) {
                this.finish = true;
                break;
            }
            bufLength += read;
        }
    }

    private void bufferUp() throws IOException {
        if (!this.finish && this.indexRead > BUFFER_SIZE) {
            this.readToBuffer();
            this.indexRead = 0;
        }
    }

    public char read() throws IOException {
        if(indexRead >= BUFFER_SIZE){
            this.readToBuffer();
        }
        return charBuffer[indexRead];
    }

    public char current(){
        return charBuffer[indexRead];
    }

    public char consome(){
        return charBuffer[indexRead++];
    }

    boolean matchConsume(String seq) {
//        this.bufferUp();
        if (this.matches(seq)) {
            this.indexRead += seq.length();
            return true;
        } else {
            return false;
        }
    }
    boolean matches(char c) {
        return !this.isEmpty() && this.charBuffer[this.indexRead] == c;
    }

    boolean matches(String seq) {
//        this.bufferUp();
        int scanLength = seq.length();
        if (scanLength > this.bufLength - this.indexRead) {
            return false;
        } else {
            for(int offset = 0; offset < scanLength; ++offset) {
                if (seq.charAt(offset) != this.charBuffer[this.indexRead + offset]) {
                    return false;
                }
            }

            return true;
        }
    }

    public void advance() {
        ++this.indexRead;
    }

    String consumeData() {
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;
        char[] val = this.charBuffer;

        label21:
        while(pos < remaining) {
            switch (val[pos]) {
                case '\u0000':
                case '&':
                case '<':
                case '#'://defs
                    break label21;
                default:
                    ++pos;
            }
        }

        this.indexRead = pos;
        return pos > start ? new String(val, start, pos - start) : "";
    }

    boolean matchConsumeIgnoreCase(String seq) {
        if (this.matchesIgnoreCase(seq)) {
            this.indexRead += seq.length();
            return true;
        } else {
            return false;
        }
    }
    boolean matchesIgnoreCase(String seq) {
//        this.bufferUp();
        int scanLength = seq.length();
        if (scanLength > this.bufLength - this.indexRead) {
            return false;
        } else {
            for(int offset = 0; offset < scanLength; ++offset) {
                char upScan = Character.toUpperCase(seq.charAt(offset));
                char upTarget = Character.toUpperCase(this.charBuffer[this.indexRead + offset]);
                if (upScan != upTarget) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean isEmpty() {
//        this.bufferUp();
        return this.indexRead >= this.bufLength;
    }

    boolean matchesAsciiAlpha() {
        if (this.isEmpty()) {
            return false;
        } else {
            char c = this.charBuffer[this.indexRead];
            return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
        }
    }

    int nextIndexOf(CharSequence seq) {
//        this.bufferUp();
        char startChar = seq.charAt(0);

        for(int offset = this.indexRead; offset < this.bufLength; ++offset) {
            if (startChar != this.charBuffer[offset]) {
                do {
                    ++offset;
                } while(offset < this.bufLength && startChar != this.charBuffer[offset]);
            }

            int i = offset + 1;
            int last = i + seq.length() - 1;
            if (offset < this.bufLength && last <= this.bufLength) {
                for(int j = 1; i < last && seq.charAt(j) == this.charBuffer[i]; ++j) {
                    ++i;
                }

                if (i == last) {
                    return offset - this.indexRead;
                }
            }
        }

        return -1;
    }

    String consumeTo(String seq) {
//        int offset = this.nextIndexOf(seq);
//        if (offset != -1) {
//            String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
//            this.bufPos += offset;
//            return consumed;
//        } else if (this.bufLength - this.bufPos < seq.length()) {
//            return this.consumeToEnd();
//        } else {
//            int endPos = this.bufLength - seq.length() + 1;
//            String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, endPos - this.bufPos);
//            this.bufPos = endPos;
//            return consumed;
//        }
        return "";
    }

    char consume() {
//        this.bufferUp();
        char val = this.isEmptyNoBufferUp() ? '\uffff' : this.charBuffer[this.indexRead];
        ++this.indexRead;
        return val;
    }

    private boolean isEmptyNoBufferUp() {
        return this.indexRead >= this.bufLength;
    }

    void unconsume() {
        if (this.indexRead < 1) {
            throw new UncheckedIOException(new IOException("WTF: No buffer left to unconsume."));
        } else {
            --this.indexRead;
        }
    }
    String consumeTagName() {
//        this.bufferUp();
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;
        char[] val = this.charBuffer;

        label21:
        while(pos < remaining) {
            switch (val[pos]) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                case '/':
                case '>':
                    break label21;
                default:
                    ++pos;
            }
        }

        this.indexRead = pos;

        return pos > start ? new String(this.charBuffer, start, pos - start) : "";
    }

    String defsConsumeName() {
//        this.bufferUp();
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;
        char[] val = this.charBuffer;

        label21:
        while(pos < remaining) {
            switch (val[pos]) {
                case '=':
                case '{':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                case '/':
                case '>':
                    break label21;
                default:
                    ++pos;
            }
        }

        this.indexRead = pos;

        return pos > start ? new String(this.charBuffer, start, pos - start) : "";
    }

    String defsConsumeExpression() {
//        this.bufferUp();
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;
        char[] val = this.charBuffer;

        label21:
        while(pos < remaining) {
            switch (val[pos]) {
                case '"':
                case '}':
                    break label21;
                default:
                    ++pos;
            }
        }

        this.indexRead = pos;

        return pos > start ? new String(this.charBuffer, start, pos - start) : "";
    }


    String consumeToAnySorted(char... chars) {
//        this.bufferUp();
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;

        for(char[] val = this.charBuffer; pos < remaining && Arrays.binarySearch(chars, val[pos]) < 0; ++pos) {
        }

        this.indexRead = pos;
        return this.indexRead > start ? cacheString(this.charBuffer, start, pos - start) : "";
    }

    String consumeAttributeQuoted(boolean single) {
        int pos = this.indexRead;
        int start = pos;
        int remaining = this.bufLength;

        label30:
        for(char[] val = this.charBuffer; pos < remaining; ++pos) {
            switch (val[pos]) {
                case '\u0000':
                case '&':
                    break label30;
                case '"':
                    if (!single) {
                        break label30;
                    }
                    break;
                case '\'':
                    if (single) {
                        break label30;
                    }
            }
        }

        this.indexRead = pos;
        return pos > start ? cacheString(this.charBuffer, start, pos - start) : "";
    }

    boolean matchesLetter() {
        if (this.isEmpty()) {
            return false;
        } else {
            char c = this.charBuffer[this.indexRead];
            return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || Character.isLetter(c);
        }
    }

    String consumeLetterSequence() {
//        this.bufferUp();

        int start;
        for(start = this.indexRead; this.indexRead < this.bufLength; ++this.indexRead) {
            char c = this.charBuffer[this.indexRead];
            if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && !Character.isLetter(c)) {
                break;
            }
        }

        return cacheString(this.charBuffer,  start, this.indexRead - start);
    }

    private static String cacheString(char[] charBuf, int start, int count) {
        return new String(charBuf, start, count);
    }
}
