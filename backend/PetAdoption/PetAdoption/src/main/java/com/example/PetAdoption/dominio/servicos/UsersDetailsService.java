package com.example.PetAdoption.dominio.servicos;

import com.example.PetAdoption.dominio.auth.UsersDetails;
import com.example.PetAdoption.dominio.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsersDetails loadUserByUsername(String username) {
        return userRepository.findByEmailIgnoreCase(username)
                .map(UsersDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
