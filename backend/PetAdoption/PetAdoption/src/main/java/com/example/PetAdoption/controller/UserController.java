package com.example.PetAdoption.controller;

import com.example.PetAdoption.dominio.auth.JwtService;
import com.example.PetAdoption.dominio.auth.TokenBlacklistService;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.dominio.enums.SignupRole;
import com.example.PetAdoption.dominio.enums.UserRoles;
import com.example.PetAdoption.dominio.respositories.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
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

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklist;

    public UserController(
            UserRepository users,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtService jwtService,
            TokenBlacklistService blacklist
    ) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.blacklist = blacklist;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Pet Adoption API!";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest body) {
        final String email = body.email().trim().toLowerCase();

        if (users.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("This email is already registered"));

        }

        // define roles efetivos
        Set<SignupRole> requested = Objects.requireNonNullElse(body.roles(), Set.of());
        EnumSet<UserRoles> effective = EnumSet.of(UserRoles.ADOTANTE);
        if (requested.contains(SignupRole.DOADOR)) {
            effective.add(UserRoles.DOADOR);
        }

        // monta o usuário
        UserModel u = new UserModel();
        u.setEmail(email);
        u.setName(body.name().trim());
        if (body.phone() != null && !body.phone().isBlank()) {
            u.setPhone(body.phone().trim());
        }
        u.setPassword(passwordEncoder.encode(body.password()));

        // aplique os roles ANTES de salvar
        effective.forEach(u::addRole);

        // salva UMA vez (User + user_roles)
        users.save(u);

        // gera o token
        String token = jwtService.generateAccessToken(u.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(new AuthResponse("User registered successfully. Token: " + token));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SigninRequest body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.email().trim().toLowerCase(), body.password()
                )
        );
        String token = jwtService.generateAccessToken(auth.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body(new AuthResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = jwtService.resolveBearerToken(authorization);
        if (token != null && jwtService.isTokenSignatureValid(token)) {
            blacklist.revoke(token, jwtService.getExpiration(token).toInstant());
        }
        return ResponseEntity.noContent().build();
    }
    
@GetMapping("/me")
public ResponseEntity<UserDto> me(Authentication auth) {
    if (auth == null || auth.getName() == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    String email = auth.getName(); 
    var user = users.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    var dto = new UserDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getRoles().stream().map(Enum::name).toList()
    );
    return ResponseEntity.ok(dto);
}

public record UserDto(
        java.util.UUID id,
        String name,
        String email,
        String phone,
        java.util.List<String> roles
) {}


    // ---------- DTOs ----------
    public record SignupRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 6, max = 100) String password,
            @NotBlank String name,
            String phone,
            Set<SignupRole> roles // opcional: ADOTANTE, DOADOR
    ) {}

    public record SigninRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    public record AuthResponse(String accessToken) {}


}
