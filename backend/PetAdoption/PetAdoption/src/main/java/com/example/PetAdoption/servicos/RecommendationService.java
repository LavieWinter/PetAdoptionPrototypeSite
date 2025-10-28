package com.example.PetAdoption.servicos;

import com.example.PetAdoption.dominio.InterfRepositories.UserRepository;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.interfAdaptadora.dtos.RecommendationDtos;
import com.example.PetAdoption.interfAdaptadora.interfJPA.JpaRecommendationRepository;
import com.example.PetAdoption.interfAdaptadora.interfJPA.projections.PetRecommendationRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecommendationService {

    private final JpaRecommendationRepository jpaRecos;
    private final UserRepository users;

    public RecommendationService(JpaRecommendationRepository jpaRecos, UserRepository users) {
        this.jpaRecos = jpaRecos;
        this.users = users;
    }

    /** Resolve o adopterId pelo e-mail (subject do token). */
    public UUID resolveAdopterIdByEmail(String email) {
        UserModel u = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + email));
        return u.getId();
    }

    public RecommendationDtos.PageResponse recommendForAdopter(UUID adopterId, UUID applicationId, Pageable pageable) {
        Page<PetRecommendationRow> page = jpaRecos.recommendForAdopter(adopterId, applicationId, pageable);

        var out = new RecommendationDtos.PageResponse();
        out.page = page.getNumber();
        out.size = page.getSize();
        out.totalElements = page.getTotalElements();
        out.totalPages = page.getTotalPages();
        out.content = page.map(RecommendationService::toItem).getContent();
        return out;
    }

    private static RecommendationDtos.Item toItem(PetRecommendationRow r) {
        var it = new RecommendationDtos.Item();
        it.petId = r.getPetId();
        it.name = r.getName();
        it.species = r.getSpecies();
        it.size = r.getSize();
        it.sex = r.getSex();
        it.score = r.getScore();
        return it;
    }
}