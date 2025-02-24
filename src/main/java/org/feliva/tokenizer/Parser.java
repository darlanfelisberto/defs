package org.feliva.tokenizer;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Parser {

    CaracterReader reader;
    Tokeniser tokeniser;

    public void readerFile(Path path) throws IOException {
        this.reader = new CaracterReader(path);
        this.tokeniser = new Tokeniser(this.reader);
    }

    public void parse(){
        do{
            this.tokeniser.state.read(this.tokeniser,this.reader);
        }while (this.reader.current() != 0);
    }
}
