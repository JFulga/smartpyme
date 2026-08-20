package com.tienda.smartP.dto;

import com.tienda.smartP.model.Role;
import lombok.Getter;
import lombok.Setter;

/** Payload for user creation performed by an authenticated administrator. */
@Getter
@Setter
public class CreateUserRequest {

    private String username;
    private String password;
    private Role role;
}
