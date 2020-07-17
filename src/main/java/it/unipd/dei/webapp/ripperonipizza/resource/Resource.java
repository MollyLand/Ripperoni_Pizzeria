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
import java.util.Iterator;
import java.util.List;
import java.io.*;

import com.fasterxml.jackson.core.*;
import org.json.JSONObject;

/**
 * Generic Resource interface.
 * <p>
 * Every subclass should be final.
 * <p>
 * Every resource should be JSON representable,
 * <p>
 * Every resource should have some primary fields.
 * <p>
 * Every field of the resource is nullable, so use Integer instead of int.
 *
 */
public abstract class Resource {

	protected static final JsonFactory JSON_FACTORY;

	static {
		// setup the JSON factory
		JSON_FACTORY = new JsonFactory();
		JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
	}

    public abstract void toJSON(final OutputStream out) throws IOException;


    // Used to print list objects into json array objects
    public abstract void toJSON(final OutputStream out, final int i, final int end) throws IOException;
    
}
