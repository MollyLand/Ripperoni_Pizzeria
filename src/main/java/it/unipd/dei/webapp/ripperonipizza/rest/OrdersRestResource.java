/*
 * Copyright 2018 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.webapp.ripperonipizza.rest;

import it.unipd.dei.webapp.ripperonipizza.database.*;
import it.unipd.dei.webapp.ripperonipizza.resource.*;

import java.io.*;
import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.*;




public final class OrdersRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Pizza} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public OrdersRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}


    /**
	 * Reads the entire order table from the database.
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */
	public void readOrders() throws IOException {

		List<Order> orderList  = null;
		Message m = null;
    	OrderDAO orderObj = null;

		try{

			// creates a new object for accessing the database and reads the order
			orderObj = new OrderDAO(con);
			orderList = orderObj.findOrdersUsers();

			if(orderList != null) {
                for(int i=0; i<orderList.size(); i++){
                    res.setStatus(HttpServletResponse.SC_OK);
					orderList.get(i).toJSON(res.getOutputStream(), i, orderList.size());
				}
				con.close();
			} else {
				m = new Message(String.format("Pizza catalogue not found."), "E5A3", null);
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				m.toJSON(res.getOutputStream());
			}
		} catch (Throwable t) {
			m = new Message("Cannot read Order list catalogue: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}


	public void readUserOrders() throws IOException {

		List<Order> orderList  = null;
		Message m = null;
   		OrderDAO orderObj = null;

		try{

			// parse the URI path to extract the username
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf("username") + 8);
			final String username = path.substring(1);

			// creates a new object for accessing the database and reads the order
			orderObj = new OrderDAO(con);
			orderList = orderObj.findOrdersUser(username);

			if(orderList != null) {
        		for(int i=0; i<orderList.size(); i++){
						res.setStatus(HttpServletResponse.SC_OK);
						orderList.get(i).toJSON(res.getOutputStream(), i, orderList.size());
				}
				con.close();
			} else {
				m = new Message(String.format("Order catalogue not found."), "E5A3", null);
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				m.toJSON(res.getOutputStream());
			}
		} catch (Throwable t) {
			m = new Message("Cannot read Order list catalogue: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}

public void insertOrder() throws IOException {
	Order order = null;
	Message m = null;
	try
	{
		String data = IOUtils.toString(req.getReader());
		JSONObject json = new JSONObject(data);

		JSONArray arrayPizzas = json.getJSONArray("pizzas");
		JSONArray arrayQuantities = json.getJSONArray("quantities");
		String coupon = json.getString("coupon");
		ArrayList<String> pizzas = new ArrayList<String>();
		ArrayList<Integer> quantities = new ArrayList<Integer>();


		PizzaDAO objPizza = new PizzaDAO(con);
		Integer numberOfPizzas = arrayPizzas.length();
		double totalPrice = 0;

		// Here we have to evaluate the price of the pizza!!!!!
		for ( int i = 0; i < numberOfPizzas; i++)
		{
			double price = objPizza.findPizza(arrayPizzas.getString(i)).getPrice() * arrayQuantities.getInt(i);
			pizzas.add(arrayPizzas.getString(i));
			quantities.add(arrayQuantities.getInt(i));
			totalPrice += price;
			objPizza.increaseSuccess(arrayPizzas.getString(i),arrayQuantities.getInt(i));

		}

		OrderDAO objOrder = new OrderDAO(con);

		// Now we have to create the order object
		String customer = json.getString("username");
		String deliverUsername = null; // No delivery man has taken the order yet
		Integer idClosing = 1; // dummy
		Timestamp requestTime = new Timestamp(System.currentTimeMillis());
		Timestamp deliveryTime = null;
		String orderStatus = "InCharge";
		Float price = new Float(totalPrice);



		Integer IDorder = objOrder.findNextID();

		order = new Order(IDorder,deliverUsername,idClosing,customer,requestTime,deliveryTime,orderStatus,price);
		objOrder.insertNewOrder(order);
		// Ok, If we reach this position, the order has been placed.

		// Now we update the coupon table and apply the discount to the order, if a correct coupon is inserted
		if (!coupon.equals("none"))
		{
			Integer idcoupon = new Integer(coupon);
			CouponDAO couponObj = new CouponDAO(con);
			couponObj.updateCoupon(idcoupon,customer,IDorder);
			objOrder.discountOrderFromCoupon(IDorder,idcoupon);
		}
		// Now we have to insert the pizzas into Contain Table
		ContainDAO objContain = new ContainDAO(con);
		for ( int i = 0; i < numberOfPizzas; i++)
			objContain.insertNewContain(IDorder, pizzas.get(i), quantities.get(i));


		// Now we choose a cook fro the order and insert a row in the cook table
		CookQueueDAO cookObj = null;
		Message m2 = null;
		String cookusername = null;
		try{
			cookObj = new CookQueueDAO(con);
			cookusername = cookObj.chooseCookOrder();
			cookObj.insertCookOrder(IDorder, cookusername);
		}
		catch (Throwable t) {
			m2 = new Message("Cannot assign a Cook to this order: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m2.toJSON(res.getOutputStream());
		}
		//if we reach this position the cook has been assigned

		// Now we have to calculate the delivery time and update it in the order table
		DeliveryQueueDAO deliveryObj = null;
		OrderDAO orderObj = null;
		Message m3 = null;
		String deliveryusername = null;
		Timestamp newDeliveryTime;
		Integer cNumpizzas;				// obtainable using chosenCookNumpizzas
		Integer ovenCapacity;			// obtainable using findOvenCapacity

		//These long are milliseconds interval, needed for working with the Timestamps
		long cDuration = 3*60*1000; 	//oven time in milliseconds
		long lastDelivery = 0;				// obtainable using findeNextDeliverytime
		long tripDuration = 10*60*1000; //time to reach the addres or coming back to the pizzeria, in milliseconds
		long deliveryTimeMS;
		long current = System.currentTimeMillis();		//current moment

		try{
			deliveryObj = new DeliveryQueueDAO(con);
			orderObj = new OrderDAO(con);
			cookObj = new CookQueueDAO(con);

			// choose a delivery guy
			deliveryusername = deliveryObj.chooseDeliveryOrder();

			// update order table with new delivery guy
			orderObj.updateDeliveryOrder(IDorder, deliveryusername);

			//retrieve data for the calculation of delivery time
			cNumpizzas = cookObj.chosenCookNumpizzas(cookusername);
			ovenCapacity = cookObj.findOvenCapacity(cookusername);
			lastDelivery = deliveryObj.findNextDeliveryTime(deliveryusername).getTime();

			//calculate delivery time
			deliveryTimeMS = Math.max(current + ((cNumpizzas / ovenCapacity + 1) * cDuration), lastDelivery + tripDuration) + tripDuration;
			// deliveryTimeMS = Math.max(current + cNumpizzas*cDuration, lastDelivery + tripDuration) + tripDuration;
			newDeliveryTime = new Timestamp(deliveryTimeMS);

			//update order table with new delivery time
			orderObj.updateOrderDeliveryTime(IDorder, newDeliveryTime);

		}
		catch (Throwable t) {
			m3 = new Message("Cannot assign a Delivery Guy to this order: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m3.toJSON(res.getOutputStream());
		}



		// now we have assigned the cook to the order
		//makeOrderCookRow(IDorder);

		// now we have assigned the delivery guy and the delivery time to the order
		//updateOrderDelivery(IDorder);

		// no we can close the connection from the pool
		con.close();

		m = new Message("If you see this message, then everything is GOOOOOD!!");

		m.toJSON(res.getOutputStream());
	}
	catch (Throwable t) {
		m = new Message("Cannot read Order list catalogue: unexpected error.", "E5A1", t.getMessage());
		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		m.toJSON(res.getOutputStream());
	}

	}


	//OBSOLETE METHOD
	//this method calls two methods from CookQueueDAO: it retrieves the username of an available cook and
	// insert a new row in the Cook table
	public void makeOrderCookRow(final Integer IDorder) throws IOException
	{
		CookQueueDAO cookObj = null;
		Message m = null;
		String cookusername;
		try{
			cookObj = new CookQueueDAO(con);
			cookusername = cookObj.chooseCookOrder();
			cookObj.insertCookOrder(IDorder, cookusername);
			//maybe change order status here? low priority
		}
		catch (Throwable t) {
			m = new Message("Cannot assign a Cook to this order: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}

	// OBSOLETE METHOD
	//this method calls two methods from DeliveryQueueDAO: it retrieves the username of an available delivery guy and
	// update the order table
	public void updateOrderDelivery(final Integer IDorder) throws IOException
	{
		DeliveryQueueDAO deliveryObj = null;
		OrderDAO orderObj = null;
		Message m = null;
		String deliveryusername = null;
		Timestamp deliveryTime;
		long cNumpizzas;
		long cDuration = 3*60*1000; 	//oven time in milliseconds
		long lastDelivery;
		long tripDuration = 10*60*1000; //time to reach the addres or coming back to the pizzeria, in milliseconds
		long deliveryTimeMS;

		try{
			deliveryObj = new DeliveryQueueDAO(con);
			orderObj = new OrderDAO(con);

			deliveryusername = deliveryObj.chooseDeliveryOrder();
			orderObj.updateDeliveryOrder(IDorder, deliveryusername);
			deliveryTime =  new DeliveryQueueDAO(con).findNextDeliveryTime(deliveryusername);
			orderObj.updateOrderDeliveryTime(IDorder, deliveryTime);

		}
		catch (Throwable t) {
			m = new Message("Cannot assign a Delivery Guy to this order: unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}

	// alternative solution using another URI
    // public void updateOrderStatus() throws IOException {

    // String role = null;
    // // String newstatus = null;
    // String username = null;
    // Message m = null;
    // try {


    //     String path = req.getRequestURI();
    //     path = path.substring(path.lastIndexOf("id") + 2);
    //     final Integer uri_id_order = Integer.parseInt(path.substring(1));

    //     String data = IOUtils.toString(req.getReader());
    //     JSONObject json = new JSONObject(data);

    //     Integer idorder = json.getInt("idorder");
    //     username = json.getString("username");
    //     // newstatus = json.getString("status");

    //     if (uri_id_order == idorder){

    //         role = new UserDAO(con, null).findEmployeeRole(username);

    //         switch(role){
    //             case "Cook":
    //                 new CookQueueDAO(con).updateOrderStatus(idorder);
    //                 break;

    //             case "Delivery Guy":
    //                 new DeliveryQueueDAO(con).updateOrderStatus(idorder);
    //                 break;

    //             default:
    //                 m = new Message("Not authorized");
    //                 m.toJSON(res.getOutputStream());
    //                 break;
    //         }

    //         m = new Message("In heaven .. everything is fine!");
    //         res.setStatus(HttpServletResponse.SC_OK);
    //         m.toJSON(res.getOutputStream());

    //     } else {
    //         m = new Message("What are you trying to do? Cannot update order status");
    //         res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //         m.toJSON(res.getOutputStream());
    //     }

    // } catch (Throwable t) {
    //         m = new Message("Cannot update the order status sorry unexpected error.", "E5A1", t.getMessage());
    //         res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //         m.toJSON(res.getOutputStream());
    //     }
    // }
}
