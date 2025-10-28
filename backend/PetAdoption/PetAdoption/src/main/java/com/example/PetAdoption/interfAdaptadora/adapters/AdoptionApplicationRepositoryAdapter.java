package com.example.PetAdoption.interfAdaptadora.adapters;
import com.example.PetAdoption.dominio.InterfRepositories.AdoptionApplicationRepositoryPort;
import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;
import com.example.PetAdoption.interfAdaptadora.entidades.AdoptionApplicationEntity;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaAdoptionApplicationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.PetAdoption.dominio.enums.ApplicationStatus.IN_REVIEW;
import static com.example.PetAdoption.dominio.enums.ApplicationStatus.SUBMITTED;

@Component
public class AdoptionApplicationRepositoryAdapter implements AdoptionApplicationRepositoryPort {

    private final JpaAdoptionApplicationRepository jpa;

    public AdoptionApplicationRepositoryAdapter(JpaAdoptionApplicationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional
    public AdoptionApplicationModel save(AdoptionApplicationModel m) {
        AdoptionApplicationEntity e = toEntity(m);
        // Se m.getId() == null, o Hibernate fará INSERT e gerará o UUID
        AdoptionApplicationEntity saved = jpa.save(e);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdoptionApplicationModel> findById(UUID id) {
        return jpa.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionApplicationModel> findMine(UUID adopterId, int page, int size) {
        return jpa.findByAdopterIdOrderByCreatedAtDesc(adopterId, PageRequest.of(page, size))
                  .map(this::toDomain)
                  .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public long countMine(UUID adopterId) {
        return jpa.countByAdopterId(adopterId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsOpenForAdopterAndPet(UUID adopterId, UUID petId) {
        return jpa.existsByAdopterIdAndPetIdAndStatusIn(adopterId, petId, List.of(SUBMITTED, IN_REVIEW));
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, ApplicationStatus newStatus) {
        jpa.updateStatus(id, newStatus);
    }

    // ===== mappers =====
    private AdoptionApplicationModel toDomain(AdoptionApplicationEntity e) {
        var m = new AdoptionApplicationModel();
        m.setId(e.getId());
        m.setAdopterId(e.getAdopterId());
        m.setPetId(e.getPetId());
        m.setStatus(e.getStatus());
        m.setUseDefaultPreferences(e.isUseDefaultPreferences());
        m.setSubmittedAt(e.getSubmittedAt());
        m.setReviewedBy(e.getReviewedBy());
        m.setReviewNotes(e.getReviewNotes());
        m.setCreatedAt(e.getCreatedAt());
        m.setUpdatedAt(e.getUpdatedAt());
        return m;
    }

    private AdoptionApplicationEntity toEntity(AdoptionApplicationModel m) {
        var e = new AdoptionApplicationEntity();
        // NÃO force gerar id; se vier null, o Hibernate gera.
        e.setId(m.getId());
        e.setAdopterId(m.getAdopterId());
        e.setPetId(m.getPetId());
        e.setStatus(m.getStatus());
        e.setUseDefaultPreferences(Boolean.TRUE.equals(m.getUseDefaultPreferences()));
        e.setSubmittedAt(m.getSubmittedAt());
        e.setReviewedBy(m.getReviewedBy());
        e.setReviewNotes(m.getReviewNotes());
        e.setCreatedAt(m.getCreatedAt());
        e.setUpdatedAt(m.getUpdatedAt());
        return e;
    }
}