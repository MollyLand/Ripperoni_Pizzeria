package it.unipd.dei.webapp.ripperonipizza.servlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;
//needed pacgages for hash password
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import it.unipd.dei.webapp.ripperonipizza.resource.User;
import it.unipd.dei.webapp.ripperonipizza.database.UserDAO;
import it.unipd.dei.webapp.ripperonipizza.resource.Message;


public final class InsertNewUserServlet extends AbstractDatabaseServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String username = null;
        String firstname = null;
        String lastname = null;
        String email = null;
        String password = null;
        String role = null;
        String address = null;
        String telephone = null;


        User us = null;
        Message m = null;


        try {
            // Retrieves all the data from the doPost
            username = req.getParameter("username");
            firstname = req.getParameter("first");
            lastname = req.getParameter("last");
            password = encryptThisString(req.getParameter("password"));
            email = req.getParameter("email");
            role = "Customer";
            address = req.getParameter("address");
            telephone = req.getParameter("phone");

            res.setContentType("text/html; charset=utf-8");


            us = new User(username, firstname, lastname, password, email, role, address, telephone);

            new UserDAO(getDataSource().getConnection(), us).insertNewUser();

            m = new Message(String.format("User %s successfully created.", username));
            req.setAttribute("login", "visible");
        } catch (NumberFormatException ex) {
            m = new Message("Cannot create the employee. Invalid input parameters: badge, age, and salary must be integer.",
                    "E100", ex.getMessage());
        } catch (SQLException ex) {
            if (ex.getSQLState().equals("23505")) {
                m = new Message(String.format("Cannot create the user: user %s already exists.", username),
                        "E300", ex.getMessage());
            } else {
                m = new Message("Cannot create the employee: unexpected error while accessing the database." + ex.getSQLState(),
                        "E200", ex.getMessage());
            }
            req.setAttribute("login", "invisible");
        }

// now I have to create the session. Only in this way you can get the pass for the rest of the website
        HttpSession session = req.getSession(true);
        session.setAttribute("user",username);
        session.setAttribute("role", role);
        session.setAttribute("first", firstname);
        session.setAttribute("last", lastname);

        PrintWriter out = res.getWriter();
        req.setAttribute("destinationPage", "html/dashboard.html");
        req.setAttribute("message", m.getMessage()); // This will be available as ${message}
        req.getRequestDispatcher("jsp/intermediate.jsp").include(req, res);
        // out.printf("<h1>%s</h1>", m.getMessage());
        out.flush();
        out.close();

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
