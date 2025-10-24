package com.example.PetAdoption.interfAdaptadora.interfJPA;

import com.example.PetAdoption.interfAdaptadora.entidades.AdopterPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository // opcional
public interface JpaAdopterPreferencesRepository extends JpaRepository<AdopterPreferenceEntity, UUID> {
    Optional<AdopterPreferenceEntity> findByAdopterId(UUID adopterId);
    boolean existsByAdopterId(UUID adopterId);
    void deleteByAdopterId(UUID adopterId);
}