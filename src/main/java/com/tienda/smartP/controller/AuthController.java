package com.tienda.smartP.controller;

import com.tienda.smartP.dto.AuthRequestDTO;
import com.tienda.smartP.dto.AuthResponseDTO;
import com.tienda.smartP.dto.RegisterRequest;
import com.tienda.smartP.model.Role;
import com.tienda.smartP.model.User;
import com.tienda.smartP.repository.UserRepository;
import com.tienda.smartP.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

@RequiredArgsConstructor

public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody AuthRequestDTO request) {


        System.out.println(authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        ));

        String token = jwtService.generateToken(
                request.getUsername()
        );

        return new AuthResponseDTO(token);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("El usuario ya existe");
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.ADMIN);

        userRepository.save(user);

        return ResponseEntity.ok("Usuario registrado");
    }


}