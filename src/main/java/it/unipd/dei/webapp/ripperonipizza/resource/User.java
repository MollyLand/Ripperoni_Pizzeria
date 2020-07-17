/*
 * Copyright 2019-20 Ripperoni
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http: //www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.webapp.ripperonipizza.resource;

import java.util.ArrayList;
import java.util.List;

import java.io.*;
import com.fasterxml.jackson.core.*;
/*
    It describes the object of a single user in the system
*/
public final class User extends Resource {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_PWD = "password";
    public static final String KEY_MAIL = "email";
    public static final String KEY_ROLE = "role";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TELEPHONE = "telephone";

    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final String email;
    private final String accountRole;
    private final String address;
    private final String telephone;

    public User(String username, String firstname, String lastname, String password, String email, String accountRole, String address, String telephone) {
      this.username = username;
      this.firstname = firstname;
      this.lastname = lastname;
      this.password = password;
      this.email = email;
      this.accountRole = accountRole;
      this.address = address;
      this.telephone = telephone;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
      return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountRole() {
        return accountRole;
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
      jg.writeStringField(KEY_USERNAME, getUsername());
      jg.writeStringField(KEY_FIRSTNAME, getFirstName());
      jg.writeStringField(KEY_LASTNAME, getLastName());
      jg.writeStringField(KEY_ROLE, getAccountRole());
      jg.writeEndObject();
      jg.writeRaw("]");
      jg.flush();
    }

    @Override
    public final void toJSON(final OutputStream out, final int i, final int end) throws IOException {}

}
