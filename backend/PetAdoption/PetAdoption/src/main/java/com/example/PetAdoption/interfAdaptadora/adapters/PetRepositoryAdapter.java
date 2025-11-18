package com.example.PetAdoption.interfAdaptadora.adapters;

import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.dominio.InterfRepositories.PetRepositoryPort;
import com.example.PetAdoption.interfAdaptadora.entidades.Pet;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaPetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class PetRepositoryAdapter implements PetRepositoryPort {

    private final JpaPetRepository jpa;

    public PetRepositoryAdapter(JpaPetRepository jpa) {
        this.jpa = jpa;
    }

    private Pageable page(int page, int size, String sortBy, boolean asc) {
        Sort sort = (sortBy == null || sortBy.isBlank())
                ? Sort.unsorted()
                : Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        return PageRequest.of(Math.max(page, 0), Math.max(size, 1), sort);
    }

    @Override
    public PetModel save(PetModel petModel) {
        Pet entity = Pet.fromModel(petModel);   // mapper na ENTIDADE JPA
        Pet saved  = jpa.save(entity);
        return Pet.toModel(saved);              // volta pro domínio
    }

    @Override
    public Optional<PetModel> findById(UUID id) {
        return jpa.findById(id).map(Pet::toModel);
    }

    @Override
    public List<PetModel> findAll(int page, int size, String sortBy, boolean asc) {
        return jpa.findAll(page(page, size, sortBy, asc))
                  .map(Pet::toModel)
                  .getContent();
    }

    @Override
    public List<PetModel> findByStatus(PetStatus status, int page, int size, String sortBy, boolean asc) {
        return jpa.findByStatus(status, page(page, size, sortBy, asc))
                  .map(Pet::toModel)
                  .getContent();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (!jpa.existsById(id)) return false;
        jpa.deleteById(id);
        return true;
    }
}