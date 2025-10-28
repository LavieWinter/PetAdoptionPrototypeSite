package com.example.PetAdoption.dominio.InterfRepositories;

import com.example.PetAdoption.dominio.entidades.PetRecommendationModel;
import java.util.List;
import java.util.UUID;



public interface AdoptionPort {
    List<PetRecommendationModel> rankedPets(UUID adopterId, UUID applicationId, int limit, int offset);

    /** Cria uma aplicação de adoção (use_default_preferences aplicado) e retorna o ID. */
    UUID createApplication(UUID adopterId, UUID petId, boolean useDefaultPreferences);

    /** Confirma a adoção: marca o pet como ADOPTED e grava linha em adoption (quando existir). */
    void confirmAdoption(UUID applicationId);
}