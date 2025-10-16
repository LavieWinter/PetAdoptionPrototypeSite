package com.example.PetAdoption.interfAdaptadora.adapters;

import com.example.PetAdoption.dominio.InterfRepositories.EventRepositoryPort;
import com.example.PetAdoption.dominio.entidades.EventModel;
import com.example.PetAdoption.dominio.enums.EventPhase;
import com.example.PetAdoption.dominio.enums.EventType;
import com.example.PetAdoption.interfAdaptadora.entidades.Event;
import com.example.PetAdoption.interfAdaptadora.interfJPA.EventJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventJpaRepository repo;

    public EventRepositoryAdapter(EventJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public EventModel save(EventModel event) {
        // Entity ↔ Model usando seus conversores estáticos
        Event entity = Event.fromModel(event);
        Event saved = repo.save(entity);
        return Event.toModel(saved);
    }

    @Override
    public Optional<EventModel> findById(UUID petId, UUID eventId) {
        return repo.findByIdAndPetId(eventId, petId).map(Event::toModel);
    }

    @Override
    public List<EventModel> findAll(UUID petId, EventType type, EventPhase phase,
            int page, int size, String sortBy, boolean asc) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        var pageRes = (type != null && phase != null)
                ? repo.findAllByPetIdAndEventTypeAndPhase(petId, type, phase, pageable)
                : (type != null) ? repo.findAllByPetIdAndEventType(petId, type, pageable)
                        : (phase != null) ? repo.findAllByPetIdAndPhase(petId, phase, pageable)
                                : repo.findAllByPetId(petId, pageable);

        return pageRes.map(Event::toModel).toList();
    }

    @Override
    public boolean delete(UUID petId, UUID eventId) {
        return repo.findByIdAndPetId(eventId, petId)
                .map(e -> {
                    repo.delete(e); // precisa de transação ativa
                    return true;
                })
                .orElse(false);
    }

}