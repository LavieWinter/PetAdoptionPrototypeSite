package com.example.PetAdoption.dominio.InterfRepositories;
import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;

import java.util.*;

public interface PetRepositoryPort {

    PetModel save(PetModel pet);

    Optional<PetModel> findById(UUID id);

    /** Lista com paginação + ordenação. */
    List<PetModel> findAll(int page, int size, String sortBy, boolean asc);

    /** Lista filtrando por status, com paginação + ordenação. */
    List<PetModel> findByStatus(PetStatus status, int page, int size, String sortBy, boolean asc);

    /** Retorna true se removeu, false se não existia. */
    boolean deleteById(UUID id);
}
