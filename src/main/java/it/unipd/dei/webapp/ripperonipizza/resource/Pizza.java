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
public final class Pizza extends Resource {
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CREATIONDATE = "creationdate";
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_INGREDIENTS = "ingredients";

    private String pizzaname;
    private String username;
    private Float price;
    private Timestamp creationDate;
    private Integer success;
    private String ingredients;

    public Pizza(String pizzaname, String username, Float price, Timestamp creationDate, Integer success) {
        this.pizzaname = pizzaname;
        this.username = username;
        this.price = price;
        this.creationDate = creationDate;
        this.success = success;
        this.ingredients = null;
    }

    public void SetIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public String getPizzaName() {
        return pizzaname;
    }
    
    public String getUsername() {
        return username;
    }

    public Float getPrice() {
        return price;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Integer getSuccess() {
        return success;
    }

    public String getIngredients() {
        return ingredients;
    }
    
    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i==0) jg.writeRaw("[");
		jg.writeStartObject();
		jg.writeStringField(KEY_NAME, getPizzaName());
		jg.writeStringField(KEY_USERNAME, getUsername());
        jg.writeStringField(KEY_PRICE, getPrice().toString());
        jg.writeStringField(KEY_INGREDIENTS, getIngredients());
        jg.writeStringField(KEY_SUCCESS, getSuccess().toString());
		jg.writeEndObject();
        if (i<end-1) jg.writeRaw(",");
        else jg.writeRaw("]");
		jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {}
}
