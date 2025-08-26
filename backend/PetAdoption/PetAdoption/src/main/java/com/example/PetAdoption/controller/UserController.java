package com.example.PetAdoption.controller;

import com.example.PetAdoption.dominio.auth.JwtService;
import com.example.PetAdoption.dominio.auth.TokenBlacklistService;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.dominio.respositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserRepository users;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenBlacklistService blacklist;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Pet Adoption API!";
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupRequest body) {
        if (users.existsByEmailIgnoreCase(body.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserModel u = new UserModel();
        u.setEmail(body.email().toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(body.password()));
        u.addRole("USER");
        users.save(u);

        String token = jwtService.generateAccessToken(u.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SigninRequest body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.email().toLowerCase(), body.password())
        );
        String token = jwtService.generateAccessToken(auth.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = jwtService.resolveBearerToken(authorization);
        if (token != null && jwtService.isTokenSignatureValid(token)) {
            blacklist.revoke(token, jwtService.getExpiration(token).toInstant());
        }
        return ResponseEntity.noContent().build();
    }

    // ---------- DTOs ----------
    public record SignupRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}
    public record SigninRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}
    public record AuthResponse(String accessToken) {}

}
