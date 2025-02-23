package org.feliva.tokenizer;

import org.jsoup.UncheckedIOException;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CaracterReader {
    SeekableByteChannel byteChannel;
    InputStream stream;
    Reader reader;

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
}
