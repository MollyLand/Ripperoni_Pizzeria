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
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

import org.json.*;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public final class CouponRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Pizza} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public CouponRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}


    /**
	 * Reads the entire pizza table from the database.
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */
    public void checkCouponUser() throws IOException {

    	List<Coupon> couponList  = null;
    	Message m = null;
    	CouponDAO couponObj = null;

    	try{
			// creates a new object for accessing the database and reads the pizza
			String data = IOUtils.toString(req.getReader());
			JSONObject json = new JSONObject(data);

			Integer coupon = new Integer(json.getInt("idcoupon"));
			String username = json.getString("username");
			boolean found = false;

				couponObj = new CouponDAO(con);
    		couponList = couponObj.findUserCoupon(username);


    		if(couponList != null) {

    			for(int i=0; i<couponList.size(); i++){
						if (couponList.get(i).getID().intValue() == coupon.intValue())
						{
							// found the correct coupon
							//couponObj.updateCoupon(coupon, username);
							res.setStatus(HttpServletResponse.SC_OK);
							found = true;
							couponList.get(i).toJSON(res.getOutputStream(),0,1);
							break;
						}
						//couponList.get(i).toJSON(res.getOutputStream(), i, couponList.size());
					}

					if (!found)
					{
						m = new Message(String.format("Coupon with provided ID not found."), "E5A3", null);
						res.setStatus(HttpServletResponse.SC_NOT_FOUND);
						m.toJSON(res.getOutputStream());
					}
    			//con.close();
    		}
            else {
							m = new Message(String.format("Coupon not found."), "E5A3", null);
							res.setStatus(HttpServletResponse.SC_NOT_FOUND);
							m.toJSON(res.getOutputStream());

    		}
    	}

        catch (Throwable t) {
    		m = new Message("Cannot read Coupon list: unexpected error.", "E5A1", t.getMessage());
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		m.toJSON(res.getOutputStream());
    	}
	}
	

	public void readCouponUser() throws IOException {

    	List<Coupon> couponList  = null;
    	Message m = null;
    	CouponDAO couponObj = null;

    	try{
			// parse the URI path to extract the username
    		String path = req.getRequestURI();
    		path = path.substring(path.lastIndexOf("username") + 8);
    		final String username = path.substring(1);

			// read all the coupons of the user
			couponObj = new CouponDAO(con);
    		couponList = couponObj.findUserCoupon(username);

    		if(couponList != null) {

    			for(int i=0; i<couponList.size(); i++){
    				res.setStatus(HttpServletResponse.SC_OK);
    				couponList.get(i).toJSON(res.getOutputStream(), i, couponList.size());
    			}
    			con.close();
    		} 
            else {
    			m = new Message(String.format("Coupon list of the user not found."), "E5A3", null);
    			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			m.toJSON(res.getOutputStream());
    		}
    	} 
        catch (Throwable t) {
    		m = new Message("Cannot read Coupon list of the user: unexpected error.", "E5A1", t.getMessage());
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		m.toJSON(res.getOutputStream());
    	}
    }
}
