package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.entidades.AdoptionApplicationModel;
import com.example.PetAdoption.interfAdaptadora.dtos.*;
import com.example.PetAdoption.servicos.AdoptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    private final AdoptionService service;
    public AdoptionController(AdoptionService service) {
        this.service = service;
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/applications")
    public ResponseEntity<AdoptionDtos.ApplicationResponse> create(
            Authentication auth,
            @RequestBody AdoptionDtos.CreateRequest req
    ) {
        if (req.petId == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean useDefault = (req.useDefaultPreferences == null) || req.useDefaultPreferences;
        try{
            var m = service.createApplicationForLoggedUser(auth.getName(), req.petId, useDefault);
            return ResponseEntity.ok(AdoptionDtos.ApplicationResponse.of(m));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/applications")
    public ResponseEntity<AdoptionDtos.PageResponse> listMine(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResult<AdoptionApplicationModel> result = service.listMyApplications(auth.getName(), page, size);
        var content = result.content.stream()
                .map(AdoptionDtos.ApplicationResponse::of)
                .collect(Collectors.toList());

        var out = new AdoptionDtos.PageResponse();
        out.page = result.page;
        out.size = result.size;
        out.totalElements = result.totalElements;
        out.totalPages = result.totalPages;
        out.content = content;
        return ResponseEntity.ok(out);
    }
    @PreAuthorize("permitAll()")
    @PostMapping("/applications/{id}/cancel")
    public ResponseEntity<Void> cancelMine(Authentication auth, @PathVariable UUID id) {
        service.cancelMyApplication(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable UUID id) {
        service.approve(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/applications/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable UUID id) {
        service.reject(id);
        return ResponseEntity.noContent().build();
    }
}