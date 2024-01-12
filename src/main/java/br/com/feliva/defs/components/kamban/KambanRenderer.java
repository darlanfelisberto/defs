package br.com.feliva.defs.components.kamban;

import br.com.feliva.defs.components.kamban.model.Card;
//import br.com.feliva.defs.components.kamban.model.CardImpl;
import br.com.feliva.defs.components.kamban.model.Coluna;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KambanRenderer extends CoreRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Kamban fieldset = (Kamban) component;
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = fieldset.getClientId(context);
        String toggleStateParam = clientId + "_collapsed";

//        if (params.containsKey(toggleStateParam)) {
//            fieldset.setCollapsed(Boolean.parseBoolean(params.get(toggleStateParam)));
//        }

        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        Kamban kamban = (Kamban) component;
        kamban.getModel();

        if(kamban.isRendered()){
            encodeMarkup(context, kamban);
        }

        encodeScript(context, kamban);
    }

    protected void encodeMarkup(FacesContext context, Kamban kamban) throws IOException {
             ResponseWriter wr = context.getResponseWriter();
        String clientId = kamban.getClientId(context);
        String widgetVar = kamban.resolveWidgetVar(context);
        String title = kamban.getTitle();

        List<Coluna> listColumn =  kamban.getModel();
        wr.startElement("div",kamban);
        wr.writeAttribute("id",kamban.getClientId(),"id");
        wr.writeAttribute("class","de-kamban","styleClass");
        for (Coluna column : listColumn){
            String idColumn = kamban.getConverter().getAsString(context,kamban,column);
            wr.startElement("ul",null);
            wr.writeAttribute("id", idColumn,null);
            wr.writeAttribute("data-id", idColumn,null);
            wr.writeAttribute("class","de-kamban-column","styleClass");
            wr.startElement("li",null);
            wr.write(column.getLabel());
            wr.endElement("li");

            this.encodeCards(context,kamban,column);
            wr.endElement("ul");

        }
        wr.endElement("div");

    }

    public void encodeCards(FacesContext context, Kamban kamban, Coluna column) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if(column.getListCards() == null || column.getListCards().isEmpty() ){
            return;
        }

        List<Card> listCard = (List<Card>) column.getListCards();

        for (int i = 0; i < listCard.size(); i++ ){
            Card card = listCard.get(i);
            this.encodeItem(context, kamban,card);
        }
    }

    protected void encodeItem(FacesContext context,Kamban kamban, Card card) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String idItem = kamban.getConverter().getAsString(context,kamban,card);
        writer.startElement("li",null);
        writer.writeAttribute("id", idItem,null);
        writer.writeAttribute("draggable","true",null);
        writer.writeAttribute("data-id", idItem,null);
        writer.writeAttribute("class","de-kamban-item","styleClass");
        writer.startElement("a",null);
        writer.writeAttribute("href", "#" + card.getTitle(), null);
        writer.writeAttribute("tabindex", "-1", null);
        writer.write(card.getTitle());
        writer.endElement("a");
        writer.endElement("li");
    }

    protected void encodeContent(FacesContext context, Fieldset fieldset) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", null);
        writer.writeAttribute("class", Fieldset.CONTENT_CLASS, null);
        if (fieldset.isCollapsed()) {
            writer.writeAttribute("style", "display:none", null);
        }

        if (!fieldset.isDynamic()) {
            renderChildren(context, fieldset);
        }

        writer.endElement("div");
    }

    protected void encodeScript(FacesContext context, Kamban kamban) throws IOException {
        WidgetBuilder wb = getWidgetBuilder(context);
        wb.init("Kamban", kamban);


        encodeClientBehaviors(context, kamban);

        wb.finish();
    }

    protected void encodeStateHolder(FacesContext context, Fieldset fieldset) throws IOException {
        String name = fieldset.getClientId(context) + "_collapsed";
        renderHiddenInput(context, name, String.valueOf(fieldset.isCollapsed()), false);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
