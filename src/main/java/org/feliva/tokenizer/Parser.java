package org.feliva.tokenizer;

import java.io.*;
import java.nio.file.Path;

public class Parser {

    CaracterReader reader;
    Tokeniser tokeniser;

    public void readerFile(Path path) throws IOException {
        this.reader = new CaracterReader(path);
        this.tokeniser = new Tokeniser(this.reader,new Document(path));
    }

    public Document parse(){
        do{
             this.tokeniser.state.read(this.tokeniser,this.reader);
        }while (this.reader.current() != 0);

        return tokeniser.document;
    }


}
