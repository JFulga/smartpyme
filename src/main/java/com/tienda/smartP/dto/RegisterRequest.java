package com.tienda.smartP.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Payload accepted by the public registration endpoint.  Roles are deliberately
 * not part of this contract: a public registration always creates a VENDEDOR.
 */
@JsonIgnoreProperties(value = "role")
public class RegisterRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
