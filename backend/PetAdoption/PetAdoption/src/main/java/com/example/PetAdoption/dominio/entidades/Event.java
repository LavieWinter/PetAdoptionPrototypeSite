package com.example.PetAdoption.dominio.entidades;

import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // FK → pets(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // FK opcional → adoptions(id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private Adoption adoption;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType; // HEALTH | RELOCATION | VISIT

    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false)
    private EventPhase phase;    // RESCUE | SHELTER | POST_ADOPTION

    @Column(name = "date")
    private LocalDate date;

    // categoria do incidente (texto livre conforme diagrama: VACCINATION|NEUTERING|DISEASE|OTHER)
    @Column(name = "incident_type")
    private String incidentType;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }

    public Adoption getAdoption() { return adoption; }
    public void setAdoption(Adoption adoption) { this.adoption = adoption; }

    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }

    public EventPhase getPhase() { return phase; }
    public void setPhase(EventPhase phase) { this.phase = phase; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}