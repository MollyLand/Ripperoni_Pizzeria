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
import org.json.JSONObject;
/*
    It describes the object of a single Ingredient in the system
*/
public final class Oven extends Resource {
    public static final String KEY_ID = "id";
    public static final String KEY_CAPACITY= "capacity";


    private final Integer id;
    private final Integer capacity;

    public Oven(Integer id, Integer capacity) {
      this.id = id;
      this.capacity = capacity;
    }

    public Integer getID() {
      return id;
    }

    public Integer getCapacity() {
      return capacity;
    }

    
    @Override
    public final void toJSON(final OutputStream out) throws IOException {}

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {}

}
