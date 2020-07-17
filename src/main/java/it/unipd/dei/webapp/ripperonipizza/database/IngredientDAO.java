package it.unipd.dei.webapp.ripperonipizza.database;

import it.unipd.dei.webapp.ripperonipizza.resource.Ingredient;

import java.sql.Connection;

import java.sql.Statement;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class IngredientDAO {

    private static final String TABLE_NAME = "ingredient";

    //  The connection to the database
    private final Connection con;

    public IngredientDAO(Connection con){
        this.con = con;
    }

    public final String getTableName() {
        return TABLE_NAME;
    }

    
    public final List<Ingredient> allIngredient() throws SQLException {

        Statement stmt = null;

        final String STATEMENT = "SELECT * FROM ingredient";

        ResultSet rs = null;

        final List<Ingredient> listIngredient = new ArrayList<Ingredient>();

        try {
           stmt = con.createStatement();

           rs = stmt.executeQuery(STATEMENT);

           while (rs.next()) {
            listIngredient.add(new Ingredient(rs.getInt("idingredient"), 
                rs.getString("category"), 
                rs.getString("icon"),
                rs.getString("ingredientname"), 
                rs.getFloat("ingredientprice")));
        }     

    } 
    finally {
       if (rs != null) 
        rs.close();

    if (stmt != null)
        stmt.close();
}

return listIngredient;
}

// retrieve the name of an ingredient starting from its ID
public final Integer getIDFromName(String ingredientName) throws SQLException {
        PreparedStatement pstmt = null;

        final String STATEMENT = "SELECT IDingredient FROM ingredient WHERE IngredientName = ?";

        ResultSet rs = null;
        Integer idIngr = 0;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setString(1, ingredientName);
            rs = pstmt.executeQuery();
            rs.next();
            idIngr = rs.getInt("idingredient");         
        }
        finally {
         if (rs != null) 
            rs.close();

        if (pstmt != null)
            pstmt.close();
    }

    return idIngr;
}

// this method return an object Ingredient starting from the ingredient name
public final Ingredient findIngredient(String ingr) throws SQLException {
  PreparedStatement pstmt = null;

  final String STATEMENT = "SELECT * FROM " + getTableName() + "  where ingredientname = ?";

  ResultSet rs = null;
  Ingredient result = null;

  try {
    pstmt = con.prepareStatement(STATEMENT);
    pstmt.setString(1,ingr);
    rs = pstmt.executeQuery();
    rs.next();
    result = new Ingredient(rs.getInt("idingredient"),
        rs.getString("category"), rs.getString("icon"),
        rs.getString("ingredientname"), rs.getFloat("ingredientprice"));
}
finally {
   if (rs != null)
    rs.close();

if (pstmt != null)
    pstmt.close();
}
return result;
}
}

