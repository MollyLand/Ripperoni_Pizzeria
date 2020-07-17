/*
 * Copyright 2019-20 Ripperoni
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http: //www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.webapp.ripperonipizza.resource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

import com.fasterxml.jackson.core.*;
import org.json.JSONObject;
/*
    It describes the object of a single pizza in the system
*/
    public final class ConsistOf extends Resource {
    public static final String KEY_PIZZANAME = "pizzaname";
    public static final String KEY_IDINGREDIENT = "idingredient";

    private String pizzaname;
    private Integer idingredient;


    public ConsistOf(String pizzaname, Integer idingredient) {
        this.pizzaname = pizzaname;
        this.idingredient = idingredient;
    }

    public String getPizzaName() {
        return pizzaname;
    }

    public Integer getIDIngredient() {
        return idingredient;
    }


    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i==0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_PIZZANAME, getPizzaName());
        jg.writeStringField(KEY_IDINGREDIENT, getIDIngredient().toString()); 
        jg.writeEndObject();
        if (i<end-1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {}
}
