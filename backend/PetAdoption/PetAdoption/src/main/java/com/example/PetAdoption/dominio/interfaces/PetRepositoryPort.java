package com.example.PetAdoption.dominio.interfaces;
import com.example.PetAdoption.dominio.entidades.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetRepositoryPort {
    List<Pet> findAvailableOrdered(int page, int size);
    Optional<Pet> findById(UUID id);
    Pet save(Pet pet);
}
