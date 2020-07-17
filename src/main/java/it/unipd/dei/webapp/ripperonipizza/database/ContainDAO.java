package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;

public final class ContainDAO{
    private static final String TABLE_NAME = "contain";

    /**
     * The connection to the database
     */
    private final Connection con;

    public ContainDAO(Connection con){
        this.con = con;
    }

    public final String getTableName() {
        return TABLE_NAME;
    }

    /*
     * Insert a new contain
     *
     * @param User
     * @return User
     * @throws SQLException
     */

    //insert a row in the Contain table, it is needed during the order creation process
    public final void insertNewContain(Integer IDorder, String PizzaName, Integer Quantity) throws SQLException
    {

        PreparedStatement pstmt = null;

        final String STATEMENT = "INSERT INTO " + getTableName() + " (IDorder, PizzaName, Quantity) VALUES(?,?,?)";


        try {
            pstmt = this.con.prepareStatement(STATEMENT);
            pstmt.setInt(1, IDorder);
            pstmt.setString(2, PizzaName);
            pstmt.setInt(3, Quantity);
            pstmt.execute();

        }
        finally
        {
            if (pstmt != null) {
                pstmt.close();
            }
        }

    }

    //retrieve the list of pizzas in a order
    public final Map<String, Integer> orderContain(Integer IDorder) throws SQLException
    {

        PreparedStatement pstmt = null;

        final String STATEMENT = "SELECT pizzaname, quantity FROM contain WHERE IDorder = ?";
        ResultSet rs = null;
        final Map<String, Integer> contain = new HashMap<String, Integer>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, IDorder);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                contain.put(rs.getString("pizzaname"), rs.getInt("quantity"));
            }
        }
        finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

            con.close();
        }

        return contain;
    }

}