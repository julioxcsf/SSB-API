package com.meuprojeto.banco.sistemabancario.controller;

import com.meuprojeto.banco.sistemabancario.controller.dto.login.AuthRequest;
import com.meuprojeto.banco.sistemabancario.controller.dto.login.AuthResponse;
import com.meuprojeto.banco.sistemabancario.security.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.senha())
            );

            UserDetails user = (UserDetails) auth.getPrincipal();
            String token = jwtTokenService.generateToken(user.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
