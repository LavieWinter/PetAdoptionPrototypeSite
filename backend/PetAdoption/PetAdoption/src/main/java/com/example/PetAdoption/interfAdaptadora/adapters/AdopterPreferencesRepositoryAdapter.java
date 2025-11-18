package com.example.PetAdoption.interfAdaptadora.adapters;
import com.example.PetAdoption.dominio.InterfRepositories.AdopterPreferencesRepositoryPort;
import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;
import com.example.PetAdoption.interfAdaptadora.entidades.AdopterPreferenceEntity;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaAdopterPreferencesRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AdopterPreferencesRepositoryAdapter implements AdopterPreferencesRepositoryPort {

    private final JpaAdopterPreferencesRepository jpa;

    public AdopterPreferencesRepositoryAdapter(JpaAdopterPreferencesRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<AdopterPreferencesModel> findByAdopterId(UUID adopterId) {
        return jpa.findByAdopterId(adopterId).map(AdopterPreferenceEntity::toModel);
    }

    @Override
    public AdopterPreferencesModel save(AdopterPreferencesModel model) {
        // @PrePersist da Entity já gera preferenceKey/updatedAt se necessário
        AdopterPreferenceEntity entity = AdopterPreferenceEntity.fromModel(model);
        AdopterPreferenceEntity saved = jpa.save(entity);
        return saved.toModel();
    }

    @Override
    public void deleteByAdopterId(UUID adopterId) {
        jpa.deleteByAdopterId(adopterId);
    }

    @Override
    public boolean existsByAdopterId(UUID adopterId) {
        return jpa.existsByAdopterId(adopterId);
    }
}
