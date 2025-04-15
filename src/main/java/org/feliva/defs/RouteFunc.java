package org.feliva.defs;

import org.feliva.DefsAplications;
import org.feliva.tokenizer.DefsAttribute;
import org.feliva.tokenizer.Tag;

public class RouteFunc extends ViewFunc{

    public void process(Tag tag){

    }

    @Override
    public boolean discoveryProcess() {
        return true;
    }

    @Override
    public boolean process(DefsAttribute attribute, DefsAplications defsAplications) {
        //realizar o processamento da rota
        defsAplications.registerRoute(attribute);
        return false;
    }
}
