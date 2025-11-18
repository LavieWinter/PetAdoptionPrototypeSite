package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.AdoptionApplicationRepositoryPort;
import com.example.PetAdoption.dominio.InterfRepositories.UserRepository;
import com.example.PetAdoption.interfAdaptadora.dtos.PageResult;
import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.dominio.enums.AdoptionStatus;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.interfAdaptadora.entidades.AdoptionEntity;
import com.example.PetAdoption.interfAdaptadora.entidades.Pet;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaAdoptionApplicationRepository;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaAdoptionRepository;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AdoptionService {

    private final AdoptionApplicationRepositoryPort apps;
    private final JpaAdoptionApplicationRepository appsJpa;
    private final JpaPetRepository pets;
    private final JpaAdoptionRepository adoptions;
    private final UserRepository users;

    public AdoptionService(AdoptionApplicationRepositoryPort apps,
                           JpaAdoptionApplicationRepository appsJpa,
                           JpaPetRepository pets,
                           JpaAdoptionRepository adoptions,
                           UserRepository users) {
        this.apps = apps;
        this.appsJpa = appsJpa;
        this.pets = pets;
        this.adoptions = adoptions;
        this.users = users;
    }

    @Transactional
    public AdoptionApplicationModel createApplicationForLoggedUser(String email, UUID petId, boolean useDefaultPrefs) {
        UUID adopterId = resolveUserId(email);
        Pet pet = pets.findById(petId).orElseThrow(() -> new IllegalArgumentException("Pet não encontrado"));

        if (pet.getStatus() != PetStatus.AVAILABLE) {
            throw new IllegalStateException("Pet não está disponível para adoção");
        }
        if (apps.existsOpenForAdopterAndPet(adopterId, petId)) {
            throw new IllegalStateException("Já existe uma candidatura aberta para este pet.");
        }

        Instant now = Instant.now();

        var m = new AdoptionApplicationModel();
        // NÃO definir id -> Hibernate gera
        m.setId(null);
        m.setAdopterId(adopterId);
        m.setPetId(petId);
        m.setStatus(ApplicationStatus.SUBMITTED);
        m.setUseDefaultPreferences(useDefaultPrefs);
        m.setSubmittedAt(now);
        m.setCreatedAt(now);
        m.setUpdatedAt(now);

        return apps.save(m); // se useDefaultPrefs=true, sua trigger/snapshot roda no DB
    }

    @Transactional(readOnly = true)
    public PageResult<AdoptionApplicationModel> listMyApplications(String email, int page, int size) {
        UUID me = resolveUserId(email);
        var content = apps.findMine(me, page, size);
        long total = apps.countMine(me);
        int totalPages = (int) Math.ceil(total / (double) size);
        return new PageResult<>(page, size, total, totalPages, content);
    }

    @Transactional
    public void cancelMyApplication(String email, UUID applicationId) {
        UUID me = resolveUserId(email);
        var app = apps.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application não encontrada"));

        if (!me.equals(app.getAdopterId())) {
            throw new SecurityException("Esta application não pertence ao usuário logado.");
        }
        if (app.getStatus() == ApplicationStatus.APPROVED || app.getStatus() == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Application já finalizada.");
        }
        apps.updateStatus(applicationId, ApplicationStatus.WITHDRAWN);
    }

    // ===== Admin =====

    @Transactional
    public void approve(UUID applicationId) {
        var app = appsJpa.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application não encontrada"));

        if (app.getStatus() == ApplicationStatus.WITHDRAWN || app.getStatus() == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Application cancelada/rejeitada.");
        }

        Pet pet = pets.findById(app.getPetId())
                .orElseThrow(() -> new IllegalArgumentException("Pet não encontrado"));

        if (pet.getStatus() != PetStatus.AVAILABLE) {
            throw new IllegalStateException("Pet não está disponível.");
        }

        // 1) aprova
        apps.updateStatus(applicationId, ApplicationStatus.APPROVED);

        // 2) pet -> ADOPTED
        pet.setStatus(PetStatus.ADOPTED);
        pets.save(pet);

        // 3) cria adoção
        var a = new AdoptionEntity();
        a.setApplicationId(app.getId());
        a.setPetId(app.getPetId());
        a.setTutorId(app.getAdopterId());
        a.setStatus(AdoptionStatus.ACTIVE);
        adoptions.save(a);
    }

    @Transactional
    public void reject(UUID applicationId) {
        var app = appsJpa.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application não encontrada"));

        if (app.getStatus() == ApplicationStatus.APPROVED || app.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException("Application já finalizada.");
        }
        apps.updateStatus(applicationId, ApplicationStatus.REJECTED);
    }

    private UUID resolveUserId(String email) {
        return users.findByEmailIgnoreCase(email)
                .map(UserModel::getId)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + email));
    }
}