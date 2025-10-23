package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.entidades.OrgModel;
import com.example.PetAdoption.servicos.OrgService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orgs")
public class OrgController {

    private final OrgService service;
    public OrgController(OrgService service) { this.service = service; }

    // ===== DTOs internos =====
    public static class OrgRequestDTO {
        @NotBlank public String name;
        @Email   public String email;
        @Pattern(regexp = "^[+()\\d\\s.-]{6,}$", message = "telefone inválido")
        public String phone;
        public UUID petOwnedId;

        public OrgModel toModel() {
            OrgModel m = new OrgModel();
            m.setName(name);
            m.setEmail(email);
            m.setPhone(phone);
            m.setPetOwnedId(petOwnedId);
            return m;
        }
    }

    public static class OrgResponseDTO {
        public UUID id;
        public String name;
        public String email;
        public String phone;
        public UUID petOwnedId;

        public static OrgResponseDTO fromModel(OrgModel m) {
            OrgResponseDTO dto = new OrgResponseDTO();
            dto.id = m.getId();
            dto.name = m.getName();
            dto.email = m.getEmail();
            dto.phone = m.getPhone();
            dto.petOwnedId = m.getPetOwnedId();
            return dto;
        }
    }
    // ==========================

    @PostMapping
    public ResponseEntity<OrgResponseDTO> create(@RequestBody OrgRequestDTO body) {
        OrgModel saved = service.create(body.toModel());
        return ResponseEntity.created(URI.create("/api/orgs/" + saved.getId()))
                             .body(OrgResponseDTO.fromModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgResponseDTO> get(@PathVariable UUID id) {
        return service.get(id)
                .map(OrgResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST: “listagem só de org” com paginação/ordenação e filtro q
    @GetMapping
    public List<OrgResponseDTO> list(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     @RequestParam(defaultValue = "name") String sortBy,
                                     @RequestParam(defaultValue = "true") boolean asc,
                                     @RequestParam(required = false) String q) {
        return service.list(page, size, sortBy, asc, q)
                      .stream().map(OrgResponseDTO::fromModel).toList();
    }

    // update parcial (mesmo padrão do EventController usa PUT)
    @PutMapping("/{id}")
    public ResponseEntity<OrgResponseDTO> update(@PathVariable UUID id,
                                                 @RequestBody OrgRequestDTO body) {
        return service.update(id, body.toModel())
                .map(OrgResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
