package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.AdoptionPort;
import com.example.PetAdoption.dominio.entidades.PetRecommendationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AdoptionService {

    private final AdoptionPort port;

    public AdoptionService(AdoptionPort port) {
        this.port = port;
    }

    public List<PetRecommendationModel> rankedPets(UUID adopterId, UUID applicationId, int limit, int offset) {
        return port.rankedPets(adopterId, applicationId, limit, offset);
    }

    @Transactional
    public UUID createApplication(UUID adopterId, UUID petId, boolean useDefaultPreferences) {
        return port.createApplication(adopterId, petId, useDefaultPreferences);
    }

    @Transactional
    public void confirmAdoption(UUID applicationId) {
        port.confirmAdoption(applicationId);
    }
}