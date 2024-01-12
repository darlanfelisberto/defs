package br.com.feliva.defs.components.kamban;


import br.com.feliva.defs.components.kamban.model.Coluna;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.ValueHolder;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.convert.Converter;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

import java.util.List;

public abstract class KambanBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder , ValueHolder {

    public static final String COMPONENT_FAMILY = "br.com.feliva.defs.components";

    public static final String DEFAULT_RENDERER = "br.com.feliva.defs.components.kambanRenderer";

    public enum PropertyKeys {

        widgetVar,
        style,
        styleClass,
        title,
        converter,
        value,
        model
    }

    private Converter<?> converter;

    public KambanBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }


    public List<Coluna> getModel() {
         return (List<Coluna>) getStateHelper().eval(PropertyKeys.model, null);
    }

    public void setModel(List<Coluna> model) {
        getStateHelper().put(PropertyKeys.model, model);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }


    @Override
    public Converter getConverter() {

        if (converter != null) {
            return converter;
        }

        return (Converter) getStateHelper().eval(PropertyKeys.converter);
    }

    @Override
    public void setConverter(Converter converter) {

        clearInitialState();
        // we don't push the converter to the StateHelper
        // if it's been explicitly set (i.e. it's not a ValueExpression
        // reference
        this.converter = converter;
    }

    @Override
    public Object getLocalValue() {
        return getStateHelper().get(PropertyKeys.value);
    }

    /**
     * <p class="changed_added_2_2">
     * Return the value property.
     * </p>
     *
     * @since 2.2
     */
    @Override
    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value);
    }

    @Override
    public void setValue(Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }
 }