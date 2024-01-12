package br.com.feliva.defs.components.dOutPut;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.ComponentUtils;

import java.io.IOException;
import java.util.logging.Logger;

//https://memorynotfound.com/jsf-custom-input-facescomponent-example/
//@FacesComponent(createTag = true, namespace = "feliva.simplex",
//        tagName = "dOutPutText", value = "DOutPutText")
public class DOutPutRenderer extends CoreRenderer {

    private static final Logger LOGGER = Logger.getLogger(DOutPutRenderer.class.getName());

    public void encodeBegin(FacesContext context) throws IOException {
        System.out.println("encodeBegin");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String value = ComponentUtils.getValueToRender(context, component);
        final ResponseWriter writer =  context.getResponseWriter();
        writer.endElement("span");
        writer.writeAttribute("id",component.getClientId(context),"id");
        writer.write(value);
        writer.endElement("span");

        System.out.println("encodeEnd");
    }

    protected void encodeRequiredIndicator(ResponseWriter writer, DOutPut label) throws IOException {
        System.out.println("encodeRequiredIndicator");
    }

    protected boolean isBeanValidationDefined(UIInput input, FacesContext context) {
        System.out.println("isBeanValidationDefined");
        return false;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        System.out.println("encodeChildren");
    }

    @Override
    public boolean getRendersChildren() {
        System.out.println("getRendersChildren");
        return true;
    }
}
