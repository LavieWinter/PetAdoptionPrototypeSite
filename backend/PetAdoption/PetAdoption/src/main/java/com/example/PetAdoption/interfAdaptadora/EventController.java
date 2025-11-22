package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.entidades.EventModel;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import com.example.PetAdoption.servicos.EventService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pets/{petId}/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // ---------- DTOs (somente para a Controller) ----------
    public static class EventRequestDTO {
        @NotNull
        public EventType eventType;   // HEALTH | RELOCATION | VISIT
        @NotNull
        public EventPhase phase;      // RESCUE | SHELTER | POST_ADOPTION

        public LocalDate date;
        public String incidentType;   // VACCINATION|NEUTERING|...
        public String description;
        public String location;

        public EventModel toModel(UUID petId) {
            EventModel m = new EventModel();
            m.setPetId(petId);
            m.setEventType(eventType);
            m.setPhase(phase);
            m.setDate(date);
            m.setIncidentType(incidentType);
            m.setDescription(description);
            m.setLocation(location);
            return m;
        }
    }

    public static class EventResponseDTO {
        public UUID id;
        public UUID petId;
        public UUID adoptionId;
        public EventType eventType;
        public EventPhase phase;
        public LocalDate date;
        public String incidentType;
        public String description;
        public String location;

        public static EventResponseDTO fromModel(EventModel m) {
            EventResponseDTO dto = new EventResponseDTO();
            dto.id = m.getId();
            dto.petId = m.getPetId();
            dto.adoptionId = m.getAdoptionId();
            dto.eventType = m.getEventType();
            dto.phase = m.getPhase();
            dto.date = m.getDate();
            dto.incidentType = m.getIncidentType();
            dto.description = m.getDescription();
            dto.location = m.getLocation();
            return dto;
        }
    }
    // ------------------------------------------------------

    // CREATE
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<EventResponseDTO> create(@PathVariable UUID petId,
                                                   @RequestBody EventRequestDTO body) {
        EventModel toCreate = body.toModel(petId);
        EventModel saved = service.create(toCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponseDTO.fromModel(saved));
    }

    // READ (by id)
    @PreAuthorize("permitAll()")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> get(@PathVariable UUID petId,
                                                @PathVariable UUID eventId) {
        return service.get(petId, eventId)
                .map(EventResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST (com filtros opcionais)
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<EventResponseDTO> list(@PathVariable UUID petId,
                                       @RequestParam(required = false) EventType type,
                                       @RequestParam(required = false) EventPhase phase,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(defaultValue = "date") String sortBy,
                                       @RequestParam(defaultValue = "false") boolean asc) {
        return service.list(petId, type, phase, page, size, sortBy, asc)
                .stream().map(EventResponseDTO::fromModel).toList();
    }

    // UPDATE (parcial – só campos enviados não-nulos)
    @PreAuthorize("permitAll()")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable UUID petId,
                                                   @PathVariable UUID eventId,
                                                   @RequestBody EventRequestDTO body) {
        EventModel incoming = body.toModel(petId);
        return service.update(petId, eventId, incoming)
                .map(EventResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("permitAll()")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable UUID petId,
                                       @PathVariable UUID eventId) {
        boolean removed = service.delete(petId, eventId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}