package it.unipd.dei.webapp.ripperonipizza.filter;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Base64;
import java.io.PrintWriter;
import it.unipd.dei.webapp.ripperonipizza.resource.User;
import it.unipd.dei.webapp.ripperonipizza.database.UserDAO;
import it.unipd.dei.webapp.ripperonipizza.resource.Message;
import java.util.List;

import java.sql.*;


/**
 * Checks for successful authentication to allow for accessing protected resources.
 *
 * @author Nicola Ferro
 * @version 1.00
 * @since 1.00
 */
public class ProtectedResourceFilter implements Filter {

    /**
     * The Base64 Decoder
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * The name of the user attribute in the session
     */
    private static final String USER_ATTRIBUTE = "user";

    /**
     * The configuration for the filter
     */
    private FilterConfig config = null;

    /**
     * The connection pool to the database.
     */
    private DataSource ds;



    @Override
    public void init(final FilterConfig config) throws ServletException {
        if (config == null) {
            throw new ServletException("Filter configuration cannot be null.");
        }
        this.config = config;

        // the JNDI lookup context
        InitialContext cxt;

        try {
            cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/ripperoni");
        } catch (NamingException e) {
            ds = null;

            throw new ServletException(
                    String.format("Impossible to access the connection pool to the database: %s", e.getMessage()));
        }
    }



    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain chain) throws IOException, ServletException {


        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("Only HTTP requests/responses are allowed.");
        }

        // Safe to downcast at this point.
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;
        PrintWriter out = res.getWriter();
        final HttpSession session = req.getSession(false);
        
        // if we do not have a session, go to home page
        if (session == null){
          sendAuthenticationChallenge(res);
          return;
        }
          else {
                //if we have a sessione, try to extract information, checking if they are valid, and finally autenticate
                final String user = (String) session.getAttribute(USER_ATTRIBUTE);
                final String role = (String) session.getAttribute("role");

                // there might exist a session but without any user in it. Go to home page
                if (user == null || user.trim().isEmpty())
                {
                    session.invalidate();
                    sendAuthenticationChallenge(res);
                    return;
                }

              }

        // the user is properly authenticated and in session, continue the processing
        chain.doFilter(servletRequest, servletResponse);

    }



    @Override
    public void destroy() {
        config = null;
        ds = null;
    }



    /**
     * Sends to the home page
     *
     * @param res the HTTP servlet response.
     * @throws IOException if anything goes wrong while sending the authentication challenge.
     */
    private void sendAuthenticationChallenge(HttpServletResponse res) throws IOException,ServletException {

        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }


}
