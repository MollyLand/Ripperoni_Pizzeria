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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public final class IngredientRestResource extends RestResource {

	/**
	 * Creates a new REST resource for managing {@code Pizza} resources.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public IngredientRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(req, res, con);
	}

	
    /**
	 * Reads the entire pizza table from the database.
	 *
	 * @throws IOException
	 *             if any error occurs in the client/server communication.
	 */
    public void readIngredient() throws IOException {

    	List<Ingredient> ingredientList  = null;
    	Message m = null;
    	IngredientDAO ingredientObj = null;

    	try{

			// creates a new object for accessing the database and reads the pizza
    		ingredientObj = new IngredientDAO(con);
    		ingredientList = ingredientObj.allIngredient();

    		if(ingredientList != null) {
    			for(int i=0; i<ingredientList.size(); i++){
    				//ingredientList.get(i).SetIngredients(new ConsistDAO(con, ingredientList.get(i)).listIngredients().toString());
    				//ingredientList.get(i)
    				res.setStatus(HttpServletResponse.SC_OK);
    				ingredientList.get(i).toJSON(res.getOutputStream(), i, ingredientList.size());
    			}
    			con.close();
    		} 
    		else {
    			m = new Message(String.format("Ingredients not found."), "E5A3", null);
    			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    			m.toJSON(res.getOutputStream());
    		}
    	} 
    	catch (Throwable t) {
    		m = new Message("Cannot read ingredients: unexpected error.", "E5A1", t.getMessage());
    		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    		m.toJSON(res.getOutputStream());
    	}
    }

}
