package org.feliva;

import io.quarkus.runtime.Quarkus;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.feliva.tokenizer.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebListener
public class DefsListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized");


        URL folder = getClass().getClassLoader().getResource("defs_view");//funcona
        try {
//            this.listFilesUsingDirectoryStream(folder);
            listFiles(Path.of(folder.toURI())).forEach(path -> {
                try {
                    System.out.println(path.toString());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile).collect(Collectors.toList());
        }
        return result;

    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed");
    }
}