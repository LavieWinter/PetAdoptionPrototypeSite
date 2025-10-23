package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.OrgRepositoryPort;
import com.example.PetAdoption.dominio.entidades.OrgModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrgService {

    private final OrgRepositoryPort orgs;

    public OrgService(OrgRepositoryPort orgs) {
        this.orgs = orgs;
    }

    public OrgModel create(OrgModel model) {
        model.setId(null); // UUID gerado pela entidade JPA
        if (model.getName() == null || model.getName().isBlank()) {
            throw new IllegalArgumentException("name é obrigatório.");
        }
        if (model.getEmail() != null && orgs.existsByEmailIgnoreCase(model.getEmail())) {
            throw new IllegalArgumentException("email já utilizado por outra org.");
        }
        return orgs.save(model);
    }

    public Optional<OrgModel> get(UUID id) {
        return orgs.findById(id);
    }

    public List<OrgModel> list(int page, int size, String sortBy, boolean asc, String q) {
        return orgs.findAll(page, size, sortBy, asc, q);
    }

    public Optional<OrgModel> update(UUID id, OrgModel incoming) {
        return orgs.findById(id).map(existing -> {
            if (incoming.getName() != null) existing.setName(incoming.getName());
            if (incoming.getEmail() != null) {
                String newEmail = incoming.getEmail();
                if (!newEmail.equalsIgnoreCase(existing.getEmail())
                        && orgs.existsByEmailIgnoreCase(newEmail)) {
                    throw new IllegalArgumentException("email já utilizado por outra org.");
                }
                existing.setEmail(newEmail);
            }
            if (incoming.getPhone() != null) existing.setPhone(incoming.getPhone());
            if (incoming.getPetOwnedId() != null) existing.setPetOwnedId(incoming.getPetOwnedId());
            return orgs.save(existing);
        });
    }

    public boolean delete(UUID id) {
        return orgs.deleteById(id);
    }
}
