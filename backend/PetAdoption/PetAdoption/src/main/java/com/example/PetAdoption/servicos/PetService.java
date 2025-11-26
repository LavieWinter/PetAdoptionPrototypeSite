package com.example.PetAdoption.servicos;

import com.example.PetAdoption.servicos.StorageService;
import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.dominio.InterfRepositories.PetRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepositoryPort pets;
    private final StorageService storage;

    public PetService(PetRepositoryPort pets, StorageService storage) {
        this.pets = pets;
        this.storage = storage;
    }

    public PetModel create(PetModel pet) {
        pet.setId(null);
        var now = OffsetDateTime.now();
        pet.setCreatedAt(now);
        pet.setUpdatedAt(now);
        // para não violar NOT NULL / CHECK do banco
        if (pet.getStatus() == null) {
            pet.setStatus(PetStatus.AVAILABLE); // ajuste para o enum que seu CHECK aceita
        }
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

            // Strings: só atualiza se não for null
            if (incoming.getName() != null)
                existing.setName(incoming.getName());
            if (incoming.getSpecies() != null)
                existing.setSpecies(incoming.getSpecies());
            if (incoming.getBreed() != null)
                existing.setBreed(incoming.getBreed());
            if (incoming.getSize() != null)
                existing.setSize(incoming.getSize());
            if (incoming.getSex() != null)
                existing.setSex(incoming.getSex());

            // Enums/UUID/Data: idem
            if (incoming.getStatus() != null)
                existing.setStatus(incoming.getStatus());
            if (incoming.getRescuedById() != null)
                existing.setRescuedById(incoming.getRescuedById());

            // Booleans (são Boolean, não primitivo) — só aplica se não for null
            if (incoming.getHasOngoingTreatment() != null) {
                existing.setHasOngoingTreatment(incoming.getHasOngoingTreatment());
            }
            if (incoming.getGoodWithOtherAnimals() != null) {
                existing.setGoodWithOtherAnimals(incoming.getGoodWithOtherAnimals());
            }
            if (incoming.getRequiresConstantCare() != null) {
                existing.setRequiresConstantCare(incoming.getRequiresConstantCare());
            }

            if (incoming.getHasSpecialNeeds() != null) {
                existing.setHasSpecialNeeds(incoming.getHasSpecialNeeds());
            }
            if (incoming.getHasChronicDisease() != null) {
                existing.setHasChronicDisease(incoming.getHasChronicDisease());
            }

            if (incoming.getRegisteredDate() != null) {
                existing.setRegisteredDate(incoming.getRegisteredDate());
            }
            if (incoming.getRescuedAt() != null) {
                existing.setRescuedAt(incoming.getRescuedAt());
            }
            if (incoming.getPetDescription() != null) {
                existing.setPetDescription(incoming.getPetDescription());
            }
            if (incoming.getPetImage() != null) {
                existing.setPetImage(incoming.getPetImage());
            }

            existing.setUpdatedAt(OffsetDateTime.now());
            return pets.save(existing);
        });
    }

    public boolean delete(UUID id) {
        return pets.deleteById(id);
    }
    public Optional<PetModel> updatePetImage(UUID id, MultipartFile file) {
        return pets.findById(id).map(existing -> {
            String imageRef = storage.storePetImage(id, file);
            existing.setPetImage(imageRef);
            existing.setUpdatedAt(OffsetDateTime.now());
            return pets.save(existing);
        });
    }

    public PetModel createWithImage(PetModel pet, MultipartFile file) {
        // 1. Prepara e salva o Pet inicialmente (sem a imagem) para gerar o ID
        pet.setId(null);
        var now = OffsetDateTime.now();
        pet.setCreatedAt(now);
        pet.setUpdatedAt(now);

        if (pet.getStatus() == null) {
            pet.setStatus(PetStatus.AVAILABLE);
        }

        // Primeiro save: Gera o UUID no banco (ou na entidade)
        PetModel savedPet = pets.save(pet);

        // 2. Se houver arquivo, faz o upload e atualiza o registro
        if (file != null && !file.isEmpty()) {
            try {
                // Usa o ID gerado para nomear/organizar o arquivo no storage
                String imageRef = storage.storePetImage(savedPet.getId(), file);

                // Atualiza o modelo com o caminho da imagem
                savedPet.setPetImage(imageRef);

                // Segundo save: Atualiza o registro no banco com o link da imagem
                return pets.save(savedPet);
            } catch (Exception e) {
                // Opcional: Se o upload falhar, você pode decidir se deleta o pet
                // ou apenas lança o erro, deixando o pet criado mas sem foto.
                throw new RuntimeException("Erro ao salvar imagem do pet", e);
            }
        }

        return savedPet;
    }
    
}