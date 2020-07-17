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


public final class PizzaRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Pizza} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public PizzaRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}

	
    /**
	 * Reads the entire pizza table from the database.
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */
    public void readPizzaCatalogue() throws IOException {

    	List<Pizza> pizzaList  = null;
    	Message m = null;
    	PizzaDAO pizzaObj = null;

    	try{
			// creates a new object for accessing the database and reads the pizza
    		pizzaObj = new PizzaDAO(con);
    		pizzaList = pizzaObj.listPizza();

    		if(pizzaList != null) {
    			for(int i=0; i<pizzaList.size(); i++){
    				pizzaList.get(i).SetIngredients(new ConsistDAO(con, pizzaList.get(i)).listIngredients().toString());
    				res.setStatus(HttpServletResponse.SC_OK);
    				pizzaList.get(i).toJSON(res.getOutputStream(), i, pizzaList.size());
    			}
    			con.close();
    		} 
            else {
    			m = new Message(String.format("Pizza catalogue not found."), "E5A3", null);
    			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			m.toJSON(res.getOutputStream());
    		}
    	} 
        catch (Throwable t) {
    		m = new Message("Cannot read Pizza catalogue: unexpected error.", "E5A1", t.getMessage());
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		m.toJSON(res.getOutputStream());
    	}
    }


    public void readPizzaCatalogueUser() throws IOException {

    	List<Pizza> pizzaList  = null;
    	Message m = null;
    	PizzaDAO pizzaObj = null;

    	try{
			// parse the URI path to extract the username
    		String path = req.getRequestURI();
    		path = path.substring(path.lastIndexOf("username") + 8);
    		final String username = path.substring(1);

			// creates a new object for accessing the database and reads the pizza
    		pizzaObj = new PizzaDAO(con);
    		pizzaList = pizzaObj.listPizzaUser(username);

    		if(pizzaList != null) {
    			for(int i=0; i<pizzaList.size(); i++){
    				pizzaList.get(i).SetIngredients(new ConsistDAO(con, pizzaList.get(i)).listIngredients().toString());
    				res.setStatus(HttpServletResponse.SC_OK);
    				pizzaList.get(i).toJSON(res.getOutputStream(), i, pizzaList.size());
    			}
    			con.close();
    		} 
            else {
    			m = new Message(String.format("Pizza catalogue of the user not found."), "E5A3", null);
    			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			m.toJSON(res.getOutputStream());
    		}
    	} 
        catch (Throwable t) {
    		m = new Message("Cannot read Pizza catalogue of the user: unexpected error.", "E5A1", t.getMessage());
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		m.toJSON(res.getOutputStream());
    	}
    }

    public void insertNewPizza() throws IOException {
        Message m = null;
        
        try
        {
            String data = IOUtils.toString(req.getReader());
            JSONObject json = new JSONObject(data);

            String pizzaName = json.getString("pizzaname");
            String customer = json.getString("username");
            double totalPrice = 3.5; 
            Timestamp creationTime = new Timestamp(System.currentTimeMillis());
            Integer success = 0;
            JSONArray arrayIngredients = json.getJSONArray("ingredients");
            ArrayList<String> ingredients = new ArrayList<String>();

            IngredientDAO objIngr = new IngredientDAO(con);

            for(int i = 0; i < arrayIngredients.length(); i++)
            {
                double price = objIngr.findIngredient(arrayIngredients.getString(i)).getCumPrice();
                ingredients.add(arrayIngredients.getString(i));
                totalPrice += price;
            }

            Float price = new Float(totalPrice);

			PizzaDAO objPizza = new PizzaDAO(con);
			
			
			if(objPizza.checkIfThisPizzaIsNew(ingredients)){

				Pizza newPizzaCreated = new Pizza(pizzaName,customer,price,creationTime,success);
				objPizza.insertNewCustomPizza(newPizzaCreated);
	
				ConsistDAO objConsist = new ConsistDAO(con,newPizzaCreated);
	
				for(int i=0; i < arrayIngredients.length(); i++) {
					ingredients.add(arrayIngredients.getString(i));
					IngredientDAO tempIngredient = new IngredientDAO(con);
					Integer tempID = tempIngredient.getIDFromName(ingredients.get(i));
					objConsist.insertNewConsistOf(tempID); 
				}   

				m = new Message("If you see this message, then everything is GOOOOOD!!");

			} else {

				m = new Message("Bad choice :( \n Hey this pizza is alredy in our catalogue, try being more creative!");
			}

			m.toJSON(res.getOutputStream());

            con.close();
        }

        catch (Throwable t) {
            m = new Message("Cannot read new pizza inserted: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
	}

}
