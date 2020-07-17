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
import java.io.*;
import com.fasterxml.jackson.core.*;
/*
    It describes the object of a single pizza in the system
*/
public final class ClosingInvoice extends Resource {
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_BILLNUMBER = "billnumber";
    public static final String KEY_TOTALBILL = "totalbill";

    private final Integer id;
    private final Timestamp date;
    private final Integer billnumber;
    private final float totalbill;

    public ClosingInvoice(Integer id, Timestamp date, Integer billnumber, float totalbill) {
      this.id = id;
      this.date = date;
      this.billnumber = billnumber;
      this.totalbill = totalbill;
    }

    public Integer getID() {
      return id;
    }

    public Timestamp getDate() {
      return date;
    }

    public Integer getBillNumber() {
      return billnumber;
    }

    public float getTotalBill() {
      return totalbill;
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {}

    @Override
    public final void toJSON(final OutputStream out) throws IOException {
		final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
      jg.writeStartObject();
      jg.writeFieldName("");
      jg.writeStartObject();
      jg.writeEndObject();
      jg.writeEndObject();
      jg.flush();
    }
}
