package it.unipd.dei.webapp.ripperonipizza.resource;

import java.sql.Timestamp;

import java.io.*;

import com.fasterxml.jackson.core.*;

/**
 it describes the object DeliverInfo used to crate the Delivery Queue
 */

public final class DeliveryInfo extends Resource {

    public static final String KEY_CUSTOMER_LASTNAME = "customer_lastname";
    public static final String KEY_CUSTOMER_ADDRESS = "customer_address";
    public static final String KEY_INFODELIVERYTIME = "delivery_time";
    public static final String KEY_NUMPIZZAS = "number_pizzas";
    public static final String KEY_IDORDER = "id_order";

    private String customer_lastname;
    private String customer_address;
    private Timestamp deliverytime;
    private Integer numpizzas;
    private Integer id_order;

    public DeliveryInfo(String customer_lastname, String customer_address, Timestamp deliverytime, Integer numpizzas, Integer id_order){
        this.customer_lastname = customer_lastname;
        this.customer_address = customer_address;
        this.deliverytime = deliverytime;
        this.numpizzas = numpizzas;
        this.id_order = id_order;
    }

    public String getCustomer_lastname() {
        return customer_lastname;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public Timestamp getDeliverytime() {
        return deliverytime;
    }

    public Integer getNumpizzas() {
        return numpizzas;
    }

    public Integer getIDorder() {
        return id_order;
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i == 0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_IDORDER, getIDorder().toString());
        jg.writeStringField(KEY_CUSTOMER_LASTNAME, getCustomer_lastname());
        jg.writeStringField(KEY_CUSTOMER_ADDRESS, getCustomer_address());
        jg.writeStringField(KEY_INFODELIVERYTIME, getDeliverytime().toString());
        jg.writeStringField(KEY_NUMPIZZAS, getNumpizzas().toString());
        jg.writeEndObject();
        if (i < end - 1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {}

}