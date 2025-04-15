package org.feliva;

import io.undertow.servlet.spec.HttpServletRequestImpl;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.feliva.tokenizer.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebFilter(urlPatterns = "/*")
public class DefsServelet implements Filter {

    @Inject
    DefsAplications routesAplications;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        getClass().getClassLoader().getResource("defs_view");//funcona
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequestImpl requestImpl = (HttpServletRequestImpl) request;

        try {
            URL viewURL = getClass().getClassLoader().getResource("defs_view");//funcona
            Path viewPath = Path.of(viewURL.toURI());
//            this.listFilesUsingDirectoryStream(folder);
            List<Path> listFiles = listFiles(viewPath);

            Map<String,Path> urlrequest = new HashMap<>();
            listFiles.forEach(file -> {
                urlrequest.put(viewPath.relativize(file).toString(), file);
            });

            String rr = requestImpl.getRequestURI().substring(requestImpl.getContextPath().length()+1);

            Path requestPath = urlrequest.get("index.html");

            if(requestPath == null) {
                System.out.println("nao encontrado");
                return;
            }

//            listFiles.forEach(path -> {
                try {
                    Parser p = new Parser();
                    p.readerFile(requestPath);
                    org.feliva.tokenizer.Document d = p.parse();
                    routesAplications.processDoc(d);
                    Document doc = Jsoup.parse(requestPath.toFile(), "UTF-8");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//            });
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
