package com.example.PetAdoption.dominio.InterfRepositories;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.PetAdoption.dominio.entidades.*;

public interface PetRepositoryPort {
    List<PetModel> findAvailableOrdered(int page, int size);
    Optional<PetModel> findById(UUID id);
    PetModel save(PetModel pet);
}
