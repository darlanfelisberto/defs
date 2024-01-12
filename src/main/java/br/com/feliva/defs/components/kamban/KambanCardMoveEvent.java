package br.com.feliva.defs.components.kamban;

import br.com.feliva.defs.components.kamban.model.Card;
import br.com.feliva.defs.components.kamban.model.Coluna;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.Behavior;
import org.primefaces.event.AbstractAjaxBehaviorEvent;

public class KambanCardMoveEvent extends AbstractAjaxBehaviorEvent {

    private Coluna column;
    private Card card;
    public KambanCardMoveEvent(UIComponent component, Behavior behavior, Coluna dragover, Card card) {
        super(component, behavior);

        this.column = dragover;
        this.card = card;
    }

    public Coluna getRaia() {
        return this.column;
    }

    public Card getCard() {
        return card;
    }

}
