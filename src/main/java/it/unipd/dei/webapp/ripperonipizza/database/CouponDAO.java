package it.unipd.dei.webapp.ripperonipizza.database;
import it.unipd.dei.webapp.ripperonipizza.resource.Coupon;

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
 this class aims to retrieve info about the coupon
 **/

public final class CouponDAO {
    /**
     * The connection to the database
     */
    private final Connection con;
    public CouponDAO(Connection con) { this.con = con; }

    //this method return the coupon objects assigned to a customer in order to display it in the dashboard
    public final List<Coupon> findUserCoupon(String username) throws SQLException {

        PreparedStatement pstmt = null;

        final String STATEMENT = "SELECT * FROM coupon where username = ?";
        ResultSet rs = null;
        final List<Coupon> CouponList = new ArrayList<Coupon>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CouponList.add(new Coupon(rs.getInt("idcoupon"), rs.getString("username"),
                        rs.getInt("percentage"),
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
        return CouponList;
    }

    //this method insert a new row in the Coupon table from a Coupon object
    public final void insertCoupon(final Coupon newcoupon) throws SQLException
    {
        PreparedStatement pstmt = null;

        final String STATEMENT = "INSERT INTO coupon VALUES (?, ?, ?, ?)";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, newcoupon.getID());
            pstmt.setString(2, newcoupon.getUsername());
            pstmt.setInt(3, newcoupon.getPercentage());
            pstmt.setInt(4, newcoupon.getOrder());
            pstmt.execute();

        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            // con.close();
        }
    }
    //check for IDcoupon and customer Username for deleting a coupon after it has been used
    public void updateCoupon(final Integer idcoupon, final String username, final Integer idorder) throws SQLException{

        PreparedStatement pstmt = null;

        final String STATEMENT = "UPDATE coupon SET idorder = ? where (idcoupon = ? AND username = ?) " ;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(2, idcoupon);
            pstmt.setString(3, username);
            pstmt.setInt(1,idorder);
            pstmt.executeUpdate();

        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            //con.close();
        }
    }


}
