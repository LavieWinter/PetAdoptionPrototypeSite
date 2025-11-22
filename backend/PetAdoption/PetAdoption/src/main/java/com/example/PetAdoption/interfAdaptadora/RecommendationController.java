package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.interfAdaptadora.dtos.RecommendationDtos;
import com.example.PetAdoption.servicos.RecommendationService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    /**
     * Recomendações para o usuário logado.
     * GET /api/pets/recommendations?page=0&size=10&applicationId=<uuid>
     *
     * - Se applicationId for informado, usa snapshot (application_preferences).
     * - Senão, usa adopter_preferences (default).
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationDtos.PageResponse> getRecommendations(
            Authentication auth,
            @RequestParam(name = "applicationId", required = false) UUID applicationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Ordenação default: score desc (já vem da query); Pageable pode ter sort extra se quiser
        Pageable pageable = PageRequest.of(page, size);
        UUID adopterId = service.resolveAdopterIdByEmail(auth.getName());
        RecommendationDtos.PageResponse res = service.recommendForAdopter(adopterId, applicationId, pageable);
        return ResponseEntity.ok(res);
    }
}