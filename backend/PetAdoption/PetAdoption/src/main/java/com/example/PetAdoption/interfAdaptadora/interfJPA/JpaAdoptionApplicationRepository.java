package com.example.PetAdoption.interfAdaptadora.interfJPA;
import com.example.PetAdoption.dominio.enums.ApplicationStatus;
import com.example.PetAdoption.interfAdaptadora.entidades.AdoptionApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.UUID;

public interface JpaAdoptionApplicationRepository extends JpaRepository<AdoptionApplicationEntity, UUID> {

    Page<AdoptionApplicationEntity> findByAdopterIdOrderByCreatedAtDesc(UUID adopterId, Pageable pageable);

    long countByAdopterId(UUID adopterId);

    boolean existsByAdopterIdAndPetIdAndStatusIn(UUID adopterId, UUID petId, Collection<ApplicationStatus> statuses);

    @Modifying
    @Query("update AdoptionApplicationEntity a set a.status = :status, a.updatedAt = CURRENT_TIMESTAMP where a.id = :id")
    void updateStatus(UUID id, ApplicationStatus status);
}