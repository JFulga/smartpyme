package com.tienda.smartP.controller;

import com.tienda.smartP.dto.CreateUserRequest;
import com.tienda.smartP.model.User;
import com.tienda.smartP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * The route is restricted to ADMIN in SecurityConfig. The requested role is
     * accepted here only after that server-side authorization has succeeded.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        if (request.getRole() == null) {
            return ResponseEntity.badRequest().body("El rol es obligatorio");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado");
    }
}
