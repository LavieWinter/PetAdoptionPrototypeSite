package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.EventRepositoryPort;
import com.example.PetAdoption.dominio.entidades.EventModel;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepositoryPort events;

    public EventService(EventRepositoryPort events) {
        this.events = events;
    }

    /**
     * Cria um evento para um pet. O ID é gerado pela JPA (@UuidGenerator) quando vier null.
     * eventType e phase são obrigatórios (colunas NOT NULL).
     */
    public EventModel create(EventModel model) {
        model.setId(null); // garante geração do UUID pela entidade JPA

        if (model.getPetId() == null) {
            throw new IllegalArgumentException("petId é obrigatório para criar um evento.");
        }
        if (model.getEventType() == null) {
            throw new IllegalArgumentException("eventType é obrigatório.");
        }
        if (model.getPhase() == null) {
            throw new IllegalArgumentException("phase é obrigatória.");
        }

        return events.save(model);
    }

    /** Busca um evento específico de um pet. */
    public Optional<EventModel> get(UUID petId, UUID eventId) {
        return events.findById(petId, eventId);
    }

    /** Lista eventos do pet com filtros opcionais (type/phase) e paginação/ordenação. */
    public List<EventModel> list(UUID petId,
                                 EventType type,
                                 EventPhase phase,
                                 int page,
                                 int size,
                                 String sortBy,
                                 boolean asc) {
        return events.findAll(petId, type, phase, page, size, sortBy, asc);
    }

    /**
     * Update parcial: aplica somente campos não nulos do 'incoming'.
     * petId e eventId são usados para garantir o escopo do recurso.
     */
    public Optional<EventModel> update(UUID petId, UUID eventId, EventModel incoming) {
        return events.findById(petId, eventId).map(existing -> {
            // Coerência de escopo
            if (incoming.getPetId() != null && !incoming.getPetId().equals(petId)) {
                // Se vier petId diferente no payload, ignoramos e mantemos o petId original
                incoming.setPetId(petId);
            }

            // Campos editáveis (aplicar somente se não nulos)
            if (incoming.getEventType() != null) existing.setEventType(incoming.getEventType());
            if (incoming.getPhase() != null) existing.setPhase(incoming.getPhase());
            if (incoming.getDate() != null) existing.setDate(incoming.getDate());
            if (incoming.getIncidentType() != null) existing.setIncidentType(incoming.getIncidentType());
            if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
            if (incoming.getLocation() != null) existing.setLocation(incoming.getLocation());
            if (incoming.getAdoptionId() != null) existing.setAdoptionId(incoming.getAdoptionId());

            return events.save(existing);
        });
    }

    /** Remove um evento do pet. */
    public boolean delete(UUID petId, UUID eventId) {
        return events.delete(petId, eventId);
    }
}