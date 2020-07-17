package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.lang.*;



public final class OrderDAO {

  private static final String TABLE_NAME = "orders";

    //  The connection to the database
    private final Connection con;

    public OrderDAO(Connection con){
        this.con = con;
    }

    public final String getTableName() {
        return TABLE_NAME;
    }

    /*
    * Insert a new order in the system.
    * @param Order
    * @return Order
    * @throws SQLException
    */

    //this method find the IDorder necessary for the insertion of a new order
    public final Integer findNextID() throws  SQLException
    {
        PreparedStatement pstmt = null;
        final Integer NextID;

        final String STATEMENT = "select max(idorder) as nextId from orders";
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            rs = pstmt.executeQuery();

            //assign the IDorder of the next order adding +1 to the current max IDorder
            rs.next();
            NextID = rs.getInt("nextId") + 1;

        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return NextID;
    }


    //this method insert a new row in the order table, it is the first db operation in the insert order process
    //the fields about cook, delivery guy and delivery time are left NULL since they will be calculated further
    public final void insertNewOrder(Order order) throws SQLException
    {

        PreparedStatement pstmt = null;

        final String STATEMENT = "INSERT INTO "+ getTableName() + " (IDorder, DeliverUsername, IDclosing, CustomerUsername, RequestTime, DeliveryTime, OrderStatus, Price) "+
                                                                    " VALUES (?,?,?,?,CAST(? AS TIMESTAMPTZ),CAST(? AS TIMESTAMPTZ),CAST(? AS orderstatus),?)";

        try {
            pstmt = this.con.prepareStatement(STATEMENT);
            pstmt.setInt(1,order.getIDOrder());
            pstmt.setString(2,order.getDeliverUsername());
            pstmt.setInt(3,order.getIDClosing());
            pstmt.setString(4,order.getCustomerUsername());
            pstmt.setTimestamp(5,order.getRequestTime());
            pstmt.setTimestamp(6,order.getDeliveryTime());
            pstmt.setString(7,order.getOrderStatus());
            pstmt.setFloat(8,order.getPrice());
            pstmt.execute();

        }
        finally{
            if (pstmt != null) {
                pstmt.close();
                }
        }

    }

    //this method retreve all the orders for a customer
    public final List<Order> findOrdersUsers() throws SQLException{

      final String STATEMENT = "SELECT * FROM " +getTableName();

      Statement stmt = null;

      ResultSet rs = null;

      final List<Order> listOrders = new ArrayList<Order>();

      try {
        stmt = con.createStatement();

        rs = stmt.executeQuery(STATEMENT);

        while (rs.next()) {
              listOrders.add(new Order(rs.getInt("idOrder"),
              rs.getString("deliverUsername"), rs.getInt("idClosing"),
              rs.getString("customerUsername"), rs.getTimestamp("requestTime"), rs.getTimestamp("deliveryTime"),
              rs.getString("orderStatus"), rs.getFloat("price")));
            }

          }
          finally
            {
              if (rs != null)
              {
                rs.close();
              }

              if (stmt != null)
              {
                stmt.close();
              }
            }

            return listOrders;
    }

    //this method updates the order after a delivery guy has been assigned
    public final void updateDeliveryOrder(final Integer idorder, final String deliveryguy) throws SQLException
    {
        PreparedStatement pstmt = null;

        final String STATEMENT = "UPDATE orders set DeliverUsername = ? where idorder = ?";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, deliveryguy);
            pstmt.setInt(2, idorder);
            pstmt.execute();
        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            // con.close();
        }
    }

    // this method retrieve a list of Orders associated to their customer
    public final List<Order> findOrdersUser(final String username) throws SQLException
    {

      final String STATEMENT = "SELECT * FROM " + getTableName() + " WHERE CustomerUsername = ?";
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      final List<Order> listOrders = new ArrayList<Order>();

      try {
        pstmt = con.prepareStatement(STATEMENT);
        pstmt.setString(1, username);

        rs = pstmt.executeQuery();

        while (rs.next()) {
              listOrders.add(new Order(rs.getInt("idOrder"),
              rs.getString("deliverUsername"), rs.getInt("idClosing"),
              rs.getString("customerUsername"), rs.getTimestamp("requestTime"), rs.getTimestamp("deliveryTime"),
              rs.getString("orderStatus"), rs.getFloat("price")));
            }
          }
          finally
            {
              if (rs != null)
              {
                rs.close();
              }

              if (pstmt != null)
              {
                pstmt.close();
              }
            }
            return listOrders;
    }

    // update the delivery time attribute in the Orders table
     public final void updateOrderDeliveryTime(Integer idorder, Timestamp deliveryTime) throws SQLException {

        PreparedStatement pstmt = null;

        final String STATEMENT = "UPDATE orders set deliverytime = ? where idorder = ?";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setTimestamp(1, deliveryTime);
            pstmt.setInt(2, idorder);
            pstmt.execute();
        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            // con.close();
        }
    }

    //this method apply the percentage discount on a order with a coupon
    // it execute a query for retrieving the original price
    // and update the price based on the percentage discount
    public final void discountOrderFromCoupon(final Integer IDorder, final Integer coupon) throws SQLException
    {

        double oldPrice;
        double newPrice;
        Integer percentage = -1;
        final String STATEMENT = "SELECT percentage FROM coupon WHERE idcoupon = ?";
        final String STATEMENT1 = "SELECT price FROM orders WHERE idorder = ?";
        final String STATEMENT2 = "UPDATE orders set price = ? where idorder = ?";
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        try {
            // lol first first query
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1,coupon);

            rs = pstmt.executeQuery();
            rs.next();
            percentage =rs.getInt("percentage");

            //first query
            pstmt1 = con.prepareStatement(STATEMENT1);
            pstmt1.setInt(1, IDorder);

            rs = pstmt1.executeQuery();

            rs.next();
            oldPrice = rs.getDouble("price");

            // calculate discounted price
            newPrice = oldPrice - oldPrice * percentage / 100;
            //round off to 2 decimal since it's money
            newPrice = Math.round(newPrice * 100.0)/100.0;

            //second query
            pstmt2 = con.prepareStatement(STATEMENT2);
            pstmt2.setDouble(1, newPrice);
            pstmt2.setDouble(2, IDorder);
            pstmt2.execute();
        }
        finally
        {
            if (rs != null)
            {
                rs.close();
            }

            if (pstmt1 != null)
            {
                pstmt1.close();
            }

            if (pstmt2 != null)
            {
                pstmt2.close();
            }

            if (pstmt != null)
            {
              pstmt.close();
            }
        }
    }
  }
