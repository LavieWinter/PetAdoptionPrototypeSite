package com.example.PetAdoption.dominio.entidades;

import com.example.PetAdoption.dominio.enums.PetStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Domain Model: sem anotações JPA/Spring. */
public class PetModel {

    private UUID id;
    private String name;
    private String species;
    private String breed;
    private String size;
    private String sex;

    private PetStatus status = PetStatus.AVAILABLE;

    // FK -> user_admin(id)
    private UUID rescuedById;

    // campos de saúde/comportamento
    private String  hasSpecialNeeds;
    private Boolean hasOngoingTreatment;
    private String  hasChronicDisease;
    private Boolean goodWithOtherAnimals;
    private Boolean requiresConstantCare;

    private LocalDate registeredDate;
    private LocalDate rescuedAt;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public PetStatus getStatus() { return status; }
    public void setStatus(PetStatus status) { this.status = status; }
    public UUID getRescuedById() { return rescuedById; }
    public void setRescuedById(UUID rescuedById) { this.rescuedById = rescuedById; }
    public String getHasSpecialNeeds() { return hasSpecialNeeds; }
    public void setHasSpecialNeeds(String hasSpecialNeeds) { this.hasSpecialNeeds = hasSpecialNeeds; }
    public Boolean getHasOngoingTreatment() { return hasOngoingTreatment; }
    public void setHasOngoingTreatment(Boolean hasOngoingTreatment) { this.hasOngoingTreatment = hasOngoingTreatment; }
    public String getHasChronicDisease() { return hasChronicDisease; }
    public void setHasChronicDisease(String hasChronicDisease) { this.hasChronicDisease = hasChronicDisease; }
    public Boolean getGoodWithOtherAnimals() { return goodWithOtherAnimals; }
    public void setGoodWithOtherAnimals(Boolean goodWithOtherAnimals) { this.goodWithOtherAnimals = goodWithOtherAnimals; }
    public Boolean getRequiresConstantCare() { return requiresConstantCare; }
    public void setRequiresConstantCare(Boolean requiresConstantCare) { this.requiresConstantCare = requiresConstantCare; }
    public LocalDate getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate = registeredDate; }
    public LocalDate getRescuedAt() { return rescuedAt; }
    public void setRescuedAt(LocalDate rescuedAt) { this.rescuedAt = rescuedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}