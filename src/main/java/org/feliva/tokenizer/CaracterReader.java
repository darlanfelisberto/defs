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

    char[] charBuffer = new char[8096];

    CaracterReader(Path path) throws IOException {
        this.byteChannel = Files.newByteChannel(path);
        this.stream = Channels.newInputStream(byteChannel);
        this.reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
//        new Chara

    }

    public char read() throws IOException {

        while (bufLength < BufferSize) {
            try {
                int read = reader.read(charBuffer, 8096, charBuf.length - bufLength);
                if (read == -1) {
                    readFully = true;
                    break;
                }
                bufLength += read;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
