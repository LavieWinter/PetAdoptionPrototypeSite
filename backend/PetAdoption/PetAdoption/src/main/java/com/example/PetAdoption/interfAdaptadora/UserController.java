package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.InterfRepositories.UserRepository;
import com.example.PetAdoption.dominio.auth.JwtService;
import com.example.PetAdoption.dominio.auth.TokenBlacklistService;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.dominio.enums.SignupRole;
import com.example.PetAdoption.dominio.enums.UserRoles;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
// @CrossOrigin(origins = "*")
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
            TokenBlacklistService blacklist) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.blacklist = blacklist;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the Pet Adoption API!";
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest body) {
        final String email = body.email().trim().toLowerCase();

        if (users.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse("This email is already registered"));
        }

        // depois 
        EnumSet<UserRoles> effective = EnumSet.of(UserRoles.USER); // todo mundo tem USER
        if (body.roles() != null && body.roles().contains(SignupRole.ADMIN)) {
            effective.add(UserRoles.ADMIN); // adiciona ADMIN se o cliente pedir
        }

        // monta o usuário
        UserModel u = new UserModel();
        u.setEmail(email);
        u.setName(body.name().trim());
        if (body.phone() != null && !body.phone().isBlank()) {
            u.setPhone(body.phone().trim());
        }
        u.setPassword(passwordEncoder.encode(body.password()));

        // aplica roles ANTES de salvar
        effective.forEach(u::addRole);

        // salva (user_admin + user_roles)
        users.save(u);

        // gera o token
        String token = jwtService.generateAccessToken(u.getEmail(), u.getRoles());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(new AuthResponse("User registered successfully. Token: " + token));
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody SigninRequest body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.email().trim().toLowerCase(), body.password()));

        // Load full user to get roles
        var user = users.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Generate a token that contains roles (e.g., ["USER","ADMIN"])
        String token = jwtService.generateAccessToken(user.getEmail(), user.getRoles());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body(new AuthResponse(token));
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String token = jwtService.resolveBearerToken(authorization);
        if (token != null && jwtService.isTokenSignatureValid(token)) {
            blacklist.revoke(token, jwtService.getExpiration(token).toInstant());
        }
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
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
                user.getRoles().stream().map(Enum::name).toList());
        return ResponseEntity.ok(dto);
    }
    @PreAuthorize("isAuthenticated()")
    public record IdDto(java.util.UUID id) { // para retornar id
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/testando", produces = "application/json") // retorna apenas o id
    public ResponseEntity<IdDto> testando(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var user = users.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(new IdDto(user.getId()));
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-profile") // atualizar nome e telefone
    public ResponseEntity<UserDto> updateProfile(Authentication auth,
            @Valid @RequestBody UpdateProfileRequest body) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        UserModel user = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(body.name().trim());
        if (body.phone() != null)
            user.setPhone(body.phone().trim());
        users.save(user);

        var dto = new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles().stream().map(Enum::name).toList());
        return ResponseEntity.ok(dto);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(Authentication auth,
            @Valid @RequestBody ChangePasswordRequest body) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        UserModel user = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // valida senha atual
        if (!passwordEncoder.matches(body.currentPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // aplica nova senha
        user.setPassword(passwordEncoder.encode(body.newPassword()));
        users.save(user);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/id")
    public ResponseEntity<IdResponse> myId(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            // mantém o tipo <IdResponse> para não estourar inferência do Optional
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return users.findByEmailIgnoreCase(auth.getName())
                .map(u -> ResponseEntity.ok(new IdResponse(u.getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    // ---------- DTOs ----------
    public record SignupRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 6, max = 100) String password,
            @NotBlank String name,
            String phone,
            Set<SignupRole> roles // agora aceita: USER, ADMIN (e outros que você tiver)
    ) {
    }

    public record SigninRequest(
            @Email @NotBlank String email,
            @NotBlank String password) {
    }

    public record AuthResponse(String accessToken) {
    }

    // ========= NOVOS DTOs =========
    public record UserDto( // para o /me
            java.util.UUID id,
            String name,
            String email,
            String phone,
            java.util.List<String> roles) {
    }

    public record UpdateProfileRequest(
            @NotBlank String name,
            String phone) {
    }

    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank @Size(min = 6, max = 100) String newPassword) {
    }

    public record RefreshResponse(String accessToken) {
    }

    public record IdResponse(java.util.UUID id) {
    }

}
