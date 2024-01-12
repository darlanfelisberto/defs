package br.com.feliva.defs.components.dOutPut;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.html.HtmlOutputLabel;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;

//https://memorynotfound.com/jsf-custom-input-facescomponent-example/
//@FacesComponent(createTag = true, namespace = "feliva.simplex",
//        tagName = "dOutPutText", value = "DOutPutText")
//@FacesComponent(DOutPut.COMPONENT_TYPE)
public class DOutPut extends HtmlOutputLabel {
    public static final String COMPONENT_TYPE = "br.com.feliva.defs.components.DOutPut";
    public static final String COMPONENT_FAMILY = "br.com.feliva.defs.components";

    public static final String DEFAULT_RENDERER = "br.com.feliva.defs.components.DOutPutRenderer";

    public enum PropertyKeys {

        indicateRequired;
    }

    public DOutPut(){
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

//    public void encodeBegin(FacesContext context) throws IOException {
//
//        String value = (String) getAttributes().get("value");
////        addFacesListener();
//
//        if (value != null) {
//            ResponseWriter writer = context.getResponseWriter();
//            writer.write("<span> DOutPutText aqui -->" + value.toUpperCase() + "</span>");
//        }
//    }

    public String getIndicateRequired() {
        return (String) getStateHelper().eval(PropertyKeys.indicateRequired, "auto");
    }

    public void setIndicateRequired(String indicateRequired) {
        getStateHelper().put(PropertyKeys.indicateRequired, indicateRequired);
    }

}
