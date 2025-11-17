package com.example.PetAdoption;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize, @PostAuthorize, @Secured, @RolesAllowed
public class MethodSecurityConfig { }
