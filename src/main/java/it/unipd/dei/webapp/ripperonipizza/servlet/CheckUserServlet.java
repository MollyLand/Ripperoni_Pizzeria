package it.unipd.dei.webapp.ripperonipizza.servlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.sql.SQLException;
//needed packages for hash password
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unipd.dei.webapp.ripperonipizza.resource.User;
import it.unipd.dei.webapp.ripperonipizza.database.UserDAO;
import it.unipd.dei.webapp.ripperonipizza.resource.Message;


public final class CheckUserServlet extends AbstractDatabaseServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

       String username = null;
       String firstname =  null;
       String lastname = null;
       String email = null;
       String password = null;
       String role = null;
       String address = null;
       String telephone = null;


       User us = null;
       Message m = null;


       try{
         // Retrieves all the data from the doPost
         username = req.getParameter("id");
         password = encryptThisString( req.getParameter("password"));
         email = req.getParameter("id");

         res.setContentType("text/html; charset=utf-8");


         us = new User(username, firstname, lastname, password, email, role, address, telephone);
         List<User> users = new UserDAO(getDataSource().getConnection(), us).selectUsers();
         int numberUsers = users.size();

         if (numberUsers == 1)
         {
           m = new Message(String.format("User %s found. Role = %s", users.get(0).getUsername(), users.get(0).getAccountRole()));
           String obtainedRole = users.get(0).getAccountRole();
           req.setAttribute("login", "visible");
           if (obtainedRole.equals("Customer") || obtainedRole.equals("Admin"))
           {
            req.setAttribute("destinationPage", "html/dashboard.html");
           }
              else if (obtainedRole.equals("Cook")) {
                        req.setAttribute("destinationPage", "html/cook_dash.html");
                  }
                  else {
                        req.setAttribute("destinationPage", "html/deliver_dash.html");
                  }
           // now I have to create the session. Only in this way you can get the pass for the rest of the website
           HttpSession session = req.getSession(true);
           session.setAttribute("user",users.get(0).getUsername());
           session.setAttribute("role", users.get(0).getAccountRole());
           session.setAttribute("first", users.get(0).getFirstName());
           session.setAttribute("last", users.get(0).getLastName());

         }
         else
         {
           m = new Message(String.format("User %s NOT found.", username));
           req.setAttribute("login", "invisible");
           req.setAttribute("destinationPage", "#");
         }
        }
         catch (NumberFormatException ex) {
            m = new Message("Cannot find the user. Invalid input parameters",
                    "E100", ex.getMessage());
        } catch (SQLException ex) {
                m = new Message("Unexpected error while accessing the database." + ex.getSQLState(),
                        "E200", ex.getMessage());
       }




      req.setAttribute("message", m.getMessage());
      req.getRequestDispatcher("jsp/intermediate.jsp").include(req, res);
      //PrintWriter out = res.getWriter();
      //out.printf("<h1>%s</h1>", m.getMessage());
      //out.flush();
      //out.close();

}

    // method for the password hashing
    private static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
