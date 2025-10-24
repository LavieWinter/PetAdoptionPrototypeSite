package com.example.PetAdoption.dominio.InterfRepositories;
import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;

import java.util.Optional;
import java.util.UUID;

public interface AdopterPreferencesRepositoryPort {
    Optional<AdopterPreferencesModel> findByAdopterId(UUID adopterId);
    AdopterPreferencesModel save(AdopterPreferencesModel model); // upsert
    void deleteByAdopterId(UUID adopterId);
    boolean existsByAdopterId(UUID adopterId);
}