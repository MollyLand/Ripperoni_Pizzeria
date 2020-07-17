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

package it.unipd.dei.webapp.ripperonipizza.servlet;

import it.unipd.dei.webapp.ripperonipizza.resource.*;
import it.unipd.dei.webapp.ripperonipizza.rest.OrdersRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.PizzaRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.IngredientRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.DeliveryInfoRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.EditProfileRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.CookInfoRestResource;
import it.unipd.dei.webapp.ripperonipizza.rest.CouponRestResource;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import org.json.*;



public final class RestManagerServlet extends AbstractDatabaseServlet {

	/**
	 * The JSON MIME media type
	 */
	private static final String JSON_MEDIA_TYPE = "application/json";

	/**
	 * The JSON UTF-8 MIME media type
	 */
	private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

	/**
	 * The any MIME media type
	 */
	private static final String ALL_MEDIA_TYPE = "*/*";

	@Override
	protected final void service(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {

		res.setContentType(JSON_UTF_8_MEDIA_TYPE);
		final OutputStream out = res.getOutputStream();

		try {
			// if the request method and/or the MIME media type are not allowed, return.
			// Appropriate error message sent by {@code checkMethodMediaType}
			if (!checkMethodMediaType(req, res)) {
				return;
			}

            // if the requested resource was a Pizza, delegate its processing and return
			if (processPizza(req, res)) {
				return;
			}

			// if the requested resource was an order, delegate its processing and return
			if (processOrder(req, res)) {
				return;
			}

            // if the requested resource was Ingredient, delegate its processing and return
			if (processIngredient(req, res)) {
				return;
			}

			//if the requested resource is delivery queue, delegate its processing and return
			if (processDeliveryQueue(req, res)) {
				return;
			}

			//if the requested resource is cook queue, delegate its processing and return
			if (processCookQueue(req, res)) {
				return;
			}

			if (processCoupon(req,res)) {
				return;
			}

			if (processUserInformation(req,res))
			{
				return;
			}

			// if none of the above process methods succeeds, it means an unknow resource has been requested
			final Message m = new Message("Unknown resource requested.", "E4A6",
										  String.format("Requested resource is %s.", req.getRequestURI()));
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			m.toJSON(out);
		} finally {
			// ensure to always flush and close the output stream
			out.flush();
			out.close();
		}
	}

	/**
	 * Checks that the request method and MIME media type are allowed.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request method and the MIME type are allowed; {@code false} otherwise.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean checkMethodMediaType(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final String contentType = req.getHeader("Content-Type");
		final String accept = req.getHeader("Accept");
		final OutputStream out = res.getOutputStream();

		Message m = null;

		if(accept == null) {
			m = new Message("Output media type not specified.", "E4A1", "Accept request header missing.");
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(out);
			return false;
		}

		if(!accept.contains(JSON_MEDIA_TYPE) && !accept.equals(ALL_MEDIA_TYPE)) {
			m = new Message("Unsupported output media type. Resources are represented only in application/json.",
							"E4A2", String.format("Requested representation is %s.", accept));
			res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			m.toJSON(out);
			return false;
		}

		switch(method) {
			case "GET":
			case "DELETE":
				// nothing to do
				break;

			case "POST":
			case "PUT":
				if(contentType == null) {
					m = new Message("Input media type not specified.", "E4A3", "Content-Type request header missing.");
					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					m.toJSON(out);
					return false;
				}

				if(!contentType.contains(JSON_MEDIA_TYPE)) {
					m = new Message("Unsupported input media type. Resources are represented only in application/json.",
									"E4A4", String.format("Submitted representation is %s.", contentType));
					res.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
					m.toJSON(out);
					return false;
				}

				break;
			default:
				m = new Message("Unsupported operation.",
								"E4A5", String.format("Requested operation %s.", method));
				res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				m.toJSON(out);
				return false;
		}

		return true;
	}


	/**
	 * Checks whether the request if for an {@link Pizza} or {link Order} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Pizza} or {@code Order}; {@code false} otherwise.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean processPizza(HttpServletRequest req, HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		if(path.lastIndexOf("rest/pizza") > 0)
		{
		try {
			// strip everyhing until after the /pizza
			path = path.substring(path.lastIndexOf("pizza") + 5);

			// the request URI is: /pizza
			// if method GET, list all the pizza in the catalogue
			if (path.length() == 0 || path.equals("/")) {

				switch (method) {
					case "GET":
						new PizzaRestResource(req, res, getDataSource().getConnection()).readPizzaCatalogue();
						break;
					case "POST":
						new PizzaRestResource(req, res, getDataSource().getConnection()).insertNewPizza();
						break;
					default:
						m = new Message("Unsupported operation for URI /pizza.",
										"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			} else{
					// the request URI is: /pizza/username/{username}  -> list all the pizza of the selected user
					if (path.contains("username")) {
						path = path.substring(path.lastIndexOf("username") + 8);

						if (path.length() == 0 || path.equals("/")) {
							m = new Message("Wrong format for URI /pizza/username/{username}: no {username} specified.",
											"E4A7", String.format("Requesed URI: %s.", req.getRequestURI()));
							res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							m.toJSON(res.getOutputStream());
						} else {

							switch (method) {
								case "GET":
									new PizzaRestResource(req, res, getDataSource().getConnection()).readPizzaCatalogueUser();
									break;

								default:
									m = new Message("Unsupported operation for URI /pizza/username/{username}.",
													"E4A5", String.format("Requested operation %s.", method));
									res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
									m.toJSON(res.getOutputStream());
									break;
							}
						}
					}

			}

        }
        catch(Throwable t) {
                m = new Message("Unexpected error.", "E5A1", t.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

		return true;
	    }
	return false;
	}

	/**
	 * Checks whether the request if for a {@link Order}resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Order}; {@code false} otherwise.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean processOrder(HttpServletRequest req, HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		if(path.lastIndexOf("rest/order") > 0){
		try {

			// strip everyhing until after the /order
			path = path.substring(path.lastIndexOf("order") + 5);
			// the request URI is: /order
			// if method GET, list all the orders in the catalogue
			if (path.length() == 0 || path.equals("/")) {
				switch (method) {
					case "GET":
						new OrdersRestResource(req, res, getDataSource().getConnection()).readOrders();
						break;
					case "POST":
						new OrdersRestResource(req, res, getDataSource().getConnection()).insertOrder();
						break;
					default:
						m = new Message("Unsupported operation for URI /order.",
										"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			} else{
				// the request URI is: /order/id/{idorder}  -> list all the data for id order
				if (path.contains("id")) {
					path = path.substring(path.lastIndexOf("id") + 2);

					if (path.length() == 0 || path.equals("/")) {
						m = new Message("Wrong format for URI /order/id/{idorder}: no {idorder} specified.",
										"E4A7", String.format("Requesed URI: %s.", req.getRequestURI()));
						res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						m.toJSON(res.getOutputStream());
					} else {

						switch (method) {
							case "GET":
								// it can be implemented in order to add functionalities
								break;

							case "POST":
								// alternative solution..
								// new OrdersRestResource(req, res, getDataSource().getConnection()).updateOrderStatus();
								break;

							case "PUT":
								// it can be implemented in order to add functionalities
								break;

							case "DELETE":
								// it can be implemented in order to add functionalities
								break;

							default:
								m = new Message("Unsupported operation for URI /order/id/{id}.",
												"E4A5", String.format("Requested operation %s.", method));
								res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
								m.toJSON(res.getOutputStream());
								break;
						}
					}
				} else {
						// the request URI is: /order/username/{username}  -> list all the orderd for the customer username
						if (path.contains("username")) {
							path = path.substring(path.lastIndexOf("username") + 8);

							if (path.length() == 0 || path.equals("/")) {
								m = new Message("Wrong format for URI /order/username/{username}: no {username} specified.",
												"E4A7", String.format("Requested URI: %s.", req.getRequestURI()));
								res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								m.toJSON(res.getOutputStream());
							} else {

								switch (method) {
									case "GET":
										// it can be implemented in order to add functionalities
										new OrdersRestResource(req, res, getDataSource().getConnection()).readUserOrders();
										break;

									case "POST":
										// it can be implemented in order to add functionalities
									// new OrdersRestResource(req, res, getDataSource().getConnection()).readUserOrders(); // to be implemented..
										break;

									case "PUT":
										// it can be implemented in order to add functionalities
									// new OrdersRestResource(req, res, getDataSource().getConnection()).readUserOrders();
										break;

									case "DELETE":
										// it can be implemented in order to add functionalities
									// new OrdersRestResource(req, res, getDataSource().getConnection()).readUserOrders();
										break;

									default:
										m = new Message("Unsupported operation for URI /pizza/username/{username}.",
														"E4A5", String.format("Requested operation %s.", method));
										res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
										m.toJSON(res.getOutputStream());
										break;
								}
							}
						}
					}
			}
		} catch(Throwable t) {
							m = new Message("Unexpected error.", "E5A1", t.getMessage());
							res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
							m.toJSON(res.getOutputStream());
					}

			return true;
			}
		return false;
	}


     /**
	 * Checks whether the request if for an {@link Ingredient} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code Ingredient}; {@code false} otherwise.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
		private boolean processIngredient(HttpServletRequest req, HttpServletResponse res) throws IOException {

			final String method = req.getMethod();
			final OutputStream out = res.getOutputStream();

			String path = req.getRequestURI();
			Message m = null;

			if(path.lastIndexOf("rest/ingredient") > 0)
			{
				try {
					switch (method) {
						case "GET":
						new IngredientRestResource(req, res, getDataSource().getConnection()).readIngredient();
						break;

						default:
						m = new Message("Unsupported operation for URI /ingredient.",
							"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
					}
				}
				catch(Throwable t) {
					m = new Message("Unexpected error.", "E5A1", t.getMessage());
					res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					m.toJSON(res.getOutputStream());
				}

				return true;
			}
			return false;
		}

		/**
	* Checks whether the request if for an {@link Coupon} resource and, in case, processes it.
	*
	* @param req the HTTP request.
	* @param res the HTTP response.
	* @return {@code true} if the request was for an {@code Ingredient}; {@code false} otherwise.
	*
	* @throws IOException if any error occurs in the client/server communication.
	*/
	 private boolean processCoupon(HttpServletRequest req, HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		if(path.lastIndexOf("rest/coupon") > 0) {
			try {

				// strip everyhing until after the /coupon
				path = path.substring(path.lastIndexOf("coupon") + 6);

				if (path.length() == 0 || path.equals("/")) {

					switch (method) {
						case "POST":
							new CouponRestResource(req, res, getDataSource().getConnection()).checkCouponUser();
						break;

						default:
						m = new Message("Unsupported operation for URI /coupon.",
							"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
					}
				} else {
				// the request URI is: /coupon/username/{username}  -> list all the coupon for the customer username
					if (path.contains("username")) {
						path = path.substring(path.lastIndexOf("username") + 8);

						if (path.length() == 0 || path.equals("/")) {
							m = new Message("Wrong format for URI /order/username/{username}: no {username} specified.",
											"E4A7", String.format("Requesed URI: %s.", req.getRequestURI()));
							res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							m.toJSON(res.getOutputStream());
						} else {
								switch (method) {
									case "GET":
										// it can be implemented in order to add functionalities
										new CouponRestResource(req, res, getDataSource().getConnection()).readCouponUser();
										break;

									case "POST":
										// it can be implemented in order to add functionalities
										break;

									case "PUT":
										// it can be implemented in order to add functionalities
										break;

									case "DELETE":
										// it can be implemented in order to add functionalities
										break;

									default:
										m = new Message("Unsupported operation for URI /coupon/username/{username}.",
														"E4A5", String.format("Requested operation %s.", method));
										res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
										m.toJSON(res.getOutputStream());
										break;
								}
						}
					}
				}
			} catch(Throwable t) {
					m = new Message("Unexpected error.", "E5A1", t.getMessage());
					res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					m.toJSON(res.getOutputStream());
			}
			return true;
		}
		return false;
	}


	 /**
	 * Checks whether the request if for an {@link deliveryQueue} resource and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for an {@code deliveryQueue}; {@code false} otherwise.
	 *
	 * @throws IOException if any error occurs in the client/server communication.
	 */
	private boolean processDeliveryQueue(HttpServletRequest req, HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		if(path.contains("rest/DeliveryQueue"))
		{
		try {
			// strip everyhing until after the /DeliveryQueue
			path = path.substring(path.lastIndexOf("DeliveryQueue") + 13);

			if (path.length() == 0 || path.equals("/")) {

				switch (method) {
					case "GET":
						break;
					default:
						m = new Message("Unsupported operation for URI /DeliveryQueue.",
										"E4A5", String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			} else{
					// the request URI is: /DeliveryQueue/username/{username}  -> list the queue of the selected user delivery guy
					if (path.contains("username")) {
						path = path.substring(path.lastIndexOf("username") + 8);
						if (path.length() == 0 || path.equals("/")) {
							m = new Message("Wrong format for URI /DeliveryQueue/username/{username}: no {username} specified.",
											"E4A7", String.format("Requesed URI: %s.", req.getRequestURI()));
							res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							m.toJSON(res.getOutputStream());
						} else {

							switch (method) {
								case "GET":
									// it can be implemented in order to add functionalities
									new DeliveryInfoRestResource(req, res, getDataSource().getConnection()).readDeliveryQueue();
									break;

								case "POST":
									// it can be implemented in order to add functionalities
									new DeliveryInfoRestResource(req, res, getDataSource().getConnection()).updateOrderStatusDelivery();
									break;

								default:
									m = new Message("Unsupported operation for URI /DeliveryQueue/username/{username}.",
													"E4A5", String.format("Requested operation %s.", method));
									res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
									m.toJSON(res.getOutputStream());
									break;
							}
						}
					}
			}
        }
        catch(Throwable t) {
                m = new Message("Unexpected error.", "E5A1", t.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
		return true;
   		}

	return false;
	}

	private boolean processCookQueue(HttpServletRequest req, HttpServletResponse res) throws IOException {

		final String method = req.getMethod();
		final OutputStream out = res.getOutputStream();

		String path = req.getRequestURI();
		Message m = null;

		if(path.lastIndexOf("rest/CookQueue") > 0)
		{
			try {
				// strip everyhing until after the /DeliveryQueue
				path = path.substring(path.lastIndexOf("CookQueue") + 9);

				if (path.length() == 0 || path.equals("/")) {

					switch (method) {
						case "GET":
							break;
						default:
							m = new Message("Unsupported operation for URI /CookQueue.",
									"E4A5", String.format("Requested operation %s.", method));
							res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
							m.toJSON(res.getOutputStream());
							break;
					}
				} else{
					// the request URI is: /CookQueue/username/{username}  -> list the queue of the selected user delivery guy
					if (path.contains("username")) {
						path = path.substring(path.lastIndexOf("username") + 8);
						if (path.length() == 0 || path.equals("/")) {
							m = new Message("Wrong format for URI /CookQueue/username/{username}: no {username} specified.",
									"E4A7", String.format("Requesed URI: %s.", req.getRequestURI()));
							res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							m.toJSON(res.getOutputStream());
						} else {

							switch (method) {
								case "GET":
									// it can be implemented in order to add functionalities
									new CookInfoRestResource(req, res, getDataSource().getConnection()).readCookQueue();
									break;

								case "POST":
									// it can be implemented in order to add functionalities
									new CookInfoRestResource(req, res, getDataSource().getConnection()).updateOrderStatusCook();
									break;

								default:
									m = new Message("Unsupported operation for URI /CookQueue/username/{username}.",
											"E4A5", String.format("Requested operation %s.", method));
									res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
									m.toJSON(res.getOutputStream());
									break;
							}
						}
					}
				}
			}
			catch(Throwable t) {
				m = new Message("Unexpected error.", "E5A1", t.getMessage());
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
			return true;
		}
		return false;
	}





	     /**
		 * Checks whether the request if for an {@link /rest/whoami /rest/edit /rest/logout} resource and, in case, processes it.
		 *
		 * @param req the HTTP request.
		 * @param res the HTTP response.
		 * @return {@code true} if the request was for an {@code /rest/whoami /rest/edit /rest/logout}; {@code false} otherwise.
		 *
		 * @throws IOException if any error occurs in the client/server communication.
		 */
			private boolean processUserInformation(HttpServletRequest req, HttpServletResponse res) throws IOException {

				final String method = req.getMethod();
				final OutputStream out = res.getOutputStream();

				String path = req.getRequestURI();
				Message m = null;

				final HttpSession session = req.getSession(false);


				final String user = (String) session.getAttribute("user");
				final String role = (String) session.getAttribute("role");
				final String first = (String) session.getAttribute("first");
				final String last = (String) session.getAttribute("last");




				if(path.lastIndexOf("rest/whoami") > 0)
				{



					if ((session == null) || (user == null))
							return false;

					try {
						switch (method) {
							// in order to retrieve user info
							case "GET":
							User us = new User(user,first,last,null,null,role,null,null);
							us.toJSON(res.getOutputStream());
							break;

							// we use post to delete the session!
							case "POST":
							session.invalidate();
							m = new Message("Got it! Deleting session..");
							m.toJSON(res.getOutputStream());
							break;

							default:
							m = new Message("Unsupported operation for URI /rest/whoami.",
								"E4A5", String.format("Requested operation %s.", method));
							res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
							m.toJSON(res.getOutputStream());
							break;
						}
					}
					catch(Throwable t) {
						m = new Message("Unexpected error.", "E5A1", t.getMessage());
						res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						m.toJSON(res.getOutputStream());
					}

					return true;
				}



				if(path.contains("rest/edit"))
				{
					if ((session == null) || (user == null))
							return false;

					try {
						switch (method) {
							// in order to retrieve user info 
						
							case "GET":
								final User us1 = new User(user,first,last,null,null,role,null,null);
								// write the info to feed the edit profile page in json
								new EditProfileRestResource(req, res, getDataSource().getConnection()).readEditProfileInfo(us1);
							break;

							// we use post to update the user credentials!
							case "POST":
								final User us2 = new User(user,first,last,null,null,role,null,null);
								new EditProfileRestResource(req, res, getDataSource().getConnection()).updateProfile(us2);
							break;


							default:
							m = new Message("Unsupported operation for URI /rest/edit.",
								"E4A5", String.format("Requested operation %s.", method));
							res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
							m.toJSON(res.getOutputStream());
							break;
						}
					}
					catch(Throwable t) {
						m = new Message("Unexpected error.", "E5A1", t.getMessage());
						res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						m.toJSON(res.getOutputStream());
					}

					return true;
				}


				if(path.contains("rest/logout"))
				{

					if ((session == null) || (user == null))
							return false;
					try{
							switch (method) {
								case "GET":
								break;

								// we use post to delete the session!
								case "POST":
								session.invalidate();
								m = new Message("Session invalidated");
								m.toJSON(res.getOutputStream());
								break;

								default:
								m = new Message("Unsupported operation for URI /logout.",
									"E4A5", String.format("Requested operation %s.", method));
								res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
								m.toJSON(res.getOutputStream());
								break;
							}
						}
						catch(Throwable t) {
							m = new Message("Unexpected error.", "E5A1", t.getMessage());
							res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
							m.toJSON(res.getOutputStream());
						}


					return true;
				}
				return false;
			}

}
