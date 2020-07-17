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

import java.io.*;
import com.fasterxml.jackson.core.*;
/*
    It describes the object of a single Ingredient in the system
*/
    public final class Ingredient extends Resource {
      public static final String KEY_ID = "idingredient";
      public static final String KEY_CATEGORY = "category";
      public static final String KEY_ICON = "icon";
      public static final String KEY_NAME = "ingredientname";
      public static final String KEY_CUMPRICE = "ingredientprice";

      private final Integer id;
      private final String category;
      private final String icon;
      private final String ingredientname;
      private final Float cumprice;

      public Ingredient(Integer id, String category, String icon, String ingredientname , Float cumprice) {
        this.id = id;
        this.category = category;
        this.icon = icon;
        this.ingredientname = ingredientname;
        this.cumprice = cumprice;
      }

      public Integer getID() {
        return id;
      }

      public String getCategory() {
        return category;
      }

      public String getIcon() {
        return icon;
      }

      public String getIngredientName() {
        return ingredientname;
      }

      public Float getCumPrice() {
        return cumprice;
      }

      @Override
      public final void toJSON(final OutputStream out) throws IOException {}

      @Override
      public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        if (i==0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_ID, getID().toString());
        jg.writeStringField(KEY_CATEGORY, getCategory());
        jg.writeStringField(KEY_ICON, getIcon());
        jg.writeStringField(KEY_NAME, getIngredientName());
        jg.writeStringField(KEY_CUMPRICE, getCumPrice().toString());
        jg.writeEndObject();
        if (i<end-1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
      }

}
