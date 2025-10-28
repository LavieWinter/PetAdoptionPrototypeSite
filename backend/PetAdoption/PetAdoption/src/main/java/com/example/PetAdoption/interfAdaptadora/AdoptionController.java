package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.InterfRepositories.UserRepository;
import com.example.PetAdoption.dominio.entidades.PetRecommendationModel;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.interfAdaptadora.dtos.AdoptionDtos;
import com.example.PetAdoption.servicos.AdoptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    private final AdoptionService service;
    private final UserRepository users;

    public AdoptionController(AdoptionService service, UserRepository users) {
        this.service = service;
        this.users = users;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<AdoptionDtos.RecommendationResponse> recommendations(
            Authentication auth,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) UUID applicationId
    ) {
        UUID adopterId = resolveUserId(auth);
        List<PetRecommendationModel> recs = service.rankedPets(adopterId, applicationId, limit, offset);
        var out = new AdoptionDtos.RecommendationResponse();
        out.items = recs.stream().map(r -> {
            var i = new AdoptionDtos.RecommendationItem();
            i.petId = r.getPetId().toString();
            i.name = r.getName();
            i.species = r.getSpecies();
            i.size = r.getSize();
            i.score = r.getScore();
            return i;
        }).toList();
        out.limit = limit;
        out.offset = offset;
        return ResponseEntity.ok(out);
    }

    @PostMapping("/applications")
    public ResponseEntity<AdoptionDtos.CreateApplicationResponse> createApplication(
            Authentication auth,
            @Valid @RequestBody AdoptionDtos.CreateApplicationRequest req
    ) {
        UUID adopterId = resolveUserId(auth);
        UUID petId = UUID.fromString(req.petId);
        boolean useDefaults = req.useDefaultPreferences == null ? true : req.useDefaultPreferences;
        UUID appId = service.createApplication(adopterId, petId, useDefaults);

        var out = new AdoptionDtos.CreateApplicationResponse();
        out.applicationId = appId.toString();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/applications/{applicationId}/confirm")
    public ResponseEntity<AdoptionDtos.ConfirmResponse> confirm(
            @PathVariable UUID applicationId
    ) {
        service.confirmAdoption(applicationId);
        var out = new AdoptionDtos.ConfirmResponse();
        out.status = "OK";
        return ResponseEntity.ok(out);
    }

    private UUID resolveUserId(Authentication auth) {
        String email = auth.getName();
        UserModel u = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + email));
        return u.getId();
    }
}