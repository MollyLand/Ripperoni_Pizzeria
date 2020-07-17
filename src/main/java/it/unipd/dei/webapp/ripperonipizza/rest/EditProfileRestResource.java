package it.unipd.dei.webapp.ripperonipizza.rest;

import it.unipd.dei.webapp.ripperonipizza.database.*;
import it.unipd.dei.webapp.ripperonipizza.resource.*;

import java.io.*;
import java.sql.Connection;
//needed packages for hash password
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.json.*;


public final class EditProfileRestResource extends RestResource {
    /**
     * Creates a new REST resource for managing {@code editprofile} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public EditProfileRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(req, res, con);
    }


    public void readEditProfileInfo(User us) throws IOException {

        Message m = null;
        EditProfile edit = null;

        try{
            edit = new UserDAO(con, us).findEditProfileInfo();
            edit.toJSON(res.getOutputStream());

        } catch (Throwable t) {
            m = new Message("Cannot read edit profile info: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


    public void updateProfile(User us) throws IOException {

        Message m = null;

        try{

            String data = IOUtils.toString(req.getReader());
            JSONObject json = new JSONObject(data);

            String username = json.getString("username");
            String firstname = json.getString("firstname");
            String lastname = json.getString("lastname");
            String password = encryptThisString( json.getString("password"));
            String address = json.getString("address");
            String email = json.getString("email");
            String telephone = json.getString("telephone");

            new UserDAO(con, us).updateUserData(username, password, firstname, lastname, email, telephone, address);

            m = new Message("Credentials updated correctly!");
            res.setStatus(HttpServletResponse.SC_OK);
            m.toJSON(res.getOutputStream());

        } catch (Throwable t) {
            m = new Message("Cannot update profile info: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
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