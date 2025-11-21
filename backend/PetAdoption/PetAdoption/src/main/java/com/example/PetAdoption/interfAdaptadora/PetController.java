package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.servicos.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    // ========== DTOs ==========
    /** DTO de entrada (request). Todos os campos podem ser nulos para permitir update parcial. */
    public static final class PetRequest {
        public UUID id;
        public String name;
        public String species;
        public String breed;
        public String size;
        public String sex;
        public PetStatus status;
        public UUID rescuedById;
        public String  hasSpecialNeeds;
        public Boolean hasOngoingTreatment;
        public String  hasChronicDisease;
        public Boolean goodWithOtherAnimals;
        public Boolean requiresConstantCare;
        public String petDescription;
        public String petImage;
        public LocalDate registeredDate;
        public LocalDate rescuedAt;

        /** Converte DTO -> Domain (ignora createdAt/updatedAt; o service cuida). */
        public PetModel toDomain() {
            PetModel p = new PetModel();
            p.setId(this.id);
            p.setName(this.name);
            p.setSpecies(this.species);
            p.setBreed(this.breed);
            p.setSize(this.size);
            p.setSex(this.sex);
            p.setStatus(this.status);
            p.setRescuedById(this.rescuedById);
            p.setHasSpecialNeeds(this.hasSpecialNeeds);
            p.setHasOngoingTreatment(this.hasOngoingTreatment);
            p.setHasChronicDisease(this.hasChronicDisease);
            p.setGoodWithOtherAnimals(this.goodWithOtherAnimals);
            p.setRequiresConstantCare(this.requiresConstantCare);
            p.setPetDescription(this.petDescription);
            p.setPetImage(this.petImage);
            p.setRegisteredDate(this.registeredDate);
            p.setRescuedAt(this.rescuedAt);
            return p;
        }

        /** Atualiza um Domain existente com campos não nulos do DTO (merge para update parcial). */
        public void mergeInto(PetModel target) {
            if (this.name != null) target.setName(this.name);
            if (this.species != null) target.setSpecies(this.species);
            if (this.breed != null) target.setBreed(this.breed);
            if (this.size != null) target.setSize(this.size);
            if (this.sex != null) target.setSex(this.sex);
            if (this.status != null) target.setStatus(this.status);
            if (this.rescuedById != null) target.setRescuedById(this.rescuedById);
            if (this.hasSpecialNeeds != null) target.setHasSpecialNeeds(this.hasSpecialNeeds);
            if (this.hasOngoingTreatment != null) target.setHasOngoingTreatment(this.hasOngoingTreatment);
            if (this.hasChronicDisease != null) target.setHasChronicDisease(this.hasChronicDisease);
            if (this.goodWithOtherAnimals != null) target.setGoodWithOtherAnimals(this.goodWithOtherAnimals);
            if (this.requiresConstantCare != null) target.setRequiresConstantCare(this.requiresConstantCare);
            if (this.petDescription != null) target.setPetDescription(this.petDescription);
            if (this.petImage != null) target.setPetImage(this.petImage);
            if (this.registeredDate != null) target.setRegisteredDate(this.registeredDate);
            if (this.rescuedAt != null) target.setRescuedAt(this.rescuedAt);
        }
    }

    /** DTO de saída (response). */
    public static final class PetResponse {
        public UUID id;
        public String name;
        public String species;
        public String breed;
        public String size;
        public String sex;
        public PetStatus status;
        public UUID rescuedById;
        public String  hasSpecialNeeds;
        public Boolean hasOngoingTreatment;
        public String  hasChronicDisease;
        public Boolean goodWithOtherAnimals;
        public Boolean requiresConstantCare;
        public String petDescription;
        public String petImage;
        public LocalDate registeredDate;
        public LocalDate rescuedAt;
        public OffsetDateTime createdAt;
        public OffsetDateTime updatedAt;

        /** Converte Domain -> DTO. */
        public static PetResponse from(PetModel p) {
            PetResponse r = new PetResponse();
            r.id = p.getId();
            r.name = p.getName();
            r.species = p.getSpecies();
            r.breed = p.getBreed();
            r.size = p.getSize();
            r.sex = p.getSex();
            r.status = p.getStatus();
            r.rescuedById = p.getRescuedById();
            r.hasSpecialNeeds = p.getHasSpecialNeeds();
            r.hasOngoingTreatment = p.getHasOngoingTreatment();
            r.hasChronicDisease = p.getHasChronicDisease();
            r.goodWithOtherAnimals = p.getGoodWithOtherAnimals();
            r.requiresConstantCare = p.getRequiresConstantCare();
            r.petDescription = p.getPetDescription();
            r.petImage = p.getPetImage();
            r.registeredDate = p.getRegisteredDate();
            r.rescuedAt = p.getRescuedAt();
            r.createdAt = p.getCreatedAt();
            r.updatedAt = p.getUpdatedAt();
            return r;
        }
    }
    // ========== /DTOs ==========

    // CREATE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PetResponse> create(@RequestBody PetRequest body) {
        PetModel saved = service.create(body.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(PetResponse.from(saved));
    }

    // READ (by id)
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> get(@PathVariable UUID id) {
        return service.get(id)
                .map(PetResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST (com paginação simples e ordenação)
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<PetResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        return service.list(page, size, sortBy, asc).stream()
                .map(PetResponse::from)
                .toList();
    }

    // LIST por status
    @PreAuthorize("permitAll()")
    @GetMapping("/status/{status}")
    public List<PetResponse> listByStatus(
            @PathVariable PetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        return service.listByStatus(status, page, size, sortBy, asc).stream()
                .map(PetResponse::from)
                .toList();
    }

    // UPDATE (PUT) — continua mesma rota/semântica, mas agora com merge de campos não nulos
    @PreAuthorize("@petSecurity.canEdit(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> update(@PathVariable UUID id, @RequestBody PetRequest body) {
        return service.get(id)
                .map(existing -> {
                    body.mergeInto(existing);                 // aplica somente campos enviados
                    existing.setUpdatedAt(OffsetDateTime.now());
                    return service.update(id, existing).orElse(existing);
                })
                .map(PetResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @PreAuthorize("@petSecurity.canEdit(#id, authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        // mantida exatamente a mesma resposta/semântica anterior
    }
    @PreAuthorize("@petSecurity.canEdit(#id, authentication)")
    @PostMapping("/{id}/image")
    public ResponseEntity<PetResponse> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        return service.updatePetImage(id, file)
                .map(PetResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
