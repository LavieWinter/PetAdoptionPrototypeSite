package com.example.PetAdoption.dominio.InterfRepositories;

import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdoptionApplicationRepositoryPort {
    AdoptionApplicationModel save(AdoptionApplicationModel model);
    Optional<AdoptionApplicationModel> findById(UUID id);
    List<AdoptionApplicationModel> findMine(UUID adopterId, int page, int size);
    long countMine(UUID adopterId);
    boolean existsOpenForAdopterAndPet(UUID adopterId, UUID petId);
    void updateStatus(UUID id, ApplicationStatus newStatus);
}