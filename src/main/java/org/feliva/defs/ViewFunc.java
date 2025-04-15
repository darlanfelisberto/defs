package org.feliva.defs;

import org.feliva.DefsAplications;
import org.feliva.tokenizer.DefsAttribute;

public abstract class ViewFunc {
    public abstract boolean discoveryProcess();

    public abstract boolean process(DefsAttribute attribute, DefsAplications defsAplications);
}
