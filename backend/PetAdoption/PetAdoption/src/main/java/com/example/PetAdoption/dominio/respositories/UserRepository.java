package com.example.PetAdoption.dominio.respositories;

import com.example.PetAdoption.dominio.entidades.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<UserModel> findByEmailIgnoreCase(String email);
}
