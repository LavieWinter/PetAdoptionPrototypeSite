package com.example.PetAdoption.dominio.auth;

import com.example.PetAdoption.dominio.entidades.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UsersDetails implements UserDetails {
    private final String username;
    private final String password;
    public List<GrantedAuthority> authorities;

    public UsersDetails(UserModel userModel) {
        this.username = userModel.getEmail();
        this.password = userModel.getPassword();
        this.authorities = Stream.of(userModel.getRoles())
                .map(
                        role -> new SimpleGrantedAuthority(role.toString())  // Convert each role to GrantedAuthority
                )
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities if needed, e.g., roles or permissions
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
