package br.com.feliva.defs.components.kamban.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Coluna<T extends Card> {

    public UUID getId();

    public List<T> getListCards();

    public String getLabel();

    public Coluna<T> addCard(T card);


}
