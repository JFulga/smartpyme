package com.tienda.smartP.controller;

import com.tienda.smartP.dto.AuthRequestDTO;
import com.tienda.smartP.dto.AuthResponseDTO;
import com.tienda.smartP.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

@RequiredArgsConstructor

public class AuthController {

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
}