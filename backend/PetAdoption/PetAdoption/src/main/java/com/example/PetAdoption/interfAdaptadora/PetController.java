package com.example.PetAdoption.interfAdaptadora;

import com.example.PetAdoption.dominio.entidades.PetModel;
import com.example.PetAdoption.dominio.enums.PetStatus;
import com.example.PetAdoption.servicos.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<PetModel> create(@RequestBody PetModel body) {
        PetModel saved = service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // READ (by id)
    @GetMapping("/{id}")
    public ResponseEntity<PetModel> get(@PathVariable UUID id) {
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST (com paginação simples e ordenação)
    @GetMapping
    public List<PetModel> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        return service.list(page, size, sortBy, asc);
    }

    // LIST por status
    @GetMapping("/status/{status}")
    public List<PetModel> listByStatus(
            @PathVariable PetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean asc
    ) {
        return service.listByStatus(status, page, size, sortBy, asc);
    }

    // UPDATE (put)
    @PutMapping("/{id}")
    public ResponseEntity<PetModel> update(@PathVariable UUID id, @RequestBody PetModel body) {
        return service.update(id, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}