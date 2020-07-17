package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.Pizza;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsistDAO {

    private static final String TABLE_NAME = "consistof";

    //  The connection to the database
    private final Connection con;
    private final Pizza pizza;

    public ConsistDAO(Connection con, Pizza pizza){
        this.con = con;
        this.pizza = pizza;
    }

    public final String getTableName() {
        return TABLE_NAME;
    }

    
    public final List<String> listIngredients() throws SQLException {

        PreparedStatement pstmt = null;

        final String STATEMENT = "SELECT I.ingredientName FROM Ingredient AS I JOIN consistof AS C ON (pizzaname = ? AND I.IDIngredient = C.IDIngredient)";

        ResultSet rs = null;

        List<String> ingredients = new ArrayList<String>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, this.pizza.getPizzaName());
            rs = pstmt.executeQuery();

        while (rs.next()) {
            ingredients.add(rs.getString("ingredientname"));
        }     

        } 
        finally {
            if (rs != null) 
                rs.close();

            if (pstmt != null) 
                pstmt.close();
        }
        return ingredients;
    }   

    //this method insert a new row in the consist table, needed for the pizza creation process
    public final void insertNewConsistOf(Integer ingrID) throws SQLException
    {
        PreparedStatement pstmt = null;

        final String STATEMENT = "INSERT INTO "+ getTableName() + " (PizzaName, IDingredient) VALUES (?,?)";

        try {
            pstmt = this.con.prepareStatement(STATEMENT);
            pstmt.setString(1,this.pizza.getPizzaName());
            pstmt.setInt(2,ingrID);
            pstmt.execute();
        }
        finally{
            if (pstmt != null) 
              pstmt.close();
      }
  } 
}
    