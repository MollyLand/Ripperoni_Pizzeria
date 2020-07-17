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

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.fasterxml.jackson.core.*;
/*
    It describes the object of a single Ingredient in the system
*/
public final class Coupon extends Resource {
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PERCENTAGE = "percentage";
    public static final String KEY_ORDER = "order";

    private final Integer id;
    private final String username;
    private final Integer percentage;
    private final Integer order;

    public Coupon(Integer id, String username, Integer percentage) {
      this.id = id;
      this.username = username;
      this.percentage = percentage;
      this.order = null;
    }

    public Coupon(Integer id, String username, Integer percentage, Integer order) {
      this.id = id;
      this.username = username;
      this.percentage = percentage;
      this.order = order;
    }

    public Integer getID() {
      return id;
    }

    public String getUsername() {
      return username;
    }

    public Integer getPercentage() {
      return percentage;
    }

    public Integer getOrder() {
      return order;
    }

    //@Override
    //public final void toJSON(final OutputStream out) throws IOException {}

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i == 0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_ID, getID().toString());
        jg.writeStringField(KEY_USERNAME, getUsername());
        jg.writeStringField(KEY_PERCENTAGE, getPercentage().toString());
        jg.writeStringField(KEY_ORDER, getOrder().toString());
        jg.writeEndObject();
        if (i < end - 1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {}


}
