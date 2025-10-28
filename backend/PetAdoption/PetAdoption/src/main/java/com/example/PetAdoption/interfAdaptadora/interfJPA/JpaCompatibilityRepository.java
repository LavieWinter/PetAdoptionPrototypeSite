package com.example.PetAdoption.interfAdaptadora.interfJPA;


import com.example.PetAdoption.interfAdaptadora.entidades.Pet;
import com.example.PetAdoption.interfAdaptadora.interfJPA.projections.PetCompatibilityRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaCompatibilityRepository extends JpaRepository<Pet, UUID> {

    @Query(value = """
        SELECT  p.id AS pet_id,
                p.name,
                p.species,
                p.size,
                compute_compatibility_score(:adopterId, p.id, :applicationId) AS score
        FROM pets p
        WHERE compute_compatibility_score(:adopterId, p.id, :applicationId) IS NOT NULL
        ORDER BY score DESC NULLS LAST, p.created_at DESC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<PetCompatibilityRow> findRankedPets(
            @Param("adopterId") UUID adopterId,
            @Param("applicationId") UUID applicationId,   // pode ser null
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}