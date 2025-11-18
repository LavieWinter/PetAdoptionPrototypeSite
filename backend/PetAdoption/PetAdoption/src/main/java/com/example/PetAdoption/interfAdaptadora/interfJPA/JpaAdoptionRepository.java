package com.example.PetAdoption.interfAdaptadora.interfJPA;
import com.example.PetAdoption.interfAdaptadora.entidades.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAdoptionRepository extends JpaRepository<AdoptionEntity, UUID> {
}