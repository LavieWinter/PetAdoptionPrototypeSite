package com.example.PetAdoption.interfAdaptadora.interfJPA.projections;

import java.util.UUID;

public interface PetCompatibilityRow {
    UUID getPetId();
    String getName();
    String getSpecies();
    String getSize();
    Integer getScore();
}