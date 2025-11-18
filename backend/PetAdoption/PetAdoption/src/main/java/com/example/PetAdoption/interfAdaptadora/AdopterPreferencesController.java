package com.example.PetAdoption.interfAdaptadora;
import com.example.PetAdoption.dominio.InterfRepositories.UserRepository;
import com.example.PetAdoption.dominio.entidades.AdopterPreferencesModel;
import com.example.PetAdoption.dominio.entidades.UserModel;
import com.example.PetAdoption.servicos.AdopterPreferencesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AdopterPreferencesController {

    private final AdopterPreferencesService service;
    private final UserRepository users;

    public AdopterPreferencesController(AdopterPreferencesService service, UserRepository users) {
        this.service = service;
        this.users = users;
    }

    // ======== "ME" endpoints ========

    @GetMapping("/preferences")
    public ResponseEntity<?> getMine(Authentication auth) {
        UUID me = resolveUserId(auth);
        Optional<AdopterPreferencesModel> opt = service.getByAdopterId(me);
        return opt.<ResponseEntity<?>>map(m -> ResponseEntity.ok(toResponse(m)))
                  .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/preferences")
    public ResponseEntity<?> upsertMine(Authentication auth,
                                        @Valid @RequestBody UpsertRequest req) {
        UUID me = resolveUserId(auth);
        AdopterPreferencesModel in = fromRequest(req);
        AdopterPreferencesModel saved = service.upsert(me, in);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/preferences")
    public ResponseEntity<?> deleteMine(Authentication auth) {
        UUID me = resolveUserId(auth);
        service.deleteByAdopterId(me);
        return ResponseEntity.noContent().build();
    }

    // ======== Admin/operacional (opcional) ========
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{adopterId}/preferences")
    public ResponseEntity<?> getByUser(@PathVariable UUID adopterId) {
        return service.getByAdopterId(adopterId)
                .<ResponseEntity<?>>map(m -> ResponseEntity.ok(toResponse(m)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{adopterId}/preferences")
    public ResponseEntity<?> upsertByUser(@PathVariable UUID adopterId,
                                          @Valid @RequestBody UpsertRequest req) {
        AdopterPreferencesModel in = fromRequest(req);
        AdopterPreferencesModel saved = service.upsert(adopterId, in);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{adopterId}/preferences")
    public ResponseEntity<?> deleteByUser(@PathVariable UUID adopterId) {
        service.deleteByAdopterId(adopterId);
        return ResponseEntity.noContent().build();
    }

    // ======== helpers ========
    private UUID resolveUserId(Authentication auth) {
        String email = auth.getName();
        UserModel u = users.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado: " + email));
        return u.getId();
    }

    private static AdopterPreferencesModel fromRequest(UpsertRequest r) {
        AdopterPreferencesModel m = new AdopterPreferencesModel();
        m.setDesiredSpecies(r.desiredSpecies);
        m.setDesiredSize(r.desiredSize);
        m.setAcceptsSpecialNeeds(r.acceptsSpecialNeeds);
        m.setAcceptsOngoingTreatment(r.acceptsOngoingTreatment);
        m.setAcceptsChronicDisease(r.acceptsChronicDisease);
        m.setHasOtherPets(r.hasOtherPets);
        m.setHasTimeForConstantCare(r.hasTimeForConstantCare);
        return m;
    }

    private static Response toResponse(AdopterPreferencesModel m) {
        var out = new Response();
        out.preferenceKey = m.getPreferenceKey() != null ? m.getPreferenceKey().toString() : null;
        out.adopterId = m.getAdopterId() != null ? m.getAdopterId().toString() : null;
        out.desiredSpecies = m.getDesiredSpecies();
        out.desiredSize = m.getDesiredSize();
        out.acceptsSpecialNeeds = m.getAcceptsSpecialNeeds();
        out.acceptsOngoingTreatment = m.getAcceptsOngoingTreatment();
        out.acceptsChronicDisease = m.getAcceptsChronicDisease();
        out.hasOtherPets = m.getHasOtherPets();
        out.hasTimeForConstantCare = m.getHasTimeForConstantCare();
        out.updatedAt = (m.getUpdatedAt() != null) ? m.getUpdatedAt().toString() : null;
        return out;
    }

    // ======== DTOs aninhadas ========
    public static class UpsertRequest {
        // validações leves; ajuste com enums/regex se preferir
        @Size(max = 20)
        public String desiredSpecies; // "DOG","CAT",...

        @Size(max = 20)
        public String desiredSize;    // "SMALL","MEDIUM","LARGE",...

        public Boolean acceptsSpecialNeeds;
        public Boolean acceptsOngoingTreatment;
        public Boolean acceptsChronicDisease;
        public Boolean hasOtherPets;
        public Boolean hasTimeForConstantCare;
    }

    public static class Response {
        public String preferenceKey;
        public String adopterId;
        public String desiredSpecies;
        public String desiredSize;
        public Boolean acceptsSpecialNeeds;
        public Boolean acceptsOngoingTreatment;
        public Boolean acceptsChronicDisease;
        public Boolean hasOtherPets;
        public Boolean hasTimeForConstantCare;
        public String updatedAt;
    }
}