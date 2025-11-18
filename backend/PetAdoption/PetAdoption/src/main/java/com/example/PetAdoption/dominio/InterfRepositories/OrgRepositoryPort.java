package com.example.PetAdoption.dominio.InterfRepositories;

import com.example.PetAdoption.dominio.entidades.OrgModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrgRepositoryPort {
    OrgModel save(OrgModel model);
    Optional<OrgModel> findById(UUID id);
    List<OrgModel> findAll(int page, int size, String sortBy, boolean asc, String q);
    boolean deleteById(UUID id);
    boolean existsByEmailIgnoreCase(String email);
}
