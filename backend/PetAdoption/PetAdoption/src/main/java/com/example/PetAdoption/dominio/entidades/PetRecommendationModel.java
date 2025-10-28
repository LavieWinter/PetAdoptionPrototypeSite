package com.example.PetAdoption.dominio.entidades;

import java.util.UUID;

public class PetRecommendationModel {
    private UUID petId;
    private String name;
    private String species;
    private String size;
    private Integer score;

    // getters/setters
    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}