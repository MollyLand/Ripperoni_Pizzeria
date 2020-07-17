package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.Pizza;

import java.sql.Connection;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public final class PizzaDAO {

  private static final String TABLE_NAME = "pizza";

    //  The connection to the database
  private final Connection con;

  public PizzaDAO(Connection con){
    this.con = con;
  }

  public final String getTableName() {
    return TABLE_NAME;
  }


  public final List<Pizza> listPizza() throws SQLException {

    Statement stmt = null;

    final String STATEMENT = "SELECT * FROM pizza";

    ResultSet rs = null;

    final List<Pizza> listPizza = new ArrayList<Pizza>();

    try {
     stmt = con.createStatement();

     rs = stmt.executeQuery(STATEMENT);


     while (rs.next()) {
      listPizza.add(new Pizza(rs.getString("name"),
        rs.getString("username"), rs.getFloat("price"),
        rs.getTimestamp("creationdate"), rs.getInt("success")) );
    }

  } 
  finally {
   if (rs != null) 
    rs.close();

  if (stmt != null)
    stmt.close();
}

return listPizza;
}


    // list all the pizza created by the selected user
public final List<Pizza> listPizzaUser(final String username) throws SQLException {

  PreparedStatement pstmt = null;

  final String STATEMENT = "SELECT * FROM "+getTableName()+" where username = ?";

  ResultSet rs = null;

  final List<Pizza> listPizza = new ArrayList<Pizza>();

  try {
   pstmt = con.prepareStatement(STATEMENT);

   pstmt.setString(1, username);
   rs = pstmt.executeQuery();

   while (rs.next()) {
    listPizza.add(new Pizza(rs.getString("name"),
      rs.getString("username"), rs.getFloat("price"),
      rs.getTimestamp("creationdate"), rs.getInt("success")) );
  }

} 
finally {
 if (rs != null) 
  rs.close();

if (pstmt != null) 
  pstmt.close();
}

return listPizza;
}

// retrieve a pizza object from its name
public final Pizza findPizza(String pizza) throws SQLException {
  PreparedStatement pstmt = null;

  final String STATEMENT = "SELECT * FROM " + getTableName() + "  where name = ?";

  ResultSet rs = null;
  Pizza result = null;

  try {

    pstmt = con.prepareStatement(STATEMENT);
    pstmt.setString(1,pizza);
    rs = pstmt.executeQuery();
    rs.next();
    result = new Pizza(rs.getString("name"),
      rs.getString("username"), rs.getFloat("price"),
      rs.getTimestamp("creationdate"), rs.getInt("success"));
  }
  finally {
   if (rs != null)
    rs.close();

  if (pstmt != null)
    pstmt.close();
}
return result;
}

//update the pizza table in order to increase the success
public final void increaseSuccess(String pizza, Integer increment) throws SQLException {

  PreparedStatement pstmt = null;
  ResultSet rs = null;
  Pizza target = this.findPizza(pizza);

  final String STATEMENT = "UPDATE " + getTableName() + " SET success = ? + ? where name = ?";

  try{
    pstmt = con.prepareStatement(STATEMENT);
    pstmt.setInt(1,increment);
    pstmt.setInt(2,target.getSuccess());
    pstmt.setString(3,pizza);
    boolean end = pstmt.execute();
  }
  finally {
   if (rs != null) 
    rs.close();

  if (pstmt != null) 
    pstmt.close();
}
}

// insert a new row in the pizza table
public final void insertNewCustomPizza(Pizza newpizza) throws SQLException
{

  PreparedStatement pstmt = null;

  final String STATEMENT = "INSERT INTO "+ getTableName() + " (Name, Username, Price, CreationDate, Success) VALUES (?,?,?,?,?)";

  try {
    pstmt = this.con.prepareStatement(STATEMENT);
    pstmt.setString(1,newpizza.getPizzaName());
    pstmt.setString(2,newpizza.getUsername());
    pstmt.setFloat(3,newpizza.getPrice());
    pstmt.setTimestamp(4,newpizza.getCreationDate());
    pstmt.setInt(5,newpizza.getSuccess());
    pstmt.execute();

  }
  finally{
    if (pstmt != null) 
      pstmt.close();
  }
}

    //check if a pizza in creation has the same ingredient as an another pizza
    // if return TRUE then you can insert the new pizza,
    // if return FALSE there is already a pizza with those ingredients
    public final boolean checkIfThisPizzaIsNew(List<String> listIngredients) throws SQLException {
        Statement stmt = null;
        PreparedStatement pstmt = null;
        boolean isNew = true;
        int pizzaSize = listIngredients.size();
        String foundPizza = null;
        List<String> foundListIngredients = new ArrayList<String>();

        final String STATEMENT = "select name from pizza";
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(STATEMENT);

            while (rs.next()) {
                foundPizza = (rs.getString("name"));
                foundListIngredients = new ConsistDAO(con, new Pizza(foundPizza, null, null, null, null)).listIngredients();

                if ( pizzaSize == foundListIngredients.size()){
                  isNew = !listIngredients.containsAll(foundListIngredients);
                  break;
                }

            }

        } finally {
            if (rs != null)
                rs.close();

            if (stmt != null)
                stmt.close();
            // con.close();
        }
        return isNew;
    }
}
