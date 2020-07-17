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
/*
    It describes the object of a single order in the system
*/
public final class Order extends Resource {
    public static final String KEY_IDORDER = "idOrder";
    public static final String KEY_DELIVERUSERNAME = "deliverUsername";
    public static final String KEY_IDCLOSING = "idClosing";
    public static final String KEY_CUSTOMERUSERNAME = "customerUsername";
    public static final String KEY_REQUESTTIME = "requestTime";
    public static final String KEY_DELIVERYTIME = "deliveryTime";
    public static final String KEY_ORDERSTATUS = "orderStatus";
    public static final String KEY_PRICE = "price";

    private final Integer idOrder;
    private final String deliverUsername;
    private final Integer idClosing;
    private final String customerUsername;
    private final Timestamp requestTime;
    private final Timestamp deliveryTime;
    private final String orderStatus;
    private final Float price;

    public Order(Integer idOrder, String deliverUsername, Integer idClosing, String customerUsername, Timestamp requestTime, Timestamp deliveryTime, String orderStatus, Float price) {
      this.idOrder = idOrder;
      this.deliverUsername = deliverUsername;
      this.idClosing = idClosing;
      this.customerUsername = customerUsername;
      this.requestTime = requestTime;
      this.deliveryTime = deliveryTime;
      this.orderStatus = orderStatus;
      this.price = price;
    }

    public Integer getIDOrder() {
        return idOrder;
    }

    public String getDeliverUsername() {
        return deliverUsername;
    }

    public Integer getIDClosing() {
        return idClosing;
    }

    public String getCustomerUsername() {
      return customerUsername;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public String getOrderStatus() {
      return orderStatus;
    }

    public Float getPrice() {
      return price;
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {
      final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
      if (i==0) jg.writeRaw("[");
        jg.writeStartObject();
        jg.writeStringField(KEY_IDORDER, getIDOrder().toString());
        jg.writeStringField(KEY_DELIVERUSERNAME, getDeliverUsername());
        jg.writeStringField(KEY_IDCLOSING, getIDClosing().toString());
        jg.writeStringField(KEY_CUSTOMERUSERNAME, getCustomerUsername());
        jg.writeStringField(KEY_REQUESTTIME, getRequestTime().toString());
        jg.writeStringField(KEY_DELIVERYTIME, getDeliveryTime().toString());
        jg.writeStringField(KEY_ORDERSTATUS, getOrderStatus());
        jg.writeStringField(KEY_PRICE, getPrice().toString());
        jg.writeEndObject();
        if (i<end-1) jg.writeRaw(",");
        else jg.writeRaw("]");
        jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {

    }


}
