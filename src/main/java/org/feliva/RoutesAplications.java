package org.feliva;

import jakarta.enterprise.context.ApplicationScoped;
import org.feliva.tokenizer.DefsBuild;
import org.feliva.tokenizer.DefsTag;
import org.feliva.tokenizer.Document;

@ApplicationScoped
public class RoutesAplications {

    DefsBuild defsBuild = new DefsBuild();

    public void processDoc(Document doc){
        doc.getIndexDef().forEach(tag -> {
            tag.getDefsAttributes().forEach(attr -> {
                this.defsBuild.processAttribute(attr);
            });
        });
    }

}
