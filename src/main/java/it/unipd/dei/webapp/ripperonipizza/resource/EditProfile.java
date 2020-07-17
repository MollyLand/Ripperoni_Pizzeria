package it.unipd.dei.webapp.ripperonipizza.resource;

import java.io.*;
import com.fasterxml.jackson.core.*;
/*
    It describes the object of a profile in the system
*/
public final class EditProfile extends Resource {
    public static final String KEY_PWD = "password";
    public static final String KEY_MAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TELEPHONE = "telephone";


    private final String password;
    private final String email;
    private final String address;
    private final String telephone;

    public EditProfile(String password, String email, String address, String telephone) {
      this.password = password;
      this.email = email;
      this.address = address;
      this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
      return address;
    }

    public String getTelephone() {
      return telephone;
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {

      final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
      jg.writeRaw("[");
      jg.writeStartObject();
      jg.writeStringField(KEY_MAIL, getEmail());
      jg.writeStringField(KEY_ADDRESS, getAddress());
      jg.writeStringField(KEY_TELEPHONE, getTelephone());
      jg.writeEndObject();
      jg.writeRaw("]");
      jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {}

}
