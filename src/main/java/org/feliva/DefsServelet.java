package org.feliva;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.feliva.tokenizer.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebFilter(urlPatterns = "/defs/*")
public class DefsServelet implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        getClass().getClassLoader().getResource("defs_view");//funcona
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        URL folder = getClass().getClassLoader().getResource("defs_view");//funcona
        try {
//            this.listFilesUsingDirectoryStream(folder);
            listFiles(Path.of(folder.toURI())).forEach(path -> {
                try {
                    Parser p = new Parser();
                    p.readerFile(path);
                    p.parse();
                    Document doc = Jsoup.parse(path.toFile(), "UTF-8");


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile).collect(Collectors.toList());
        }
        return result;

    }


}
