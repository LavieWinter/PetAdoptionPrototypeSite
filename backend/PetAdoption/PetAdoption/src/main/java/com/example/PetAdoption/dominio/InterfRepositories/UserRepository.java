package com.example.PetAdoption.dominio.InterfRepositories;

import com.example.PetAdoption.dominio.entidades.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<UserModel> findByEmailIgnoreCase(String email);
}
