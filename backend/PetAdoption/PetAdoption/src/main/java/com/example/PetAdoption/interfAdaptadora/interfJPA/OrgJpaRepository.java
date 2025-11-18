package com.example.PetAdoption.interfAdaptadora.interfJPA;

import com.example.PetAdoption.interfAdaptadora.entidades.Org;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrgJpaRepository extends JpaRepository<Org, UUID> {
    boolean existsByEmailIgnoreCase(String email);

    Page<Org> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
            String name, String email, String phone, Pageable pageable
    );
}
