package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.CookInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 this class aims to retrieve the orders assigned to a specific cook and can update the OrderStatus.
 **/

public final class CookQueueDAO{

    /**
     * The connection to the database
     */
    private final Connection con;

    public CookQueueDAO(Connection con){
        this.con = con;
    }

    //retrieve the list of pizzas assigned to a cook
    public final List<CookInfo> findCookQueue(String CookUsername) throws SQLException {

        PreparedStatement pstmt = null;

        final String STATEMENT = "SELECT  distinct O.idorder, C.pizzaname, C.quantity "+
        "FROM cook inner join Orders AS O "+
        "ON cook.idorder = O.idorder "+
        "INNER JOIN Contain AS C "+
        "ON  O.idorder = C.idorder "+
        "INNER JOIN Pizza AS P "+
        "ON C.pizzaname = P.name "+
        "INNER JOIN Consistof AS CO "+
        "ON P.name = CO.pizzaname "+
        "WHERE cook.username = ? "+
        " and (o.orderstatus = 'InCharge' or o.orderstatus ='Baking') ";
        ResultSet rs = null;
        final List<CookInfo> CookQueue = new ArrayList<CookInfo>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, CookUsername);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CookQueue.add(new CookInfo(rs.getInt("idorder"), rs.getString("pizzaname"),
                        rs.getInt("quantity")));
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
        return CookQueue;
    }

    // update the order status to 'Delivering'
    // this method is used to mark the pizzas that are ready to be inserted in the delivery guy queue
    public void updateOrderStatus(final Integer idorder) throws SQLException{

        PreparedStatement pstmt = null;

        final String STATEMENT = "UPDATE orders set orderstatus = 'Delivering' where idorder = ?";

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

    //this method retrieve the username of the cook who has the minimum numnber of pizzas in his queue
    public final String chooseCookOrder() throws SQLException
    {
        Statement stmt = null;
        final String chosenCook;
        final String STATEMENT = "select cook.username, sum(quantity) as numpizzas " +
                "    from cook inner join orders as o " +
                "        on cook.idorder = O.idorder " +
                "        inner join Contain as C " +
                "        on C.idorder = O.idorder " +
                "    where (O.orderstatus = 'InCharge' or O.orderstatus = 'Baking') " +
                "       group by cook.username " +
                "    order by numpizzas asc " +
                "    limit 1";
        ResultSet rs = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(STATEMENT);

            rs.next();
            chosenCook = rs.getString("username");
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
        return chosenCook;
    }

    //retrieve the number of pizzas in the queue of a certain cook
    public final Integer chosenCookNumpizzas(final String CookUsername) throws SQLException
    {
        PreparedStatement pstmt = null;
        final Integer numpizzas;

        final String STATEMENT = "\n" +
                " select sum(quantity) as numpizzas \n" +
                "    from cook inner join orders as o \n" +
                "        on cook.idorder = O.idorder \n" +
                "        inner join Contain as C \n" +
                "        on C.idorder = O.idorder \n" +
                "    where ((O.orderstatus = 'InCharge' or O.orderstatus = 'Baking') and cook.username = ?)";
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, CookUsername);
            rs = pstmt.executeQuery();

            rs.next();
            numpizzas = rs.getInt("numpizzas");
            }

        finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return numpizzas;

    }
    // this method insert a new row in the Cook table, so it assigns a cook to an order
    public final void insertCookOrder(final Integer idorder, final String cookusername) throws SQLException
    {
        PreparedStatement pstmt = null;

        final String STATEMENT = "INSERT INTO cook VALUES (?, ?, ?)";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, cookusername);
            pstmt.setInt(2, 1);     //not an interesting feature to be implemented at this point. Find the IDoven assigned to the cook
            pstmt.setInt(3, idorder);
            pstmt.execute();

        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            // con.close();
        }
    }

    //find the oven capacity of an oven associated with a cook
    public final Integer findOvenCapacity(final String CookUsername) throws SQLException {
        PreparedStatement pstmt = null;
        final Integer capacity;

        final String STATEMENT = "    select O.capacity FROM " +
                "        cook as C INNER JOIN oven as O " +
                "        ON C.idoven = o.idoven " +
                "        where c.username = ? ";
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, CookUsername);
            rs = pstmt.executeQuery();

            rs.next();
            capacity = rs.getInt("capacity");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return capacity;

    }



}
