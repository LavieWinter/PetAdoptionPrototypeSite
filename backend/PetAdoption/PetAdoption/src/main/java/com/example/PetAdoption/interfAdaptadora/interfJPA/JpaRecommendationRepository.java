package com.example.PetAdoption.interfAdaptadora.interfJPA;

import com.example.PetAdoption.interfAdaptadora.interfJPA.projections.PetRecommendationRow;
import com.example.PetAdoption.interfAdaptadora.entidades.Pet; // <-- a tua entidade

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaRecommendationRepository extends JpaRepository<Pet, UUID> {

    @Query(
  value = """
    SELECT 
      p.id      AS petId,   -- <== aqui!
      p.name    AS name,
      p.species AS species,
      p.size    AS size,
      p.sex     AS sex,
      r.score   AS score
    FROM pets p
    CROSS JOIN LATERAL public.compute_compatibility_score(:adopterId, p.id, :applicationId) AS r(score)
    WHERE p.status = 'AVAILABLE'
      AND r.score IS NOT NULL
    ORDER BY r.score DESC, p.created_at DESC
  """,
  countQuery = """
    SELECT COUNT(*)
    FROM pets p
    CROSS JOIN LATERAL public.compute_compatibility_score(:adopterId, p.id, :applicationId) AS r(score)
    WHERE p.status = 'AVAILABLE'
      AND r.score IS NOT NULL
  """,
  nativeQuery = true
)
Page<PetRecommendationRow> recommendForAdopter(
    @Param("adopterId") UUID adopterId,
    @Param("applicationId") UUID applicationId,
    Pageable pageable
);
}