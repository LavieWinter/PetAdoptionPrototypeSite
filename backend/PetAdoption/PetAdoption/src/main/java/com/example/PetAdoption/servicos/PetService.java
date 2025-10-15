package com.example.PetAdoption.servicos;
import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.dominio.InterfRepositories.PetRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepositoryPort pets;

    public PetService(PetRepositoryPort pets) {
        this.pets = pets;
    }

    public PetModel create(PetModel pet) {
        pet.setId(null);
        var now = OffsetDateTime.now();
        pet.setCreatedAt(now);
        pet.setUpdatedAt(now);
        return pets.save(pet);
    }

    public Optional<PetModel> get(UUID id) {
        return pets.findById(id);
    }

    public List<PetModel> list(int page, int size, String sortBy, boolean asc) {
        return pets.findAll(page, size, sortBy, asc);
    }

    public List<PetModel> listByStatus(PetStatus status, int page, int size, String sortBy, boolean asc) {
        return pets.findByStatus(status, page, size, sortBy, asc);
    }

    public Optional<PetModel> update(UUID id, PetModel incoming) {
        return pets.findById(id).map(existing -> {
            // campos editáveis
            existing.setName(incoming.getName());
            existing.setSpecies(incoming.getSpecies());
            existing.setBreed(incoming.getBreed());
            existing.setSize(incoming.getSize());
            existing.setSex(incoming.getSex());
            if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
            existing.setRescuedById(incoming.getRescuedById());
            existing.setHasSpecialNeeds(incoming.getHasSpecialNeeds());
            existing.setHasOngoingTreatment(incoming.getHasOngoingTreatment());
            existing.setHasChronicDisease(incoming.getHasChronicDisease());
            existing.setGoodWithOtherAnimals(incoming.getGoodWithOtherAnimals());
            existing.setRequiresConstantCare(incoming.getRequiresConstantCare());
            existing.setRegisteredDate(incoming.getRegisteredDate());
            existing.setRescuedAt(incoming.getRescuedAt());
            existing.setUpdatedAt(OffsetDateTime.now());
            return pets.save(existing);
        });
    }

    public boolean delete(UUID id) {
        return pets.deleteById(id);
    }
}