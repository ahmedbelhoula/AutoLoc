package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContratController {

    private final IContratService contratService;

    // 1. ADD (Ajouter un contrat)
    @PostMapping
    public ResponseEntity<Contrat> add(@RequestBody Contrat contrat) {
        Contrat createdContrat = contratService.create(contrat);
        return new ResponseEntity<>(createdContrat, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer tous les contrats)
    @GetMapping
    public ResponseEntity<List<Contrat>> getAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    // 3. GET BY ID (Récupérer un contrat par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    // 4. DELETE (Supprimer un contrat)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}