package com.example.PetAdoption.interfAdaptadora.adapters;

import com.example.PetAdoption.dominio.InterfRepositories.OrgRepositoryPort;
import com.example.PetAdoption.dominio.entidades.OrgModel;
import com.example.PetAdoption.interfAdaptadora.entidades.Org;
import com.example.PetAdoption.interfAdaptadora.interfJPA.OrgJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrgRepositoryAdapter implements OrgRepositoryPort {

    private final OrgJpaRepository repo;

    public OrgRepositoryAdapter(OrgJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public OrgModel save(OrgModel model) {
        Org saved = repo.save(Org.fromModel(model));
        return Org.toModel(saved);
    }

    @Override
    public Optional<OrgModel> findById(UUID id) {
        return repo.findById(id).map(Org::toModel);
    }

    @Override
    public List<OrgModel> findAll(int page, int size, String sortBy, boolean asc, String q) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sort);

        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(Org::toModel).toList();
        }
        String s = q.trim();
        return repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                s, s, s, pageable
        ).map(Org::toModel).toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        return repo.existsByEmailIgnoreCase(email);
    }
}
