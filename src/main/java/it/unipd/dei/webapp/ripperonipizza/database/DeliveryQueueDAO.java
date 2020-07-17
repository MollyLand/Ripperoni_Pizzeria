package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.DeliveryInfo;

// import java.security.Timestamp;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Time;

/**
this class aims to retrieve the orders assigned to a specific delivery guy and can update the OrderStatus.
**/

public final class DeliveryQueueDAO{

    /**
     * The connection to the database
     */
    private final Connection con;

    public DeliveryQueueDAO(Connection con){
        this.con = con;
    }

    public final List<DeliveryInfo> findDeliveryQueue(String DeliverUsername) throws SQLException {

        PreparedStatement pstmt = null; 
        // here we retrieve the surname of the client, the delivery time, how many pizzas in the order
        // for the orders assigned at the input delivery guy username and the Delivering status
        final String STATEMENT = "SELECT U.LastName, U.Address, O.DeliveryTime, SUM(C.Quantity) AS NumPizzas, O.idorder " +
                "FROM orders AS O INNER JOIN users AS U " +
                    "ON O.CustomerUsername = U.Username " +
                "INNER JOIN contain AS C " +
                    "ON O.IDorder = C.IDorder " +
                "WHERE (O.OrderStatus = 'Delivering' AND O.DeliverUsername = ?) " + 
                "GROUP BY U.LastName, O.DeliveryTime, U.address, O.idorder " +
                "ORDER BY O.DeliveryTime ASC";
        ResultSet rs = null;
        final List<DeliveryInfo> DeliveryQueue = new ArrayList<DeliveryInfo>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, DeliverUsername);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DeliveryQueue.add(new DeliveryInfo(rs.getString("lastname"), rs.getString("address"), 
                                                    rs.getTimestamp("deliverytime"), rs.getInt("numpizzas"),
                                                    rs.getInt("idorder") ));
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return DeliveryQueue;
    }

    // update the order status to 'Delivered'
    public void updateOrderStatus(final Integer idorder) throws SQLException{

        PreparedStatement pstmt = null; 

        final String STATEMENT = "UPDATE orders set orderstatus = 'Delivered' where idorder = ?";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, idorder);
            pstmt.executeUpdate();

        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            con.close();
        }        
    }

    //this method retrieve the username of the delivery guy who has the minimum numnber of pizzas in his queue
    public final String chooseDeliveryOrder() throws SQLException
    {
        Statement stmt = null;
        final String chosenDelivery;
        final String STATEMENT = "select O.deliverusername, sum(C.quantity) as numpizzas " +
                                   " from Contain AS C inner join orders as o  "+
                                       " on C.idorder = O.idorder " +
                                   " where (O.orderstatus = 'Delivering' or O.orderstatus = 'InCharge' or O.orderstatus = 'Baking') "+
                                    " and deliverusername is not null " +
                                       " group by O.deliverusername  " +
                                   " order by numpizzas asc "+
                                   " limit 1";
        
        ResultSet rs = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(STATEMENT);

            rs.next();
            chosenDelivery = rs.getString("deliverusername");
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            // con.close();
        }
        return chosenDelivery;
    }


    // this method retrieve the predicted delivery time of the last delivery in the queue
    // it is used to predict the delivery time of new order
    public final Timestamp findNextDeliveryTime(final String deliveryUsername) throws SQLException
    {
        PreparedStatement pstmt = null;
        Timestamp deliverytime = null;
        // no more hardcoded time interval
        //final String STATEMENT = "select max(deliverytime) + '30 minutes'::interval as time from orders where deliverusername = ?";
        final String STATEMENT = "select max(deliverytime) as time from orders where deliverusername = ?";
        ResultSet rs = null;
        try{
            pstmt = con.prepareStatement(STATEMENT);
            // pstmt.setInt(1, minutes);
            pstmt.setString(1, deliveryUsername);
            rs = pstmt.executeQuery();
            rs.next();
            deliverytime = rs.getTimestamp("time");
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            // con.close();
        }
        return deliverytime;
    }








}