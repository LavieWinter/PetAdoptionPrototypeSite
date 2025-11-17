package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.AdopterPreferencesRepositoryPort;
import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdopterPreferencesService {

    private final AdopterPreferencesRepositoryPort repo;

    public AdopterPreferencesService(AdopterPreferencesRepositoryPort repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public Optional<AdopterPreferencesModel> getByAdopterId(UUID adopterId) {
        return repo.findByAdopterId(adopterId);
    }

    @Transactional
    public AdopterPreferencesModel upsert(UUID adopterId, AdopterPreferencesModel incoming) {
        // regra 0..1 (índice único no banco já garante); aqui só consolida merge
        Optional<AdopterPreferencesModel> existingOpt = repo.findByAdopterId(adopterId);
        AdopterPreferencesModel toSave = existingOpt.orElseGet(AdopterPreferencesModel::new);
        toSave.setAdopterId(adopterId);

        // merge dos campos (parcial/total, conforme tua preferência)
        toSave.setDesiredSpecies(incoming.getDesiredSpecies());
        toSave.setDesiredSize(incoming.getDesiredSize());
        toSave.setPetGender(incoming.getPetGender());
        toSave.setAcceptsSpecialNeeds(incoming.getAcceptsSpecialNeeds());
        toSave.setAcceptsOngoingTreatment(incoming.getAcceptsOngoingTreatment());
        toSave.setAcceptsChronicDisease(incoming.getAcceptsChronicDisease());
        toSave.setHasOtherPets(incoming.getHasOtherPets());
        toSave.setHasTimeForConstantCare(incoming.getHasTimeForConstantCare());
        toSave.setUpdatedAt(Instant.now());

        return repo.save(toSave);
    }

    @Transactional
    public void deleteByAdopterId(UUID adopterId) {
        repo.deleteByAdopterId(adopterId);
    }
}