package com.example.PetAdoption.interfAdaptadora.interfJPA;

import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.interfAdaptadora.entidades.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPetRepository extends JpaRepository<Pet, UUID> {
    Page<Pet> findByStatus(PetStatus status, Pageable pageable);
}