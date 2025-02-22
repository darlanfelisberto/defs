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
            int read = reader.read(charBuffer, bufLength, BUFFER_SIZE);
            if (read == -1) {
                finish = true;
                break;
            }
            bufLength += read;
        }
    }

    public char read() throws IOException {
        if(indexRead >= BUFFER_SIZE){
            this.readToBuffer();
        }
        return charBuffer[indexRead];
    }
}
