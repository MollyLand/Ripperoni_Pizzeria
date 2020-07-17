package it.unipd.dei.webapp.ripperonipizza.rest;

import it.unipd.dei.webapp.ripperonipizza.database.*;
import it.unipd.dei.webapp.ripperonipizza.resource.*;

import java.io.*;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.*;

public final class DeliveryInfoRestResource extends RestResource {
    /**
     * Creates a new REST resource for managing {@code Pizza} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public DeliveryInfoRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(req, res, con);
    }


    public void readDeliveryQueue() throws IOException {

        List<DeliveryInfo> DeliveryQueue  = null;
        Message m = null;
        DeliveryQueueDAO queueObj = null;

        try{

            // parse the URI path to extract the username
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf("username") + 8);
            final String DeliverUsername = path.substring(1);
            
            // creates a new object for accessing the database and reads the delivery queue
            queueObj = new DeliveryQueueDAO(con);
            DeliveryQueue = queueObj.findDeliveryQueue(DeliverUsername);

            if(DeliveryQueue != null) {
                for(int i=0; i<DeliveryQueue.size(); i++){
                    res.setStatus(HttpServletResponse.SC_OK);
                    DeliveryQueue.get(i).toJSON(res.getOutputStream(), i, DeliveryQueue.size());
                }
                con.close();
            } else {
                m = new Message(String.format("Delivery Queue not found"), "E5A3", null);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                m.toJSON(res.getOutputStream());
            }
        } catch (Throwable t) {
            m = new Message("Cannot read delivery queue: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

    // TO BE IMPLEMENTED
    public void updateOrderStatusDelivery() throws IOException {

        Message m = null;

        String username = null;
        String newstatus = null;
        Integer idorder = null;

        try{
    
            String data = IOUtils.toString(req.getReader());
            JSONObject json = new JSONObject(data);

            idorder = json.getInt("idorder");
            username = json.getString("username");
            newstatus  = json.getString("status");

            new DeliveryQueueDAO(con).updateOrderStatus(idorder);

            m = new Message("In heaven .. everything is fine!");
            res.setStatus(HttpServletResponse.SC_OK);
            m.toJSON(res.getOutputStream());


        } catch (Throwable t) {
            m = new Message("Cannot update order status: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
