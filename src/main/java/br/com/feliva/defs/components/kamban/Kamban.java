package br.com.feliva.defs.components.kamban;

;
import br.com.feliva.defs.components.kamban.model.Card;
import br.com.feliva.defs.components.kamban.model.Coluna;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;
import jakarta.faces.event.FacesEvent;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.MapBuilder;

import java.util.Collection;
import java.util.Map;

@FacesComponent(Kamban.COMPONENT_TYPE)
@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "defs", name = "kamban/kamban.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
@ResourceDependency(library = "defs", name = "kamban/kamban.js")
public class Kamban extends KambanBase {

    public static final String COMPONENT_TYPE = "br.com.feliva.component.kamban";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>>builder()
            .put("eventMove", KambanCardMoveEvent.class)
            .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();


    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }



    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        if (ComponentUtils.isRequestSource(this, context) && event instanceof AjaxBehaviorEvent) {
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String idCard = params.get("card-drop-id");
            String idColumn = params.get("column-drop-id");

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            Converter c = this.getConverter();

            KambanCardMoveEvent moveEvent = new KambanCardMoveEvent(this, behaviorEvent.getBehavior(), (Coluna) c.getAsObject(context,this,idColumn), (Card) c.getAsObject(context,this, idCard));
            super.queueEvent(moveEvent);
        }
        else {
            super.queueEvent(event);
        }
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (ComponentUtils.isRequestSource(this, context)) {
            decode(context);
        }
        else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processUpdates(context);
        }
    }

    public boolean isContentLoadRequest(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(getClientId(context) + "_contentLoad");
    }
}