package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.User;
import it.unipd.dei.webapp.ripperonipizza.resource.EditProfile;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class UserDAO {

  private static final String TABLE_NAME = "users";

	/**
	 * The connection to the database
	 */
	private final Connection con;
  private final User user;

  public UserDAO(Connection con, User user){
    this.con = con;
    this.user = user;
  }

  public final String getTableName() {
    return TABLE_NAME;
  }

 /*
 * Insert a new user in the system.
 *
 * @param User
 * @return User
 * @throws SQLException
 */
  public final void insertNewUser() throws SQLException
  {

    PreparedStatement pstmt = null;

    final String STATEMENT = "INSERT INTO "+ getTableName() + " (Username, FirstName, LastName, Password, Role, Mail, Telephone, Address) VALUES (?,?,?,?,CAST(? AS role),?,?,?)";
    // boolean rs;


    try {
			   pstmt = this.con.prepareStatement(STATEMENT);
         pstmt.setString(1,this.user.getUsername());
         pstmt.setString(2,this.user.getFirstName());
         pstmt.setString(3,this.user.getLastName());
         pstmt.setString(4,this.user.getPassword());
         pstmt.setString(5,this.user.getAccountRole());
         pstmt.setString(6,this.user.getEmail());
         pstmt.setString(7,this.user.getTelephone());
         pstmt.setString(8,this.user.getAddress());
         pstmt.execute();

       }
         finally
            {
                //if (rs != null) {
                //  rs.close();
                // }
            if (pstmt != null) {
                  pstmt.close();
                }
            }

	}


	  // retrieve all the users
      public final List<User> selectUsers () throws SQLException {

        PreparedStatement pstmt = null;
        final String STATEMENT = "SELECT * FROM " + getTableName() + " WHERE (username = ? AND password = ?) OR (mail  = ? AND password = ?)";

        ResultSet rs = null;
        final List<User> allUsers= new ArrayList<User>();

        try {
             pstmt = con.prepareStatement(STATEMENT);
             pstmt.setString(1,user.getUsername());
             pstmt.setString(2,user.getPassword());
             pstmt.setString(3,user.getEmail());
             pstmt.setString(4,user.getPassword());
             rs = pstmt.executeQuery();
             while (rs.next()) {
               allUsers.add(new User(rs.getString("username"), rs.getString("firstname"),rs.getString("lastname"),rs.getString("password"),rs.getString("mail"),rs.getString("role"),rs.getString("address"),rs.getString("telephone")));
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

  		return allUsers;
  	}

  	// retrieve the role of a user
    public final String findEmployeeRole(String username) throws SQLException {

      final String statement = "select role from users where username = ?";
      PreparedStatement pstmt = null;
      String role = null;
      ResultSet rs = null;

      try{

        pstmt = con.prepareStatement(statement);
        pstmt.setString(1, username);
        rs = pstmt.executeQuery();
        if(rs.next())
          role = rs.getString("role");

      } finally{
        if (rs != null) {
  				rs.close();
  			}

  			if (pstmt != null) {
  				pstmt.close();
  			}

        // con.close();
      }
      return role;
    }

    //update the user table using string, not the User object
    public void updateUserData(String username, String password, String firstname, String lastname, String email, String telephone, String address) throws SQLException
    {
        PreparedStatement pstmt = null;

        final String STATEMENT = "UPDATE users SET " +
                "firstname = ? ," +
                "lastname = ? ," +
                "mail = ? ," +
                "telephone = ? ," +
                "address = ? ," +
                "password = ? " +
                "where username = ?";

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, email);
            pstmt.setString(4, telephone);
            pstmt.setString(5, address);
            pstmt.setString(6, password);
            pstmt.setString(7, username);
            pstmt.executeUpdate();

        }

        finally {
            if (pstmt != null) {
                pstmt.close();
            }
            con.close();
        }
    }

    //this method check if a username is already taken or it is free
    //it returns true if the username is free, false if it is already taken
    public boolean checkIfThisUsernameIsNew(String username) throws SQLException
    {
        Statement stmt = null;
        final String STATEMENT = "SELECT username FROM users";
        boolean isNew = true;
        ResultSet rs = null;
        List<String> takenNames = new ArrayList<String>();
        try
        {
            stmt = con.createStatement();
            rs = stmt.executeQuery(STATEMENT);

            while(rs.next())
            {
                takenNames.add(rs.getString("username"));
            }
            if (takenNames.contains(username))
                isNew = false;

        }
        finally
        {
            if (rs != null)
                rs.close();

            if (stmt != null)
                stmt.close();
            // con.close();
        }
        return isNew;
    }
    
    // find the tunable credentials to be shown in the edit profile web page
    public final EditProfile findEditProfileInfo() throws SQLException {

      final String statement = "select mail, telephone, address from users where username = ?";
      PreparedStatement pstmt = null;
      String email = null;
      String telephone = null;
      String address = null;
      ResultSet rs = null;
      EditProfile oldCredentials = null;

      try{

        pstmt = con.prepareStatement(statement);
        pstmt.setString(1, this.user.getUsername());
        rs = pstmt.executeQuery();
        if(rs.next())
          email = rs.getString("mail");
          telephone = rs.getString("telephone");
          address = rs.getString("address");
          oldCredentials = new EditProfile(null, email, address, telephone);

      } finally{
        if (rs != null) {
  				rs.close();
  			}

  			if (pstmt != null) {
  				pstmt.close();
  			}

        con.close();
      }
      return oldCredentials;
    }

    
}
