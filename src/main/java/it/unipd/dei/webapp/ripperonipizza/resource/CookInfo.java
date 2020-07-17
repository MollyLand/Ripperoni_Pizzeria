package it.unipd.dei.webapp.ripperonipizza.resource;

import java.io.*;

import com.fasterxml.jackson.core.*;

/**
 it describes the object CookInfo used to crate the Cook Queue
 */

public final class CookInfo extends Resource {

    public static final String KEY_COOKIDORDER = "idorder";
    public static final String KEY_COOKPIZZANAME = "pizzaname";
    public static final String KEY_COOKQUANTITY = "quantity";
    public static final String KEY_INGREDIENTS = "ingredients";

    private Integer id_order;
    private String pizzaname;
    private Integer quantity;
    private String ingredients;

    public CookInfo(Integer id_order, String pizzaname, Integer quantity) {
        this.id_order = id_order;
        this.pizzaname = pizzaname;
        this.quantity = quantity;
        this.ingredients = null;
    }

    public Integer getId_order() {
        return id_order;
    }

    public String getPizzaname() {
        return pizzaname;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i == 0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_COOKIDORDER, getId_order().toString());
        jg.writeStringField(KEY_COOKPIZZANAME, getPizzaname());
        jg.writeStringField(KEY_COOKQUANTITY, getQuantity().toString());
        jg.writeStringField(KEY_INGREDIENTS, getIngredients());
        jg.writeEndObject();
        if (i < end - 1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {}

}