package org.feliva.tokenizer;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class Parser {

    void readerFile(Path path) throws IOException {

        FileReader buffer = new FileReader(path.toFile());
        int cr = 0;
        do {
            cr = buffer.read();
            System.out.println((char)cr);
        }while (cr != -1);
    }
}
