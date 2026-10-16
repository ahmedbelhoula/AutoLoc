package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final IAgenceService agenceService;

    // 1. ADD (Ajouter une agence)
    @PostMapping
    public ResponseEntity<Agence> add(@RequestBody Agence agence) {
        Agence createdAgence = agenceService.create(agence);
        return new ResponseEntity<>(createdAgence, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer toutes les agences)
    @GetMapping
    public ResponseEntity<List<Agence>> getAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    // 3. GET BY ID (Récupérer une agence par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Agence> getById(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.findById(id));
    }

    // 4. DELETE (Supprimer une agence)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}