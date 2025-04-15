package org.feliva;

import jakarta.enterprise.context.ApplicationScoped;
import org.feliva.defs.Route;
import org.feliva.defs.ViewFunc;
import org.feliva.tokenizer.DefsAttribute;
import org.feliva.tokenizer.DefsBuild;
import org.feliva.tokenizer.Document;
import org.feliva.tokenizer.Tag;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class DefsAplications {

    DefsBuild defsBuild = new DefsBuild();

    Map<String, Tag> routes = new HashMap<>();

    public void processDoc(Document doc){
        doc.getIndexDef().forEach(tag -> {
            tag.getDefsAttributes().forEach(attr -> {
                ViewFunc func = DefsBuild.defsKeyWord.get(attr.getNome());
                if(func != null && func.discoveryProcess()){
                    func.process(attr,this);
                }
            });
        });
    }

    public void registerRoute(DefsAttribute attribute){
        routes.put(attribute.getValue(),attribute.getTagParent());
    }
}
