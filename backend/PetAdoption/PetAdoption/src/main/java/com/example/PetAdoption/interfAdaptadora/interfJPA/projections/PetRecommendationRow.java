package com.example.PetAdoption.interfAdaptadora.interfJPA.projections;

import java.util.UUID;

/** Projeção para resultados da recomendação (native query). */
public interface PetRecommendationRow {
    UUID getPetId();
    String getName();
    String getSpecies();
    String getSize();
    String getSex();
    Integer getScore();
}