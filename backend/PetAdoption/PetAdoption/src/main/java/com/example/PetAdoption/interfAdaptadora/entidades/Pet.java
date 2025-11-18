package com.example.PetAdoption.interfAdaptadora.entidades;
import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "species")
    private String species;

    @Column(name = "breed")
    private String breed;

    @Column(name = "size")
    private String size;

    @Column(name = "sex")
    private String sex;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PetStatus status = PetStatus.AVAILABLE;

    @Column(name = "rescued_by_id")
    private UUID rescuedById;

    @Column(name = "has_special_needs")
    private String hasSpecialNeeds;

    @Column(name = "has_ongoing_treatment")
    private Boolean hasOngoingTreatment;

    @Column(name = "has_chronic_disease")
    private String hasChronicDisease;

    @Column(name = "good_with_other_animals")
    private Boolean goodWithOtherAnimals;

    @Column(name = "requires_constant_care")
    private Boolean requiresConstantCare;

    @Column(name = "registered_date")
    private LocalDate registeredDate;

    @Column(name = "rescued_at")
    private LocalDate rescuedAt;

    @Column(name = "created_at", columnDefinition = "timestamptz")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamptz")
    private OffsetDateTime updatedAt;

    // ===== Lifecycle =====
    @PrePersist
    void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // ===== Getters/Setters =====
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

    // ======= Mapper embutido =======

    /** Converte Domain Model -> Entity JPA */
    public static Pet fromModel(PetModel m) {
        if (m == null) return null;
        Pet e = new Pet();
        e.setId(m.getId());
        e.setName(m.getName());
        e.setSpecies(m.getSpecies());
        e.setBreed(m.getBreed());
        e.setSize(m.getSize());
        e.setSex(m.getSex());
        e.setStatus(m.getStatus());
        e.setRescuedById(m.getRescuedById());
        e.setHasSpecialNeeds(m.getHasSpecialNeeds());
        e.setHasOngoingTreatment(m.getHasOngoingTreatment());
        e.setHasChronicDisease(m.getHasChronicDisease());
        e.setGoodWithOtherAnimals(m.getGoodWithOtherAnimals());
        e.setRequiresConstantCare(m.getRequiresConstantCare());
        e.setRegisteredDate(m.getRegisteredDate());
        e.setRescuedAt(m.getRescuedAt());
        e.setCreatedAt(m.getCreatedAt());
        e.setUpdatedAt(m.getUpdatedAt());
        return e;
    }

    /** Converte Entity JPA -> Domain Model */
    public static PetModel toModel(Pet e) {
        if (e == null) return null;
        PetModel m = new PetModel();
        m.setId(e.getId());
        m.setName(e.getName());
        m.setSpecies(e.getSpecies());
        m.setBreed(e.getBreed());
        m.setSize(e.getSize());
        m.setSex(e.getSex());
        m.setStatus(e.getStatus());
        m.setRescuedById(e.getRescuedById());
        m.setHasSpecialNeeds(e.getHasSpecialNeeds());
        m.setHasOngoingTreatment(e.getHasOngoingTreatment());
        m.setHasChronicDisease(e.getHasChronicDisease());
        m.setGoodWithOtherAnimals(e.getGoodWithOtherAnimals());
        m.setRequiresConstantCare(e.getRequiresConstantCare());
        m.setRegisteredDate(e.getRegisteredDate());
        m.setRescuedAt(e.getRescuedAt());
        m.setCreatedAt(e.getCreatedAt());
        m.setUpdatedAt(e.getUpdatedAt());
        return m;
    }
}