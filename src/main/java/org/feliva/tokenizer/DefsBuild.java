package org.feliva.tokenizer;

import org.feliva.defs.RouteFunc;
import org.feliva.defs.ViewFunc;

import java.util.HashMap;
import java.util.Map;

public class DefsBuild {

    public static Map<String, ViewFunc> defsKeyWord = new HashMap<String, ViewFunc>();

    static {
        defsKeyWord.put("route", new RouteFunc());
    }


    public void processTags(){

    }

    public void processAttribute(DefsAttribute attr){
        ViewFunc func = defsKeyWord.get(attr.nome);
        if(func != null){

        }
    }
}
